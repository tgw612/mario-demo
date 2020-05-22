package com.mall.discover.web.service.impl.biz;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.redis.util.RedisTemplateUtil;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.DiscoverCountEnum;
import com.mall.discover.request.client.ClientSubjectArticlePageRequest;
import com.mall.discover.request.client.ClientSubjectInfoRequest;
import com.mall.discover.request.client.ClientSubjectPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientSubjectInfoResponse;
import com.mall.discover.response.client.ClientSubjectPageResponse;
import com.mall.discover.service.ClientSubjectService;
import com.mall.discover.web.common.constants.DubboConstants;
import com.mall.discover.web.util.DiscoverUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;

/**
 * @author: huangzhong
 * @Date: 2019/11/4
 * @Description:
 */
@Service
@Slf4j
public class ClientSubjectBiz {
    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientSubjectService clientSubjectService;

    /**
     *  话题列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.SUBJECT_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientSubjectPageResponse>> subjectQueryPage;

    /**
     *  话题详情缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.SUBJECT_QUERY_INFO,
            cacheType = CacheType.REMOTE)
    private Cache<Long, ClientSubjectInfoResponse> subjectQueryInfo;

    /**
     *  话题内文章缓存,key:话题id-页码
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.SUBJECT_QUERY_ARTICLE_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<String, CommonPageResult<ClientArticleResponse>> subjectQueryArtilePage;

    /**
     *  文章点赞用户缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.LIKE_USER_ARTICLE_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Long, LinkedHashSet<Integer>> articleLikeUserListCache;


    /**
     * 话题列表
     */
    public CommonResponse<CommonPageResult<ClientSubjectPageResponse>> querySubjectPage(ClientSubjectPageRequest request){
        CommonPageResult<ClientSubjectPageResponse> result = new CommonPageResult<>();
        try {
            result = subjectQueryPage.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("获取缓存话题列表分页出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            return clientSubjectService.querySubjectPage(request, false);
        }
        return ResponseManage.success(result);
    }

    /**
     * 话题详情
     */
    public CommonResponse<ClientSubjectInfoResponse> querySubjectInfo(ClientSubjectInfoRequest request){
        ClientSubjectInfoResponse infoResponse = new ClientSubjectInfoResponse();
        try {
            //自增阅读数
            RedisTemplateUtil.incrLong(DiscoverCountEnum.SUBJECT_READ_COUNT.getCode() + request.getSubjectId());
            //获取返回值
            infoResponse = subjectQueryInfo.get(request.getSubjectId());
        } catch (Exception e) {
            log.error("获取缓存话题详情出错,错误信息：{}", e.getMessage());
        }

        if (infoResponse == null) {
            CommonResponse<ClientSubjectInfoResponse> result = clientSubjectService.querySubjectInfo(request, false);
            if (!result.isSuccess()) {
                log.error("DB中获取话题详情出错,错误信息：{}", result);
                return ResponseManage.fail("该话题详情不存在");
            } else {
                return result;
            }
        }
        return ResponseManage.success(infoResponse);
    }

    /**
     * 话题内文章列表
     */
    public CommonResponse<CommonPageResult<ClientArticleResponse>> querySubjectArticlePage(ClientSubjectArticlePageRequest request){
        CommonPageResult<ClientArticleResponse> result = new CommonPageResult<>();
        try {
            result = subjectQueryArtilePage.get(request.getSubjectId() + "-" + request.getCurrentPage());
        } catch (Exception e) {
            log.error("获取缓存话题内文章列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            CommonResponse<CommonPageResult<ClientArticleResponse>> response = clientSubjectService.querySubjectArticlePage(request);
            if (!response.isSuccess()) {
                log.error("DB中获取话题详情内列表出错,错误信息：{}", response);
                return ResponseManage.fail("文章列表不存在");
            }
            result = response.getResult();
        }
        DiscoverUtil.getUserLikePage(articleLikeUserListCache, result.getData());
        return ResponseManage.success(result);
    }
}
