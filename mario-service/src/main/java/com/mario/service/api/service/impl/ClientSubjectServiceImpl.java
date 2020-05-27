package com.mario.service.api.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.PictureLook;
import com.mall.discover.common.VodLook;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.persistence.bo.SubjectArticleBO;
import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.request.client.ClientSubjectArticlePageRequest;
import com.mall.discover.request.client.ClientSubjectInfoRequest;
import com.mall.discover.request.client.ClientSubjectPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientSubjectInfoResponse;
import com.mall.discover.response.client.ClientSubjectPageResponse;
import com.mall.discover.response.client.ClientSubjectResponse;
import com.mall.discover.service.ClientSubjectService;
import com.mario.service.api.config.properties.DiscoverProperties;
import com.mario.service.api.service.impl.biz.ClientSubjectBiz;
import com.mario.service.api.util.DiscoverUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * @author: huangzhong
 * @Date: 2019/10/8
 * @Description:
 */
@Slf4j
@Service(
    group = "${dubbo.provider.group}",
    version = "${dubbo.provider.version}",
    application = "${dubbo.application.id}",
    protocol = "${dubbo.protocol.id}",
    registry = "${dubbo.registry.id}"
)
public class ClientSubjectServiceImpl implements ClientSubjectService {

  @Autowired
  private ClientSubjectBiz clientSubjectBiz;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DiscoverProperties discoverProperties;

