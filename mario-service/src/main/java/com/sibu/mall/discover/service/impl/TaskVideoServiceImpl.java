package com.mall.discover.service.impl;

import com.doubo.common.model.response.CommonResponse;
import com.mall.common.utils.SeqGenUtil;
import com.mall.discover.common.VodLook;
import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.common.contants.DubboConstants;
import com.mall.discover.request.article.AddOrEditArticleRequest;
import com.mall.discover.request.video.TaskVideo;
import com.mall.discover.service.TaskVideoService;
import com.mall.discover.service.impl.biz.ArticleServiceBiz;
import com.mall.search.request.ProductSearchRequest;
import com.mall.search.response.SearchProductBriefResponse;
import com.mall.search.service.ESProductSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service(
        group = "${dubbo.provider.group}",
        version = "${dubbo.provider.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class TaskVideoServiceImpl implements TaskVideoService {

    @Reference(consumer = DubboConstants.ELASTICSEARCH_CONSUMER)
    private ESProductSearchService esProductSearchService;
    @Autowired
    ArticleServiceBiz articleServiceBiz;
    @Override
    public CommonResponse<Boolean> saveTaskVideo(TaskVideo taskVideo,String webpUrl){
        // 添加 文章
        AddOrEditArticleRequest addOrEditArticleRequest = new AddOrEditArticleRequest();

        String title = Optional.ofNullable(taskVideo.getTitle()).orElseGet(()->"");
        addOrEditArticleRequest.setArticleTitle(title.length() > 30 ? title.substring(0,30):title);
        addOrEditArticleRequest.setArticleContent(Optional.ofNullable(taskVideo.getContent()).orElseGet(() ->""));
        List<String> productNos = new ArrayList<>();
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setProductIds(taskVideo.getProductIdList());
        productSearchRequest.setInitiationID(SeqGenUtil.getLogId());
        productSearchRequest.setProductClaszz(SearchProductBriefResponse.class);
        CommonResponse<List<SearchProductBriefResponse>> commonResponseProduct = esProductSearchService.searchByRequest(productSearchRequest);
        if(commonResponseProduct.isSuccess() && Objects.nonNull(commonResponseProduct.getResult())){
            commonResponseProduct.getResult().forEach(item ->{
                productNos.add(item.getProductNum());
            });
        }
        addOrEditArticleRequest.setProductNo(productNos);
        addOrEditArticleRequest.setArticleStatus(1);
        if(taskVideo.getPubType() == 0){
            // 商家来源
            addOrEditArticleRequest.setCreateUserType(2);
            addOrEditArticleRequest.setCurrentUserId(taskVideo.getSellerId().toString());
        }else if(taskVideo.getPubType() == 1){
            // 平台来源
            addOrEditArticleRequest.setCreateUserType(1);
        }else if(taskVideo.getPubType() == -1){
            // 个人用户
            addOrEditArticleRequest.setCreateUserType(3);
            addOrEditArticleRequest.setCurrentUserId(taskVideo.getMemberId().toString());
        }
        ArticleLook articleLook = new ArticleLook();
        VodLook vodLook = new VodLook();
        vodLook.setFileId(taskVideo.getVodID());
        vodLook.setCoverUrl(taskVideo.getFirstFrame());
        vodLook.setVodUrl(taskVideo.getVideoUrl());
        // 三秒动图
        vodLook.setWebpUrl(webpUrl);
        vodLook.setMemberId(taskVideo.getMemberId());
        List<VodLook> vodlist = new ArrayList<>();
        vodlist.add(vodLook);
        articleLook.setVodLookList(vodlist);
        addOrEditArticleRequest.setArticleLook(articleLook);
        // 添加关联商品，添加关联关系
        CommonResponse<Boolean> result = articleServiceBiz.addOrEditArticle(addOrEditArticleRequest);
        return result;
    }
}
