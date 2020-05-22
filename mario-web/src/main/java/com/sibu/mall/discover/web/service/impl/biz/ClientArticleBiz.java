package com.mall.discover.web.service.impl.biz;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.redis.util.RedisTemplateUtil;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.BizTypeEnum;
import com.mall.discover.common.enums.DiscoverCountEnum;
import com.mall.discover.request.client.ClientArticleInfoRequest;
import com.mall.discover.request.client.ClientArticlePageRequest;
import com.mall.discover.request.client.ClientUpdateCountRequest;
import com.mall.discover.response.client.*;
import com.mall.discover.service.ClientArticleService;
import com.mall.discover.service.ClientProductService;
import com.mall.discover.web.common.constants.DubboConstants;
import com.mall.discover.web.support.config.properties.DiscoverProperties;
import com.mall.discover.web.util.AccessTokenUtil;
import com.mall.discover.web.util.DiscoverUtil;
import com.mall.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: huangzhong
 * @Date: 2019/11/4
 * @Description:
 */
@Service
@Slf4j
public class ClientArticleBiz {
    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientArticleService clientArticleService;

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientProductService clientProductService;

    @Reference(consumer = DubboConstants.SIBU_MALL_USER_CONSUMER)
    private UserService userService;

    @Autowired
    private DiscoverProperties discoverProperties;

    /**
     *  文章列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.ARTICLE_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientDiscoverResponse>> articleQueryPage;

    /**
     *  文章详情缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.ARTICLE_QUERY_INFO,
            cacheType = CacheType.REMOTE)
    private Cache<Long, ClientArticleInfoResponse> articleQueryInfo;

    /**
     *  文章点赞用户缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.LIKE_USER_ARTICLE_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Long, LinkedHashSet<Integer>> articleLikeUserListCache;

    /**
     *  商品编号和商品id对应关系
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.PRODUCT_NO_ID_RELATION,
            cacheType = CacheType.REMOTE)
    private Cache<String, Long> productNoIdRelation;


    /**
     * 文章列表
     */
    public CommonResponse<CommonPageResult<ClientDiscoverResponse>> queryArticlePage(ClientArticlePageRequest request){
        CommonPageResult<ClientDiscoverResponse> result = new CommonPageResult<>();
        try {
            result = articleQueryPage.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("获取缓存文章列表出错,错误信息：{}", e.getMessage());
        }

        //DB中获取
        if (result == null) {
            CommonResponse<CommonPageResult<ClientDiscoverResponse>> response = clientArticleService.queryArticlePage(request, false);
            if (!response.isSuccess()) {
                log.error("DB中获取文章列表出错,错误信息：{}", response);
                return ResponseManage.success(new CommonPageResult<>());
            }
            result = response.getResult();
        }

        //筛选文章列表
        List<ClientArticleResponse> articleResponseList = result.getData()
                .stream()
                .filter(response -> response instanceof ClientArticleResponse)
                .map(a -> (ClientArticleResponse) a)
                .collect(Collectors.toList());
        //获取用户点赞信息
        DiscoverUtil.getUserLikePage(articleLikeUserListCache, articleResponseList);
        return ResponseManage.success(result);
    }

    /**
     * 文章详情
     */
    public CommonResponse<ClientArticleInfoResponse> articleInfo(ClientArticleInfoRequest request) {
        ClientArticleInfoResponse infoResponse = new ClientArticleInfoResponse();
        try {
            //自增阅读数
            RedisTemplateUtil.incrLong(DiscoverCountEnum.ARTICLE_READ_COUNT.getCode() + request.getArticleId());
            //获取返回值
            infoResponse = articleQueryInfo.get(request.getArticleId());
        } catch (Exception e) {
            log.error("获取缓存文章详情出错,错误信息：{}", e.getMessage());
        }

        //DB中获取
        if (infoResponse == null) {
            CommonResponse<ClientArticleInfoResponse> result = clientArticleService.queryArticleInfo(request, false);
            if (!result.isSuccess()) {
                log.error("DB中获取文章详情出错,错误信息：{}", result);
                return ResponseManage.fail("该文章详情不存在");
            } else {
                infoResponse = result.getResult();
            }
        }

        //获取用户点赞信息
        ClientLikeResponse response = getUserLikeInfo(request);
        infoResponse.setClientLikeResponse(response);

        //相关推荐更新点赞信息
        DiscoverUtil.getUserLikePage(articleLikeUserListCache, infoResponse.getArticleList());
        return ResponseManage.success(infoResponse);
    }

    /**
     * 更新分享次数
     */
    public CommonResponse<Boolean> updateCount(ClientUpdateCountRequest request) {
        Integer bizTypeId = request.getBizTypeId();
        try {
            if (BizTypeEnum.ARTICLE.getCode().equals(bizTypeId)) {
                //文章
                RedisTemplateUtil.incrLong(DiscoverCountEnum.ARTICLE_SHARE_COUNT.getCode() + request.getCommonId());
            } else if (BizTypeEnum.PRODUCT.getCode().equals(bizTypeId)) {
                //商品 兼容商品编号
                Long productId =  DiscoverUtil.getProductId(request.getCommonId(), request.getProductNo(), productNoIdRelation, clientProductService);
                if (!ObjectUtils.isEmpty(productId)) {
                    RedisTemplateUtil.incrLong(DiscoverCountEnum.PRODUCT_SHARE_COUNT.getCode() + productId);
                }
            }else if (BizTypeEnum.SUBJECT.getCode().equals(bizTypeId)) {
                //话题
                RedisTemplateUtil.incrLong(DiscoverCountEnum.SUBJECT_SHARE_COUNT.getCode() + request.getCommonId());
            }
        } catch (Exception e) {
            log.error("更新分享数出错,错误信息：{}", e.getMessage());
        }

        return ResponseManage.success(true);
    }

    /**
     * 详情获取用户列表
     * @param request
     * @return
     */
    private ClientLikeResponse getUserLikeInfo(ClientArticleInfoRequest request) {
        ClientLikeResponse response = new ClientLikeResponse();
        LinkedHashSet<Integer> totalUserIds = articleLikeUserListCache.get(request.getArticleId());
        if (!ObjectUtils.isEmpty(totalUserIds)) {
            List<Integer> userIds = new ArrayList<>();
            //详情内显示点赞头像最大数量
            Integer iconUrlMaxSize = discoverProperties.getIconUrlMaxSize();
            int startSize = totalUserIds.size() - iconUrlMaxSize;
            if (startSize > 0) {
                userIds.addAll(totalUserIds.stream().skip(startSize).limit(iconUrlMaxSize).collect(Collectors.toList()));
            }else {
                userIds.addAll(totalUserIds.stream().limit(iconUrlMaxSize).collect(Collectors.toList()));
            }
            Collections.reverse(userIds);

            //获取用户信息
            List<ClientLikePageResponse> userInfoList = DiscoverUtil.getUserInfoList(userService, userIds);
            //获取用户id
            Integer userId = AccessTokenUtil.getCurrentUserId();
            response.setLikeCount(totalUserIds.size());
            response.setLikeCountString(DiscoverUtil.getReadCountString(totalUserIds.size()));
            response.setLikePageList(userInfoList);
            if (userId != null) {
                response.setLikeStatus(totalUserIds.contains(userId));
            }
        }
        return response;
    }

    /**
     * 更新文章音频状态
     * @return
     */
    public CommonResponse<Boolean> batchUpdateLookType() {
        return clientArticleService.updateArticleLookType();
    }
}
