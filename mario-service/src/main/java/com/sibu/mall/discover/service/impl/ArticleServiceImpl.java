package com.mall.discover.service.impl;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.VodLook;
import com.mall.discover.request.article.*;
import com.mall.discover.response.article.ArticleInfoResponse;
import com.mall.discover.response.article.ArticleListResponse;
import com.mall.discover.response.article.DiscoverCountListResponse;
import com.mall.discover.service.ArticleService;
import com.mall.discover.service.impl.biz.ArticleServiceBiz;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author zhiming
 */
@Service(
        group = "${dubbo.provider.group}",
        version = "${dubbo.provider.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleServiceBiz articleServiceBiz;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResponse<Boolean> addOrEditArticle(AddOrEditArticleRequest request) {
        return articleServiceBiz.addOrEditArticle(request);
    }

    @Override
    public CommonResponse<CommonPageResult<ArticleListResponse>> listArticle(ListArticleRequest request) {
        return articleServiceBiz.listArticle(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<Boolean> deleteArticle(DeleteArticleRequest request) {
        return articleServiceBiz.deleteArticle(request);
    }

    @Override
    public CommonResponse<ArticleInfoResponse> getArticleInfo(Long articleId) {
        return articleServiceBiz.getArticleInfo(articleId);
    }

    @Override
    public CommonResponse<CommonPageResult<DiscoverCountListResponse>> listDiscoverCount(ListDiscoverCountRequest request) {
        return articleServiceBiz.listDiscoverCount(request);
    }

    @Override
    public CommonResponse<Boolean> editArticleSortClient(EditArticleSortClientRequest request) {
        return articleServiceBiz.editArticleSortClient(request);
    }

    @Override
    public CommonResponse<Boolean> updateReadShareCount() {
        return articleServiceBiz.updateReadShareCount();
    }

    @Override
    public CommonResponse<Boolean> publishArticle() {
        return articleServiceBiz.publishArticle();
    }

    @Override
    public CommonResponse<Boolean> publishVodLook() {
        return articleServiceBiz.publishVodLookGeneral();
    }

    @Override
    public CommonResponse<String> getSignature() {
        return articleServiceBiz.getSignature();
    }

    @Override
    public CommonResponse<VodLook> getVodLook(String fileId) {
        VodLook vodLook = articleServiceBiz.searchTencentCloudVodLookByField(fileId);
        if (vodLook != null) {
            return ResponseManage.success(vodLook);
        } else {
            return ResponseManage.fail("获取视频信息失败");
        }
    }

    @Override
    public CommonResponse<Boolean> updateArticleLook(VodLook vodLook, Long articleId) {
        boolean resp = articleServiceBiz.updateArticleLook(vodLook, articleId);
        if (!resp) {
            return ResponseManage.fail("更新失败");
        } else {
            return ResponseManage.success(Boolean.TRUE);
        }
    }

    @Override
    public CommonResponse<Boolean> updateArticleLookVod(VodLook vodLook, Long articleId) {
        boolean resp = articleServiceBiz.updateArticleLookVod(vodLook, articleId);
        if (!resp) {
            return ResponseManage.fail("更新失败");
        } else {
            return ResponseManage.success(Boolean.TRUE);
        }
    }

    @Override
    public CommonResponse<Boolean> editRelateArticleSort(EditRelateArticleSortRequest request) {
        return articleServiceBiz.editRelateArticleSort(request);
    }
}