  /**
   * 话题列表缓存
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.SUBJECT_QUERY_PAGE,
      cacheType = CacheType.REMOTE)
  private Cache<Integer, CommonPageResult<ClientSubjectPageResponse>> subjectQueryPage;

  /**
   * 话题详情缓存
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.SUBJECT_QUERY_INFO,
      cacheType = CacheType.REMOTE)
  private Cache<Long, ClientSubjectInfoResponse> subjectQueryInfo;

  /**
   * 话题内文章缓存,key:话题id-页码
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.SUBJECT_QUERY_ARTICLE_PAGE,
      cacheType = CacheType.REMOTE)
  private Cache<String, CommonPageResult<ClientArticleResponse>> subjectQueryArtilePage;

  /**
   * 话题内文章缓存,key:话题id-页码,兼容老版本
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.SUBJECT_QUERY_ARTICLE_PAGE_OLD,
      cacheType = CacheType.REMOTE)
  private Cache<String, CommonPageResult<ClientArticleResponse>> subjectQueryArtilePageOld;

  @Override
  public CommonResponse<CommonPageResult<ClientSubjectPageResponse>> querySubjectPage(
      ClientSubjectPageRequest request, boolean cache) {
    CommonPageResult<ClientSubjectPageResponse> result = new CommonPageResult<>();
    Integer totalCount = clientSubjectBiz.querySubjectPageCount();

    //封装返回值
    List<ClientSubjectPageResponse> responseList = new ArrayList<>();
    if (DiscoverUtil.checkTotalCount(totalCount)) {
      List<SubjectBO> subjectList = clientSubjectBiz.querySubjectPage(request);
      for (SubjectBO subject : subjectList) {
        ClientSubjectPageResponse response = DiscoverUtil
            .convertResponse(subject, ClientSubjectPageResponse.class);
        //阅读数转换
        String readCountString = DiscoverUtil.getReadCountString(response.getReadCount());
        response.setViewCountString(readCountString);
        //获取图片URL
        response.setSubjectPictureUrl(DiscoverUtil.getSubjectPictureUrl(subject));
        responseList.add(response);
      }

      //忽略总页数、总条数
      result.setData(responseList);
      result.setTotalCount(totalCount);
      result
          .setTotalPage(PageUtils.getPageCount(discoverProperties.getClientPageSize(), totalCount));

      //缓存有效时间
      long clientExpireTime = discoverProperties.getClientExpireTime();
      //缓存话题列表
      subjectQueryPage.put(request.getCurrentPage(), result, clientExpireTime, TimeUnit.SECONDS);

      //缓存话题详情
      int clientCacheInfoMaxPage = discoverProperties.getClientCacheInfoMaxPage();
      if (cache && request.getCurrentPage() < clientCacheInfoMaxPage) {
        for (ClientSubjectPageResponse response : responseList) {
          ClientSubjectInfoResponse infoResponse = DiscoverUtil
              .convertResponse(response, ClientSubjectInfoResponse.class);
          subjectQueryInfo
              .put(infoResponse.getSubjectId(), infoResponse, clientExpireTime, TimeUnit.SECONDS);
          //缓存话题内文章
          cacheSubjectArticlePage(infoResponse.getSubjectId());
        }
      }
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<ClientSubjectInfoResponse> querySubjectInfo(
      ClientSubjectInfoRequest request, boolean cache) {
    SubjectBO subject = clientSubjectBiz.querySubjectInfo(request);
    ClientSubjectInfoResponse infoResponse = null;
    if (subject != null) {
      infoResponse = DiscoverUtil.convertResponse(subject, ClientSubjectInfoResponse.class);
      //阅读数转换
      String readCountString = DiscoverUtil.getReadCountString(infoResponse.getReadCount());
      infoResponse.setViewCountString(readCountString);
      //获取图片URL
      infoResponse.setSubjectPictureUrl(DiscoverUtil.getSubjectPictureUrl(subject));

      //缓存有效时间
      long clientExpireTime = discoverProperties.getClientExpireTime();
      //缓存话题详情
      subjectQueryInfo
          .put(infoResponse.getSubjectId(), infoResponse, clientExpireTime, TimeUnit.SECONDS);

      //缓存话题内文章
      if (cache) {
        cacheSubjectArticlePage(infoResponse.getSubjectId());
      }
    }
    return ResponseManage.success(infoResponse);
  }

  @Override
  public CommonResponse<CommonPageResult<ClientArticleResponse>> querySubjectArticlePage(
      ClientSubjectArticlePageRequest request) {
    CommonPageResult<ClientArticleResponse> result = new CommonPageResult<>();

    //获取话题名称(只有一个话题)
    SubjectBO subject = clientSubjectBiz.querySubjectName(request);
    ClientSubjectResponse clientSubjectResponse = new ClientSubjectResponse();
    clientSubjectResponse.setSubjectId(subject.getSubjectId());
    clientSubjectResponse.setSubjectName(subject.getSubjectName());
    List<ClientSubjectResponse> clientSubjectResponses = Collections
        .singletonList(clientSubjectResponse);

    //封装返回值
    List<ClientArticleResponse> responses = new ArrayList<>();
    Integer totalCount = clientSubjectBiz.querySubjectArticlePageCount(request);
    if (DiscoverUtil.checkTotalCount(totalCount)) {
      List<SubjectArticleBO> subjectArticles = clientSubjectBiz.querySubjectArticlePage(request);
      for (SubjectArticleBO subjectArticle : subjectArticles) {
        ClientArticleResponse response = DiscoverUtil
            .convertResponse(subjectArticle, ClientArticleResponse.class);
        //阅读数转换
        String readCountString = DiscoverUtil.getReadCountString(response.getReadCount());
        response.setViewCountString(readCountString);
        //获取第一张图片信息
        PictureLook pictureLook = DiscoverUtil
            .getArticleFirstPicture(subjectArticle.getArticleLook());
        response.setPictureLook(pictureLook);
        VodLook vodLook = DiscoverUtil.getVodLook(subjectArticle.getArticleLook());
        response.setVodLook(vodLook);
        response.setSubjectList(clientSubjectResponses);
        responses.add(response);
      }
      //忽略总页数、总条数
      result.setData(responses);
      result.setTotalCount(totalCount);
      result
          .setTotalPage(PageUtils.getPageCount(discoverProperties.getClientPageSize(), totalCount));
      //缓存话题内文章
      if (!ObjectUtils.isEmpty(subjectArticles)) {
        subjectQueryArtilePage.put(request.getSubjectId() + "-" + request.getCurrentPage(), result,
            discoverProperties.getClientExpireTime(), TimeUnit.SECONDS);
      }
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<CommonPageResult<ClientArticleResponse>> querySubjectArticlePageOld(
      ClientSubjectArticlePageRequest request, boolean cache) {
    //兼容老版本 批量缓存
    if (cache) {
      List<Long> subjectIds = clientSubjectBiz.queryAllSubjectId();
      for (Long subjectId : subjectIds) {
        ClientSubjectArticlePageRequest tempRequest = new ClientSubjectArticlePageRequest();
        tempRequest.setSubjectId(subjectId);
        getClientArticleResponsePageOld(tempRequest);
      }
      return ResponseManage.success(new CommonPageResult<>());
    } else {
      CommonPageResult<ClientArticleResponse> result = getClientArticleResponsePageOld(request);
      return ResponseManage.success(result);
    }

  }

  /**
   * 分页缓存话题内文章
   */
  private void cacheSubjectArticlePage(Long subjectId) {
    //缓存页数
    int clientArticleMaxPage = discoverProperties.getClientArticleMaxPage();
    for (int i = 1; i <= clientArticleMaxPage; i++) {
      ClientSubjectArticlePageRequest request = new ClientSubjectArticlePageRequest();
      request.setSubjectId(subjectId);
      request.setCurrentPage(i);
      //缓存文章
      querySubjectArticlePage(request);
    }
  }

