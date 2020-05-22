package com.mall.discover.web.service.impl.biz;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.redis.util.RedisTemplateUtil;
import com.mall.common.exception.BusinessException;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.BizTypeEnum;
import com.mall.discover.common.enums.DiscoverCountEnum;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.request.client.*;
import com.mall.discover.response.client.ClientLikePageResponse;
import com.mall.discover.web.common.constants.DubboConstants;
import com.mall.discover.web.util.AccessTokenUtil;
import com.mall.discover.web.util.DiscoverUtil;
import com.mall.discover.web.util.RedisLock;
import com.mall.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.Min;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: huangzhong
 * @Date: 2019/11/4
 * @Description:
 */
@Service
@Slf4j
public class ClientLikeBiz {
    @Autowired
    private RedisLock redisLock;

    @Reference(consumer = DubboConstants.SIBU_MALL_USER_CONSUMER)
    private UserService userService;
    /**
     *  文章点赞用户缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.LIKE_USER_ARTICLE_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Long, LinkedHashSet<Integer>> articleLikeUserListCache;

    /**
     *  用户点赞列表统计缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.LIKE_USER_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, Map<Integer,LinkedHashSet<Long>>> userLikeCountChache;


    /**
     *  更新点赞
     * @param request
     * @return
     */
    public CommonResponse<Boolean> updateLike(ClientUpdateLikeRequest request) {
        Integer userId = AccessTokenUtil.getCurrentUserId();
        if (userId != null) {
            //登录后 更新缓存集合
            String key = redisLock.getKey(request.getBizTypeId(), request.getCommonId());
            try {
                if (redisLock.lock(key)) {
                    //更新点赞
                    updateLikeUser(userId, request);
                }
            } catch (BusinessException e) {
                log.error("更新点赞出错,错误信息：{}", e.getMessage());
                return ResponseManage.fail("点赞失败");
            } finally {
                redisLock.unlock(key);
            }

            //用户点赞统计（冗余信息，后期会使用）
//            updateUserLikeMap(request, userId);
        }
        return ResponseManage.success(true);
    }

    /**
     * 点赞列表
     * @param request
     * @return
     */
    public CommonResponse<CommonPageResult<ClientLikePageResponse>> queryLikePage(ClientLikePageRequest request){
        List<Integer> userIds = new ArrayList<>();
        long totalCount = 0L;

        //文章点赞列表
        if (BizTypeEnum.ARTICLE.getCode().equals(request.getBizTypeId())) {
            totalCount = getUserIdsAndSize(request, userIds);
        }

        //从用户系统中获取信息
        List<ClientLikePageResponse> responseList =  DiscoverUtil.getUserInfoList(userService, userIds);

        //封装返回值
        CommonPageResult<ClientLikePageResponse> result = new CommonPageResult<>();
        result.setTotalCount(totalCount);
        result.setTotalPage(PageUtils.getPageCount(request.getPageSize(), totalCount));
        result.setData(responseList);
        return ResponseManage.success(result);
    }

    /**
     * 用户点赞统计（冗余信息，后期会使用）
     * @param request
     * @param userId
     */
    private void updateUserLikeMap(ClientUpdateLikeRequest request, Integer userId) {
        Map<Integer, LinkedHashSet<Long>> map = userLikeCountChache.get(userId);
        if (map == null) {
            map = new HashMap<>();
        }
        LinkedHashSet<Long> ids = map.get(request.getBizTypeId());
        long commonId = request.getCommonId();
        if (request.getLikeStatus()) {
            if (ids == null) {
                ids = new LinkedHashSet<>();
            }
            if (!ids.contains(commonId)) {
                ids.add(commonId);
                map.put(request.getBizTypeId(), ids);
                userLikeCountChache.put(userId, map);
            }
            //取消点赞
        }else{
            if (!CollectionUtils.isEmpty(ids) && ids.contains(commonId)) {
                ids.remove(commonId);
                userLikeCountChache.put(userId, map);
            }
        }
    }

    /**
     * 获取分页用户id集合
     * @param request
     * @param userIds
     * @return
     */
    private long getUserIdsAndSize(ClientLikePageRequest request, List<Integer> userIds) {
        LinkedHashSet<Integer> userIdList = articleLikeUserListCache.get(request.getCommonId());
        if (ObjectUtils.isEmpty(userIdList)) {
            return 0L;
        }
        Integer pageSize = request.getPageSize();
        int startSize = userIdList.size() - (pageSize * request.getCurrentPage());
        if (startSize > 0) {
            userIds.addAll(userIdList.stream().skip(startSize).limit(pageSize).collect(Collectors.toList()));
        } else if (startSize > -pageSize) {
            userIds.addAll(userIdList.stream().limit(pageSize - Math.abs(startSize)).collect(Collectors.toList()));
        }

        //转换顺序
        Collections.reverse(userIds);
        return userIdList.size();
    }

    /**
     * 更新点赞用户
     * @param userId
     */
    private void updateLikeUser(Integer userId, ClientUpdateLikeRequest request) {
        //文章点赞
        if (BizTypeEnum.ARTICLE.getCode().equals(request.getBizTypeId())) {
            long commonId = request.getCommonId();
            LinkedHashSet<Integer> list = articleLikeUserListCache.get(commonId);
            if (list == null) {
                list = new LinkedHashSet<>(1024);
            }
            if (request.getLikeStatus()) {
                if (!list.contains(userId)) {
                    list.add(userId);
                    articleLikeUserListCache.put(commonId, list);
                    //更新count
                    RedisTemplateUtil.incrLong(DiscoverCountEnum.ARTICLE_LIKE_COUNT.getCode() + commonId);
                }
                //取消点赞
            }else{
                if (list.contains(userId)) {
                    list.remove(userId);
                    articleLikeUserListCache.put(commonId, list);
                    //更新count
                    RedisTemplateUtil.decrLong(DiscoverCountEnum.ARTICLE_LIKE_COUNT.getCode() + commonId);
                }
            }
        }
    }
}
