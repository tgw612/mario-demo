package com.mario.service.api.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.common.manage.ResponseManage;
import com.mall.darwin.query.ApplicationLaunchQuery;
import com.mall.darwin.response.LaunchResponse;
import com.mall.darwin.service.ApplicationLaunchService;
import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.contants.DubboConstants;
import com.mall.discover.common.enums.ArticleLookTypeEnum;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.persistence.bo.ArticleBO;
import com.mall.discover.persistence.bo.ProductBO;
import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.request.client.ClientArticleInfoRequest;
import com.mall.discover.request.client.ClientArticlePageRequest;
import com.mall.discover.request.client.ClientProductInfoRequest;
import com.mall.discover.request.client.ClientSubjectInfoRequest;
import com.mall.discover.response.client.*;
import com.mall.discover.service.ClientArticleService;
import com.mario.service.api.config.properties.DiscoverProperties;
import com.mario.service.api.service.impl.biz.ClientArticleBiz;
import com.mario.service.api.service.impl.biz.ClientProductBiz;
import com.mario.service.api.service.impl.biz.ClientSubjectBiz;
import com.mario.service.api.util.DiscoverUtil;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
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
public class ClientArticleServiceImpl implements ClientArticleService {

  @Autowired
  private ClientArticleBiz clientArticleBiz;

  @Autowired
  private ClientProductBiz clientProductBiz;

  @Autowired
  private ClientSubjectBiz clientSubjectBiz;

  @Autowired
  private DiscoverProperties discoverProperties;

  @Reference(consumer = DubboConstants.DARWIN_CONSUMER)
  private ApplicationLaunchService applicationLaunchService;

