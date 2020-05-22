package com.mall.discover.service;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.client.ClientArticleInfoRequest;
import com.mall.discover.request.client.ClientArticlePageRequest;
import com.mall.discover.request.client.ClientUpdateCountRequest;
import com.mall.discover.response.client.ClientArticleInfoResponse;
import com.mall.discover.response.client.ClientDiscoverResponse;

/**
 * @author: huangzhong
 * @Date: 2019/10/7
 * @Description: 客户端文章服务
 */
public interface ClientArticleService {

    /**
     * 文章列表
     * @param cache 定时任务使用，true为缓存较多数据
     * @return
     */
    CommonResponse<CommonPageResult<ClientDiscoverResponse>> queryArticlePage(ClientArticlePageRequest request, boolean cache);

    /**
     * 文章详情
     * @param cache 定时任务使用，true为缓存较多数据
     * @return
     */
    CommonResponse<ClientArticleInfoResponse> queryArticleInfo(ClientArticleInfoRequest request, boolean cache);


    /**
     * 文章列表
     * @param cache 定时任务使用，维护旧版本
     * @return
     */
    CommonResponse<CommonPageResult<ClientDiscoverResponse>> queryArticlePageOld(ClientArticlePageRequest request, boolean cache);

    /**
     * 文章详情
     * @param cache 定时任务使用，维护旧版本
     * @return
     */
    CommonResponse<ClientArticleInfoResponse> queryArticleInfoOld(ClientArticleInfoRequest request, boolean cache);

    /**
     * 更新look状态，内部接口（仅执行一次）
     */
    CommonResponse<Boolean> updateArticleLookType();
}