  /**
   * 缓存话题内文章，兼容老版本
   *
   * @param request
   * @return
   */
  private CommonPageResult<ClientArticleResponse> getClientArticleResponsePageOld(
      ClientSubjectArticlePageRequest request) {
    CommonPageResult<ClientArticleResponse> result = new CommonPageResult<>();

    //获取话题名称(只有一个话题)
    SubjectBO subject = clientSubjectBiz.querySubjectName(request);
    ClientSubjectResponse clientSubjectResponse = new ClientSubjectResponse();
    clientSubjectResponse.setSubjectId(subject.getSubjectId());
    clientSubjectResponse.setSubjectName(subject.getSubjectName());
    List<ClientSubjectResponse> clientSubjectResponses = Collections
        .singletonList(clientSubjectResponse);

    //封装返回值
    List<ClientArticleResponse> responses = new ArrayList<>();
    Integer totalCount = clientSubjectBiz.querySubjectArticlePageCount(request);
    if (DiscoverUtil.checkTotalCount(totalCount)) {
      List<SubjectArticleBO> subjectArticles = clientSubjectBiz.querySubjectArticlePage(request);
      for (SubjectArticleBO subjectArticle : subjectArticles) {
        ClientArticleResponse response = DiscoverUtil
            .convertResponse(subjectArticle, ClientArticleResponse.class);
        //阅读数转换
        String readCountString = DiscoverUtil.getReadCountString(response.getReadCount());
        response.setViewCountString(readCountString);
        //获取第一张图片信息
        PictureLook pictureLook = DiscoverUtil
            .getArticleFirstPictureOld(subjectArticle.getArticleLook());
        if (pictureLook == null) {
          continue;
        }
        response.setPictureLook(pictureLook);
        response.setSubjectList(clientSubjectResponses);
        responses.add(response);

      }
      //忽略总页数、总条数
      result.setData(responses);
      result.setTotalCount(totalCount);
      result
          .setTotalPage(PageUtils.getPageCount(discoverProperties.getClientPageSize(), totalCount));
      //缓存话题内文章
      if (!ObjectUtils.isEmpty(subjectArticles)) {
        subjectQueryArtilePageOld
            .put(request.getSubjectId() + "-" + request.getCurrentPage(), result,
                discoverProperties.getClientExpireTime(), TimeUnit.SECONDS);
      }
    }
    return result;
  }
}