  /**
   * 话题详情缓存
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.SUBJECT_QUERY_INFO,
      cacheType = CacheType.REMOTE)
  private Cache<Long, ClientSubjectInfoResponse> subjectQueryInfo;

  /**
   * 文章列表缓存
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.ARTICLE_QUERY_PAGE,
      cacheType = CacheType.REMOTE)
  private Cache<Integer, CommonPageResult<ClientDiscoverResponse>> articleQueryPage;

  /**
   * 文章详情缓存
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.ARTICLE_QUERY_INFO,
      cacheType = CacheType.REMOTE)
  private Cache<Long, ClientArticleInfoResponse> articleQueryInfo;

  /**
   * 文章列表缓存
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.ARTICLE_QUERY_PAGE_OLD,
      cacheType = CacheType.REMOTE)
  private Cache<Integer, CommonPageResult<ClientDiscoverResponse>> articleQueryPageOld;

  /**
   * 文章详情缓存
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.ARTICLE_QUERY_INFO_OLD,
      cacheType = CacheType.REMOTE)
  private Cache<Long, ClientArticleInfoResponse> articleQueryInfoOld;

  @Override
  public CommonResponse<CommonPageResult<ClientDiscoverResponse>> queryArticlePage(
      ClientArticlePageRequest request, boolean cache) {
    CommonPageResult<ClientDiscoverResponse> result = new CommonPageResult<>();
    List<ClientDiscoverResponse> responsesList = new ArrayList<>();

    //第一页数据才有话题
    List<ClientSubjectResponse> subjectResponseList = new ArrayList<>();
    List<ClientSubjectInfoResponse> subjectInfoResponseList = new ArrayList<>();
    if (request.getCurrentPage() == 1) {
      //从T台获取话题数据
      subjectResponseList = getDataFromDarwin(subjectInfoResponseList);
    }

    //从后台获取文章数据
    List<ClientArticleResponse> articleResponseList = new ArrayList<>();
    List<ArticleBO> articleList = clientArticleBiz.queryArticlePage(request, null);
    articleList.forEach(
        articleBO -> articleResponseList.add(DiscoverUtil.getClientArticleResponse(articleBO)));

    //获取总页数
    Integer totalCount = clientArticleBiz.queryArticlePageCount(null);

    //组合排序
    if (subjectResponseList.size() > 0) {
      getArticleResult(responsesList, subjectResponseList, articleResponseList);
    } else {
      responsesList.addAll(articleResponseList);
    }
    result.setData(responsesList);
    //话题数量不进入页码计算
    result.setTotalCount(totalCount + subjectResponseList.size());
    result.setTotalPage(PageUtils.getPageCount(discoverProperties.getClientPageSize(), totalCount));

    //缓存有效时间
    long clientExpireTime = discoverProperties.getClientExpireTime();
    //缓存文章列表
    if (responsesList.size() > 0) {
      articleQueryPage.put(request.getCurrentPage(), result, clientExpireTime, TimeUnit.SECONDS);
    }

    //缓存(详情暂时缓存5页)
    int clientCacheInfoMaxPage = discoverProperties.getClientCacheInfoMaxPage();
    if (cache && responsesList.size() > 0 && request.getCurrentPage() < clientCacheInfoMaxPage) {
      //缓存详情
      for (ClientDiscoverResponse response : responsesList) {
        //缓存文章详情
        if (response instanceof ClientArticleResponse) {
          //文章详情
          ClientArticleInfoResponse infoResponse = DiscoverUtil
              .convertResponse(response, ClientArticleInfoResponse.class);
          //图片音频集合
          ArticleBO article = articleList.stream()
              .filter(a -> a.getArticleId().equals(infoResponse.getArticleId()))
              .findFirst()
              .orElseGet(ArticleBO::new);

          //缓存文章详情
          getArticleInfo(infoResponse, article);
          articleQueryInfo
              .put(infoResponse.getArticleId(), infoResponse, clientExpireTime, TimeUnit.SECONDS);
        }

        //缓存资源位相关话题
        if (response instanceof ClientSubjectResponse) {
          ClientSubjectResponse clientSubjectResponse = (ClientSubjectResponse) response;
          ClientSubjectInfoResponse infoResponse = subjectInfoResponseList.stream()
              .filter(info -> clientSubjectResponse.getSubjectId().equals(info.getSubjectId()))
              .findFirst()
              .orElseGet(ClientSubjectInfoResponse::new);
          subjectQueryInfo.put(clientSubjectResponse.getSubjectId(), infoResponse, clientExpireTime,
              TimeUnit.SECONDS);
        }
      }
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<ClientArticleInfoResponse> queryArticleInfo(
      ClientArticleInfoRequest request, boolean cache) {
    ClientArticleInfoResponse result = null;
    ArticleBO articleBO = clientArticleBiz.queryArticleInfo(request);
    if (articleBO != null) {
      result = new ClientArticleInfoResponse();
      result.setArticleId(articleBO.getArticleId());
      result.setArticleTitle(articleBO.getArticleTitle());
      result.setArticleContent(articleBO.getArticleContent());
      result.setSubjectList(DiscoverUtil.getClientSubjectResponseList(articleBO.getArticleId()));
      getArticleInfo(result, articleBO);

      //缓存
      articleQueryInfo.put(result.getArticleId(), result, discoverProperties.getClientExpireTime(),
          TimeUnit.SECONDS);
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<CommonPageResult<ClientDiscoverResponse>> queryArticlePageOld(
      ClientArticlePageRequest request, boolean cache) {
    //全部缓存
    CommonPageResult<ClientDiscoverResponse> result = new CommonPageResult<>();
    List<ClientDiscoverResponse> responsesList = new ArrayList<>();

    //第一页数据才有话题
    List<ClientSubjectResponse> subjectResponseList = new ArrayList<>();
    List<ClientSubjectInfoResponse> subjectInfoResponseList = new ArrayList<>();
    if (request.getCurrentPage() == 1) {
      //从T台获取话题数据
      subjectResponseList = getDataFromDarwin(subjectInfoResponseList);
    }

    //从后台获取文章数据，筛选look类型
    List<Integer> articleLookTypeList = new ArrayList<>();
    articleLookTypeList.add(ArticleLookTypeEnum.ONLY_PICTURE.getCode());
    articleLookTypeList.add(ArticleLookTypeEnum.PICTURE_VIDEO.getCode());
    //获取总页数
    Integer totalCount = clientArticleBiz.queryArticlePageCount(articleLookTypeList);
    List<ClientArticleResponse> totalArticleList = new ArrayList<>();
    //筛选数据
    int clientPageSize = discoverProperties.getClientPageSize();
    List<ArticleBO> articleList = clientArticleBiz.queryArticlePage(request, articleLookTypeList);
    for (ArticleBO articleBO : articleList) {
      ClientArticleResponse clientArticleResponse = DiscoverUtil
          .getClientArticleResponseOld(articleBO);
      //防止错误数据
      if (!ObjectUtils.isEmpty(clientArticleResponse)) {
        totalArticleList.add(clientArticleResponse);
      }
    }

    //组合排序
    if (subjectResponseList.size() > 0) {
      getArticleResult(responsesList, subjectResponseList, totalArticleList);
    } else {
      responsesList.addAll(totalArticleList);
    }

    //封装返回值
    result.setData(responsesList);
    //话题数量不进入页码计算
    result.setTotalCount(totalCount);
    result.setTotalPage(PageUtils.getPageCount(clientPageSize, totalCount));

    //缓存有效时间
    long clientExpireTime = discoverProperties.getClientExpireTime();
    //缓存文章列表
    if (responsesList.size() > 0) {
      articleQueryPageOld.put(request.getCurrentPage(), result, clientExpireTime, TimeUnit.SECONDS);
    }

    //缓存详情
    if (cache && request.getCurrentPage() < discoverProperties.getClientCacheInfoMaxPage()) {
      //缓存详情
      for (ClientDiscoverResponse response : responsesList) {
        //缓存文章详情
        if (response instanceof ClientArticleResponse) {
          //文章详情
          ClientArticleInfoResponse infoResponse = DiscoverUtil
              .convertResponse(response, ClientArticleInfoResponse.class);
          //图片音频集合
          ArticleBO article = articleList.stream()
              .filter(a -> a.getArticleId().equals(infoResponse.getArticleId()))
              .findFirst()
              .orElseGet(ArticleBO::new);

          //缓存文章详情
          getArticleInfoOld(infoResponse, article);
          articleQueryInfoOld
              .put(infoResponse.getArticleId(), infoResponse, clientExpireTime, TimeUnit.SECONDS);
        }
      }
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<ClientArticleInfoResponse> queryArticleInfoOld(
      ClientArticleInfoRequest request, boolean cache) {
    ClientArticleInfoResponse result = null;
    ArticleBO articleBO = clientArticleBiz.queryArticleInfo(request);
    if (articleBO != null) {
      result = new ClientArticleInfoResponse();
      result.setArticleId(articleBO.getArticleId());
      result.setArticleTitle(articleBO.getArticleTitle());
      result.setArticleContent(articleBO.getArticleContent());
      result.setSubjectList(DiscoverUtil.getClientSubjectResponseList(articleBO.getArticleId()));
      getArticleInfoOld(result, articleBO);

      //缓存
      articleQueryInfoOld
          .put(result.getArticleId(), result, discoverProperties.getClientExpireTime(),
              TimeUnit.SECONDS);
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<Boolean> updateArticleLookType() {
    clientArticleBiz.updateArticleLookType();
    return ResponseManage.success(true);
  }

  /**
   * 从T台获取数据
   *
   * @param subjectInfoResponseList 缓存详情
   */
  private List<ClientSubjectResponse> getDataFromDarwin(
      List<ClientSubjectInfoResponse> subjectInfoResponseList) {
    //返回数据
    List<ClientSubjectResponse> subjectResponseList = new ArrayList<>();
    //T台资源
    ApplicationLaunchQuery applicationLaunchQuery = new ApplicationLaunchQuery();
    applicationLaunchQuery.setDefinitionId(discoverProperties.getDefinitionId());
    try {
      CommonResponse<LaunchResponse> launchResponseCommonResponse = applicationLaunchService
          .applicationLaunch(applicationLaunchQuery);
      if (launchResponseCommonResponse.isSuccess()) {
        List<Map<String, Object>> value = launchResponseCommonResponse.getResult().getValue();
        if (value != null && value.size() > 0) {
          value.forEach(map -> {
            Long subjectId = Long.valueOf((String) map.get(discoverProperties.getSubjectIdField()));
            Integer resourceSort = Integer
                .valueOf((String) map.get(discoverProperties.getResourceSortField()));
            ClientSubjectResponse subjectResponse = getSubjectResponse(subjectId,
                subjectInfoResponseList);
            if (subjectResponse.getSubjectId() != null) {
              subjectResponse.setResourceSort(resourceSort);
              subjectResponseList.add(subjectResponse);
            }
          });
        }
      }
    } catch (Exception e) {
      log.error("T台获取数据出错,报错信息 {}", e.getMessage());
    }

    return subjectResponseList;
  }

