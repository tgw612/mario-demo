package com.mall.discover.service;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.common.VodLook;
import com.mall.discover.request.article.*;
import com.mall.discover.response.article.ArticleInfoResponse;
import com.mall.discover.response.article.ArticleListResponse;
import com.mall.discover.response.article.DiscoverCountListResponse;

public interface ArticleService {
    /**
     * 新增or编辑文章
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> addOrEditArticle(AddOrEditArticleRequest request);

    /**
     * 文章列表
     *
     * @param request
     * @return
     */
    CommonResponse<CommonPageResult<ArticleListResponse>> listArticle(ListArticleRequest request);

    /**
     * 删除文章
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> deleteArticle(DeleteArticleRequest request);

    /**
     * 文章详情
     *
     * @param articleId
     * @return
     */
    CommonResponse<ArticleInfoResponse> getArticleInfo(Long articleId);


    /**
     * 分页查询count
     *
     * @param request
     * @return
     */
    CommonResponse<CommonPageResult<DiscoverCountListResponse>> listDiscoverCount(ListDiscoverCountRequest request);

    /**
     * 修改sort_client排序值
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> editArticleSortClient(EditArticleSortClientRequest request);

    /**
     * 定时任务调用更新ReadShareCount
     *
     * @return
     */
    CommonResponse<Boolean> updateReadShareCount();


    /**
     * 定时发布文章
     *
     * @return
     */
    CommonResponse<Boolean> publishArticle();

    /**
     * 发布任务流完成的短视频
     *
     * @return
     */
    CommonResponse<Boolean> publishVodLook();


    /**
     * 获取上传视频签名
     */
    CommonResponse<String> getSignature();

    /**
     * 获取vod信息
     */
    CommonResponse<VodLook> getVodLook(String fileId);

    /**
     * 发消息
     */
    CommonResponse<Boolean> updateArticleLook(VodLook vodLook, Long articleId);

    /**
     * 更新文章视频
     */
    CommonResponse<Boolean> updateArticleLookVod(VodLook vodLook, Long articleId);

    /**
     * 编辑话题/商品内文章列表排序
     */
    CommonResponse<Boolean> editRelateArticleSort(EditRelateArticleSortRequest request);

}
