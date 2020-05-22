package com.mall.discover.service;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.client.ClientProductArticlePageRequest;
import com.mall.discover.request.client.ClientProductInfoRequest;
import com.mall.discover.request.client.ClientProductPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientProductInfoResponse;
import com.mall.discover.response.client.ClientProductPageResponse;

/**
 * @author: huangzhong
 * @Date: 2019/10/7
 * @Description: 客户端商品服务
 */
public interface ClientProductService {

    /**
     * 爆款商品列表
     * @param cache 定时任务使用，true为缓存较多数据
     * @return
     */
    CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHotProductPage(ClientProductPageRequest request, boolean cache);

    /**
     * 高佣金商品列表
     * @param cache 定时任务使用，true为缓存较多数据
     * @return
     */
    CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHighProductPage(ClientProductPageRequest request, boolean cache);

    /**
     * 商品详情
     * @param cache 定时任务使用，true为缓存较多数据
     * @return
     */
    CommonResponse<ClientProductInfoResponse> queryProductInfo(ClientProductInfoRequest request, boolean cache);

    /**
     * 商品内文章列表
     */
    CommonResponse<CommonPageResult<ClientArticleResponse>> queryProductArticlePage(ClientProductArticlePageRequest request);

    /**
     * 通过商品编号获取商品id（兼容）
     * @param productNo
     * @return
     */
    CommonResponse<Long> queryProductNoIdRelation(String productNo);

    /**
     * 商品列表
     * @param cache 定时任务使用，true为缓存较多数据
     * @return
     */
    CommonResponse<CommonPageResult<ClientProductPageResponse>> queryProductPage(ClientProductPageRequest request, boolean cache);
}