  /**
   * 获取话题详情
   *
   * @return
   */
  private ClientSubjectResponse getSubjectResponse(Long subjectId,
      List<ClientSubjectInfoResponse> subjectInfoResponseList) {
    ClientSubjectInfoRequest subjectInfoRequest = new ClientSubjectInfoRequest();
    subjectInfoRequest.setSubjectId(subjectId);
    SubjectBO subject = clientSubjectBiz.querySubjectInfo(subjectInfoRequest);
    ClientSubjectInfoResponse subjectInfoResponse = DiscoverUtil
        .convertResponse(subject, ClientSubjectInfoResponse.class);
    if (subjectInfoResponse.getSubjectId() != null && subjectInfoResponse.getSubjectId() > 0) {
      //阅读数转换
      String readCountString = DiscoverUtil.getReadCountString(subjectInfoResponse.getReadCount());
      subjectInfoResponse.setViewCountString(readCountString);
      //获取图片URL
      subjectInfoResponse.setSubjectPictureUrl(DiscoverUtil.getSubjectPictureUrl(subject));
      subjectInfoResponseList.add(subjectInfoResponse);
    }
    return DiscoverUtil.convertResponse(subjectInfoResponse, ClientSubjectResponse.class);
  }

  /**
   * 对话题和文章组合排序
   *
   * @param responsesList
   * @param subjectResponseList
   * @param articleResponseList
   */
  private void getArticleResult(List<ClientDiscoverResponse> responsesList,
      List<ClientSubjectResponse> subjectResponseList,
      List<ClientArticleResponse> articleResponseList) {
    int resourceSortCount = discoverProperties.getResourceSortCount();
    int count = 0;
    for (int i = 0; i < resourceSortCount; i++) {
      boolean flag = true;
      for (ClientSubjectResponse subjectResponse : subjectResponseList) {
        if (subjectResponse.getResourceSort() == i) {
          responsesList.add(subjectResponse);
          flag = false;
          break;
        }
      }
      if (flag && count < articleResponseList.size()) {
        responsesList.add(articleResponseList.get(count));
        count++;
      }
    }
    //添加剩余文章
    for (int i = count; i < articleResponseList.size(); i++) {
      responsesList.add(articleResponseList.get(i));
    }
  }

