package com.mario.service.api.service.impl;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.json.util.JsonUtil;
import com.mall.discover.common.VodLook;
import com.mall.discover.request.article.DeleteArticleRequest;
import com.mall.discover.request.article.ListArticleRequest;
import com.mall.discover.request.article.ListDiscoverCountRequest;
import com.mall.discover.response.article.ArticleListResponse;
import com.mall.discover.response.article.DiscoverCountListResponse;
import com.mall.discover.service.ArticleService;
import com.mario.mq.VodHandlerMqProducer;
import com.mario.mq.request.ArticleVodUpdateRequest;
import com.mario.service.api.config.QcloudConfigProperties;
import com.mario.service.api.service.base.BaseSpringTest;
import com.mario.service.api.service.impl.biz.ArticleServiceBiz;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.PullEventsRequest;
import com.tencentcloudapi.vod.v20180717.models.PullEventsResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ArticleServiceImplTest extends BaseSpringTest {

  @Autowired
  ArticleService articleService;
  @Autowired
  QcloudConfigProperties qcloudConfigProperties;
  @Autowired
  VodClient vodClient;
  @Autowired
  ArticleServiceBiz articleServiceBiz;
  @Autowired
  private VodHandlerMqProducer vodHandlerMqProducer;

  @Test
  public void addOrEditArticle() {
    String json = "{\"content\":null,\"coverUrl\":\"http://1400281946.vod2.myqcloud.com/d440b5edvodtranscq1400281946/2b7235015285890795850988174/coverBySnapshot/1573788536_1086332534.100_0.jpg\",\"fileId\":\"5285890795850988174\",\"height\":1920,\"memberId\":null,\"vodUrl\":\"http://1400281946.vod2.myqcloud.com/d440b5edvodtranscq1400281946/2b7235015285890795850988174/v.f48142.mp4\",\"webpUrl\":\"http://1400281946.vod2.myqcloud.com/d440b5edvodtranscq1400281946/2b7235015285890795850988174/animatedGraphics/1573788536_4183996497.100_0.webp\",\"whRatio\":0.56,\"width\":1080}";
    VodLook vodLook = JsonUtil.parseObject(json, VodLook.class);
    CommonResponse<Boolean> response = articleService.updateArticleLook(vodLook, 214L);
    System.out.println(response.getResult());
  }

  @Test
  public void listArticle() {
    ListArticleRequest request = new ListArticleRequest() {
      {
        setCurrentPage(1);
        setPageSize(10);
      }
    };
    CommonResponse<CommonPageResult<ArticleListResponse>> resp = articleService
        .listArticle(request);
    System.out.println(JsonUtil.toJSON(resp));
  }

  @Test
  public void deleteArticle() {
    articleService.deleteArticle(new DeleteArticleRequest() {{
      setArticleId(1L);
    }});
  }

  @Test
  public void getArticleInfo() {
  }

  @Test
  public void listDiscoverCount() {
    CommonResponse<CommonPageResult<DiscoverCountListResponse>> response = articleService.
        listDiscoverCount(new ListDiscoverCountRequest() {{
          setBiztype(1);
        }});
    JsonUtil.toJson(response);
  }

  @Test
  public void publishArticle() {
    articleService.publishArticle();
  }

  @Test
  public void getSignature() {
    CommonResponse<String> result = articleService.getSignature();
    System.out.println(result);
  }

  @Test
  public void PullEvents() {
//        Future<PullEventsResponse> future = uploadVodExecutor.submit(() -> {
    //拉取视频相关
    PullEventsRequest pullEventsRequest = new PullEventsRequest();
    pullEventsRequest.setSubAppId(Long.valueOf(qcloudConfigProperties.getVodSubAppId()));
    PullEventsResponse response = null;
    try {
      response = vodClient.PullEvents(pullEventsRequest);
    } catch (TencentCloudSDKException e) {
      e.printStackTrace();
    }
    System.out.println(response);
//        });
  }

  @Test
  public void test() {
    VodLook look = new VodLook();
    look.setFileId("100");

    ArticleVodUpdateRequest request = new ArticleVodUpdateRequest();
    request.setArticleId(100L);
    request.setVodLook(look);
    this.vodHandlerMqProducer.sendVodHandlerTask(request);
  }

  @Test
  public void publishVodLook() {
    articleService.publishVodLook();
  }

  @Test
  public void confirmEvents() {
    articleServiceBiz.confirmEvents();
  }

  @Test
  public void updateArticleLook() {
    String fileId = "5285890795431438010";
  }
}