  /**
   * 获取文章详情
   *
   * @param infoResponse
   * @param article
   */
  private void getArticleInfo(ClientArticleInfoResponse infoResponse, ArticleBO article) {
    ArticleLook articleLook = DiscoverUtil
        .convertStringResponse(article.getArticleLook(), ArticleLook.class);
    //计算宽高比例
    if (articleLook.getPictureLookList() != null) {
      articleLook.getPictureLookList().forEach(DiscoverUtil::computerWhRatio);
    }
    infoResponse.setArticleLook(articleLook);

    //阅读数转换
    String readCountString = DiscoverUtil.getReadCountString(article.getReadCount());
    infoResponse.setViewCountString(readCountString);

    //商品相关
    List<ClientProductResponse> productList = getClientProductResponseList(infoResponse);
    infoResponse.setProductList(productList);

    //分享赚总金额
    BigDecimal totalCommission = productList.stream().map(ClientProductResponse::getCommission)
        .reduce(BigDecimal::add).orElse(new BigDecimal(0));
    infoResponse.setTotalCommission(totalCommission);

    //推荐相关，超出下限不再寻找
    int clientRecommendCount = discoverProperties.getClientRecommendCount();
    //最终结果
    Set<ArticleBO> recommendSet = new HashSet<>();

    //TODO 暂时改造为商品一级分类查询 start
    getFirstProductList(productList, clientRecommendCount, recommendSet);
    //从精选页查找
    getThreeArticleInfo(clientRecommendCount, recommendSet);
    //TODO 暂时改造为商品一级分类查询 end

    //TODO 后期开启 start
    //去除相同商品,减少查询DB次数
//        Set<Long> productIdSet = productList.stream().map(ClientProductResponse::getProductId).collect(Collectors.toSet());
//        //  第一梯度：同一商品的文章。
//        getFirstArticleInfo(productList, clientRecommendCount, recommendSet);
//        //	第二梯度：同为二级类别商品的关联文章。
//        getSecondArticleInfo(productList, clientRecommendCount, recommendSet, productIdSet);
//        //	第三梯度：文章流中的sort和X排序值。
//        getThreeArticleInfo(clientRecommendCount, recommendSet);
    //TODO 后期开启 end

    //去除本文章
    List<ArticleBO> resultProduct = recommendSet.stream()
        .filter(a -> !article.getArticleId().equals(a.getArticleId())).collect(Collectors.toList());

    //封装数据
    List<ClientArticleResponse> clientArticleResponses = new ArrayList<>();
    resultProduct.forEach(articleOne -> clientArticleResponses
        .add(DiscoverUtil.getClientArticleResponse(articleOne)));
    infoResponse.setArticleList(clientArticleResponses);
  }

  /**
   * 获取文章详情,兼容老版本,下版本删除
   *
   * @param infoResponse
   * @param article
   */
  private void getArticleInfoOld(ClientArticleInfoResponse infoResponse, ArticleBO article) {
    ArticleLook articleLook = DiscoverUtil
        .convertStringResponse(article.getArticleLook(), ArticleLook.class);
    //计算宽高比例
    if (articleLook.getPictureLookList() != null) {
      articleLook.getPictureLookList().forEach(DiscoverUtil::computerWhRatio);
    }
    infoResponse.setArticleLook(articleLook);

    //阅读数转换
    String readCountString = DiscoverUtil.getReadCountString(article.getReadCount());
    infoResponse.setViewCountString(readCountString);

    //商品相关
    List<ClientProductResponse> productList = getClientProductResponseList(infoResponse);
    //兼容老版本的iOS错误
    if (ObjectUtils.isEmpty(productList)) {
      //   添加默认商品
      ClientProductInfoRequest request = new ClientProductInfoRequest();
      request.setProductNo(discoverProperties.getClientDefaultProductNo());
      ProductBO productBO = clientProductBiz.queryProductInfo(request);
      log.info("添加默认商品{}", productBO);
      ClientProductResponse productResponse = DiscoverUtil
          .convertResponse(productBO, ClientProductResponse.class);
      productResponse.setProductUrlList(Collections.singletonList(productBO.getMasterImg()));
      productList.add(productResponse);
    }
    infoResponse.setProductList(productList);

    //分享赚总金额
    BigDecimal totalCommission = productList.stream().map(ClientProductResponse::getCommission)
        .reduce(BigDecimal::add).orElse(new BigDecimal(0));
    infoResponse.setTotalCommission(totalCommission);

    //推荐相关，超出下限不再寻找
    int clientRecommendCount = discoverProperties.getClientRecommendCount();
    //最终结果
    Set<ArticleBO> recommendSet = new HashSet<>();

    //TODO 暂时改造为商品一级分类查询 start
    getFirstProductList(productList, clientRecommendCount, recommendSet);

    Set<ClientArticleResponse> result = new HashSet<>();
    for (ArticleBO articleBO : recommendSet) {
      ClientArticleResponse clientArticleResponse = DiscoverUtil
          .getClientArticleResponseOld(articleBO);
      if (!ObjectUtils.isEmpty(clientArticleResponse)) {
        result.add(clientArticleResponse);
      }
    }
    //从精选页查找
    getThreeArticleInfoOld(clientRecommendCount, result);
    //TODO 暂时改造为商品一级分类查询 end

    //去除本文章
    List<ClientArticleResponse> resultProduct = result.stream()
        .filter(a -> !article.getArticleId().equals(a.getArticleId())).collect(Collectors.toList());

    //封装数据
    infoResponse.setArticleList(resultProduct);
  }

  private void getFirstProductList(List<ClientProductResponse> productList,
      int clientRecommendCount, Set<ArticleBO> recommendSet) {
    List<Long> productIdList = productList.stream().map(ClientProductResponse::getProductId)
        .collect(Collectors.toList());
    if (productIdList.size() > 0) {
      //查找所有相关一级分类
      Set<Integer> categoryIdFirstList = clientProductBiz.queryCategoryIdFirstPage(productIdList);
      //查找旗下所有商品
      if (categoryIdFirstList.size() > 0) {
        List<Long> productIds = clientProductBiz
            .queryProductPageByCategoryIdFirst(categoryIdFirstList);
        getFirstArticleInfoById(productIds, clientRecommendCount, recommendSet);
      }
    }
  }

  /**
   * 添加商品相关文章
   */
  private void addAllProductArticle(Long productId, int clientRecommendCount,
      Set<ArticleBO> recommendList) {

    List<ArticleBO> articleBOS = clientProductBiz
        .queryProductArticlePage(productId, 0, clientRecommendCount);
    recommendList.addAll(articleBOS);
  }

  private List<ClientProductResponse> getClientProductResponseList(
      ClientArticleInfoResponse infoResponse) {
    List<ClientProductResponse> productList = new ArrayList<>();
    List<ProductBO> productBOS = clientArticleBiz.queryArticleProduct(infoResponse.getArticleId());
    productBOS.forEach(productBO -> {
      ClientProductResponse productResponse = DiscoverUtil
          .convertResponse(productBO, ClientProductResponse.class);
      productResponse.setProductUrlList(Collections.singletonList(productBO.getMasterImg()));
      productList.add(productResponse);
    });
    return productList;
  }

  private void getThreeArticleInfo(int clientRecommendCount, Set<ArticleBO> recommendSet) {
    int currentPage = 1;
    while (recommendSet.size() < clientRecommendCount) {
      ClientArticlePageRequest articleRequest = new ClientArticlePageRequest();
      articleRequest.setCurrentPage(currentPage);
      List<ArticleBO> articleBOS = clientArticleBiz.queryArticlePage(articleRequest, null);
      if (articleBOS.size() == 0) {
        break;
      }
      recommendSet.addAll(articleBOS);
      currentPage++;
    }
  }

  private void getThreeArticleInfoOld(int clientRecommendCount, Set<ClientArticleResponse> result) {
    int currentPage = 1;
    while (result.size() < clientRecommendCount) {
      ClientArticlePageRequest articleRequest = new ClientArticlePageRequest();
      articleRequest.setCurrentPage(currentPage);
      List<ArticleBO> articleBOS = clientArticleBiz.queryArticlePage(articleRequest, null);
      if (articleBOS.size() == 0) {
        break;
      }
      filterArticleList(result, articleBOS);
      currentPage++;
    }
  }

  private void filterArticleList(Set<ClientArticleResponse> result, List<ArticleBO> articleBOS) {
    for (ArticleBO articleBO : articleBOS) {
      ClientArticleResponse clientArticleResponse = DiscoverUtil
          .getClientArticleResponseOld(articleBO);
      if (!ObjectUtils.isEmpty(clientArticleResponse)) {
        result.add(clientArticleResponse);
      }
    }
  }

  private void getSecondArticleInfo(List<ClientProductResponse> productList,
      int clientRecommendCount, Set<ArticleBO> recommendSet, Set<Long> productIdSet) {
    if (recommendSet.size() < clientRecommendCount) {
      //通过二级类别获取相关商品
      Set<Integer> collect = productList.stream().map(ClientProductResponse::getProductCategory)
          .collect(Collectors.toSet());
      first:
      for (Integer productCategory : collect) {
        int currentPage = 1;
        boolean flag = true;
        while (flag) {
          List<ProductBO> productBOS = clientProductBiz
              .queryProductListByCategory(productCategory, currentPage, clientRecommendCount);
          if (productBOS.size() == clientRecommendCount) {
            currentPage++;
          } else {
            flag = false;
          }
          for (ProductBO productBO : productBOS) {
            if (!productIdSet.contains(productBO.getProductId())) {
              productIdSet.add(productBO.getProductId());
              addAllProductArticle(productBO.getProductId(), clientRecommendCount, recommendSet);
              if (recommendSet.size() >= clientRecommendCount) {
                break first;
              }
            }
          }
        }
      }
    }
  }

  private void getFirstArticleInfo(List<ClientProductResponse> productList,
      int clientRecommendCount, Set<ArticleBO> recommendSet) {
    for (ClientProductResponse clientProductResponse : productList) {
      addAllProductArticle(clientProductResponse.getProductId(), clientRecommendCount,
          recommendSet);
      if (recommendSet.size() >= clientRecommendCount) {
        break;
      }
    }
  }

  private void getFirstArticleInfoById(List<Long> productIdList, int clientRecommendCount,
      Set<ArticleBO> recommendSet) {
    for (Long productId : productIdList) {
      addAllProductArticle(productId, clientRecommendCount, recommendSet);
      if (recommendSet.size() >= clientRecommendCount) {
        break;
      }
    }
  }
}
