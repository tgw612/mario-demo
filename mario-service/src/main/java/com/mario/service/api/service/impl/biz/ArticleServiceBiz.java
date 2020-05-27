package com.mario.service.api.service.impl.biz;

import com.alibaba.fastjson.TypeReference;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.exception.ServiceException;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.threadlocal.SerialNo;
import com.doubo.common.util.ExceptionUtil;
import com.doubo.common.util.ObjectUtil;
import com.doubo.common.util.StringUtil;
import com.doubo.json.util.JsonUtil;
import com.doubo.redis.util.RedisTemplateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.VodLook;
import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.*;
import com.mall.discover.common.exception.code.BussinessErrCodeEnum;
import com.mall.discover.common.util.BeanCopyUtils;
import com.mall.discover.common.util.JsonUtils;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.persistence.bo.*;
import com.mall.discover.persistence.dao.mysql.*;
import com.mall.discover.persistence.entity.article.ArticleCountEntity;
import com.mall.discover.persistence.entity.article.ArticleEntity;
import com.mall.discover.persistence.entity.article.RelationEntity;
import com.mall.discover.request.article.*;
import com.mall.discover.request.product.ProductAddOrEditRequestDto;
import com.mall.discover.response.article.ArticleInfoResponse;
import com.mall.discover.response.article.ArticleListResponse;
import com.mall.discover.response.article.DiscoverCountListResponse;
import com.mall.discover.response.product.ProductInfoResponse;
import com.mall.discover.response.subject.ArticleHistorySubjectResponse;
import com.mario.mq.VodHandlerMqProducer;
import com.mario.mq.request.ArticleVodUpdateRequest;
import com.mario.service.api.config.QcloudConfigProperties;
import com.mario.service.api.config.properties.DiscoverProperties;
import com.mario.service.api.util.DiscoverUtil;
import com.mario.service.api.util.SignatureUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * @author zhiming
 */
@Slf4j
@Service
public class ArticleServiceBiz {

  @Autowired
  ArticleMapper articleMapper;
  @Autowired
  ProductDao productDao;
  @Autowired
  RelationMapper relationMapper;
  @Autowired
  CountMapper articleCountMapper;
  @Autowired
  ProductServiceBiz productServiceBiz;
  @Autowired
  DiscoverProperties discoverProperties;
  @Autowired
  VodClient vodClient;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  ObjectMapper mapper;
  @Autowired
  VodHandlerMqProducer producer;
  @Autowired
  SubjectBiz subjectBiz;
  @Autowired
  private DiscoverSubjectMapper subjectMapper;
  @Resource
  private QcloudConfigProperties qcloudConfigProperties;
  /**
   * 文章点赞用户缓存
   */
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.LIKE_USER_ARTICLE_PAGE,
      cacheType = CacheType.REMOTE)
  private Cache<Long, LinkedHashSet<Integer>> articleLikeUserListCache;
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.DISCOVER_ARTICLE_HISTORY_SUBJECT,
      cacheType = CacheType.REMOTE)
  private Cache<Long, LinkedHashSet<ArticleHistorySubjectResponse.HistorySubject>> ArticleIdHistorySubjectCache;
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.DISCOVER_ARTICLE_VOD_URL,
      cacheType = CacheType.REMOTE)
  private Cache<String, Long> vodIdArticleIdCache;
  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.DISCOVER_UPLOAD_VOD_EVENT,
      cacheType = CacheType.REMOTE)
  private Cache<String, VodLook> vodUpLoadEventCache;

  public Integer getArticleLikeCount(Long articleId) {
    LinkedHashSet<Integer> set = articleLikeUserListCache.get(articleId);
    if (set != null) {
      return set.size();
    } else {
      return 0;
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public CommonResponse<Boolean> addOrEditArticle(AddOrEditArticleRequest request) {
    ArticleEntity entity = null;
    if (ObjectUtil.isNotNull(request.getArticleId())) {
      //编辑文章
      entity = updateArticle(request);
    } else {
      entity = addArticle(request);
    }
    return ResponseManage.success(Boolean.TRUE);
  }

  private ArticleEntity addArticle(AddOrEditArticleRequest request) {
    //新增文章
    ArticleEntity entity = saveArticleEntity(request);
    //存储fileID和文章id关联关系
    if (!CollectionUtils.isEmpty(request.getArticleLook().getVodLookList())) {
      String fileId = request.getArticleLook().getVodLookList().get(0).getFileId();
      vodIdArticleIdCache.put(fileId, entity.getArticleId(), 3600 * 2, TimeUnit.SECONDS);
    }
    //保存count表
    saveCount(request.getSort(), entity);
    //保存商品
    saveProduct(request, entity);
    //保存商品与文章关系
    saveProductArticleRelation(request, entity.getArticleId());
    //保存话题与文章关系
    saveSubjectArticleRealtion(request, entity.getArticleId());
    return entity;
  }

  private ArticleEntity updateArticle(AddOrEditArticleRequest request) {
    AsyncUpdateArticleLook(request);
    ArticleEntity entity = updateArticleEntity(request);
    //保存或更新商品
    saveProduct(request, entity);
    //文章间关系
    articleMapper.deleteRelation(entity.getArticleId());
    saveProductArticleRelation(request, entity.getArticleId());
    saveSubjectArticleRealtion(request, entity.getArticleId());
    updateCount(request);
    return entity;
  }

  private void AsyncUpdateArticleLook(AddOrEditArticleRequest request) {
    if (request.getArticleLook() != null
        && !CollectionUtils.isEmpty(request.getArticleLook().getVodLookList())
        && StringUtil.isBlank(request.getArticleLook().getVodLookList().get(0).getWebpUrl())) {
      //新上传的视频没有webpUrl
      VodLook vodLookOrigin = request.getArticleLook().getVodLookList().get(0);
      String fileId = vodLookOrigin.getFileId();
      vodIdArticleIdCache.put(fileId, request.getArticleId(), 3600 * 2, TimeUnit.SECONDS);
//            uploadVodExecutor.execute(() -> {
//                try {
//                    Thread.sleep(10 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                VodLook vodLook = vodUpLoadEventCache.get(fileId);
//                if (vodLook != null && StringUtil.isNotBlank(vodLook.getWebpUrl())) {
//                    updateArticleLook(vodLook.getFileId(), request.getArticleId());
//                }
//            });
    }
  }

  private void asyncUploadVod(String fileId) {
    try {
      Thread.sleep(5 * 1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    PullEventsResponse response = getPullEventsResponse();
    if (ObjectUtil.isNotNull(response) && ObjectUtil.isNotNull(response.getEventSet())) {
      EventContent[] eventContents = response.getEventSet();//如果[]太长
      for (EventContent eventContent : eventContents) {
        if ("ProcedureStateChanged".equals(eventContent.getEventType())
            && eventContent.getProcedureStateChangeEvent().getFileId().equals(fileId)) {
          VodLook vodLook = generateVodLook(eventContent);
          updateArticleLook(eventContent, vodLook);
        }
      }
    }
  }

  private void saveProduct(AddOrEditArticleRequest request, ArticleEntity entity) {
    ProductAddOrEditRequestDto dto = new ProductAddOrEditRequestDto();
    dto.setArticleId(entity.getArticleId());
    dto.setProductNo(request.getProductNo());
    productServiceBiz.addOrEditProduct(dto);
  }

  private ArticleEntity saveArticleEntity(AddOrEditArticleRequest request) {
    ArticleEntity entity = getArticleEntity(request);
    log.debug("insert ArticleEntity json:" + JsonUtil.toJson(entity));
    articleMapper.saveArticleEntity(entity);
//        //处理上传视频
//        if (!CollectionUtils.isEmpty(request.getArticleLook().getVodLookList())) {
//            if (StringUtil.isBlank(request.getArticleLook().getVodLookList().get(0).getWebpUrl())) {
//                String fileId = request.getArticleLook().getVodLookList().get(0).getFileId();
//                uploadVodExecutor.execute(() -> {
//                    try {
//                        Thread.sleep(10 * 1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    VodLook vodLook = vodUpLoadEventCache.get(fileId);
//                    if (vodLook != null && StringUtil.isNotBlank(vodLook.getWebpUrl())) {
//                        updateArticleLook(vodLook.getFileId(), entity.getArticleId());
//                    }
//                });
//            }
//        }
    return entity;
  }

  private ArticleEntity updateArticleEntity(AddOrEditArticleRequest request) {
    ArticleEntity entity = articleMapper.selectById(request.getArticleId());
    entity.setArticleTitle(request.getArticleTitle());
    entity.setUpdateTime(System.currentTimeMillis());
    entity.setArticleContent(request.getArticleContent());
    entity.setArticleLook(JsonUtil.toJson(request.getArticleLook()));
    //更新look的类型
    entity.setArticleLookType(DiscoverUtil.getArticleLookType(request.getArticleLook()));
    entity.setArticleStatus(request.getArticleStatus());
    if (ObjectUtil.isNotNull(request.getPublishTime())) {
      entity.setPublishTime(request.getPublishTime());
    } else {
      entity.setPublishTime(0L);
    }
    articleMapper.updateAllColumnById(entity);
    return entity;
  }

  private void updateCount(AddOrEditArticleRequest request) {
    ArticleCountEntity articleCount = articleCountMapper.selectArticle(request.getArticleId());
    if (ObjectUtil.isNotNull(articleCount.getSort())
        && !articleCount.getSort().equals(request.getSort())) {
      articleCount.setSort(request.getSort() == null || request.getSort() <= 0 ||
          request.getSort() > discoverProperties.getMaxSort() ? discoverProperties.getMaxSort()
          : request.getSort());
      articleCount.setUpdateTime(System.currentTimeMillis());
      articleCountMapper.updateById(articleCount);
    }
  }

  private void saveCount(Long sort, ArticleEntity entity) {
    ArticleCountEntity existCountEntity = articleCountMapper.selectArticle(entity.getArticleId());
    if (ObjectUtil.isNull(existCountEntity)) {
      ArticleCountEntity articleCountEntity = new ArticleCountEntity();
      articleCountEntity.setBizId(entity.getArticleId());
      articleCountEntity.setBiztype(BizTypeEnum.ARTICLE.getCode());
      articleCountEntity.setStatus(0);
      articleCountEntity
          .setSort(sort == null || sort <= 0 || sort > discoverProperties.getMaxSort() ?
              discoverProperties.getMaxSort() : sort);
      articleCountEntity.setUpdateTime(System.currentTimeMillis());
      articleCountEntity.setCreateTime(System.currentTimeMillis());
      articleCountMapper.insert(articleCountEntity);
      log.debug("insert articleCountEntity json:" + JsonUtil.toJson(entity));
    } else {
      log.error("articleServiceBiz saveCount保存articleCount报错,数据库已存在数据:" + JsonUtil
          .toJson(existCountEntity));
      throw new ServiceException("统计表里存在该文章统计值,无法插入,请联系管理员排查");
    }
  }

  public Boolean updateArticleLookVod(VodLook vodLook, Long articleId) {
    if (vodLook != null && StringUtil.isNotBlank(vodLook.getCoverUrl())
        && StringUtil.isNotBlank(vodLook.getVodUrl())
        && StringUtil.isNotBlank(vodLook.getWebpUrl())) {
      List<VodLook> vodLooks = new ArrayList<>(1);
      String articleLookStr = articleMapper.selectArticleLook(articleId);
      ArticleLook articleLook = JsonUtil
          .parseObject(articleLookStr, new TypeReference<ArticleLook>() {
          });
      vodLooks.add(vodLook);
      articleLook.setVodLookList(vodLooks);
      articleMapper.updateArticleLook(articleId, JsonUtil.toJson(articleLook));
      return true;
    } else {
      return false;
    }
  }

  public Boolean updateArticleLook(VodLook vodLook, Long articleId) {
    if (articleId == null) {
      articleId = vodIdArticleIdCache.get(vodLook.getFileId());
    }
    if (articleId != null) {
      ArticleVodUpdateRequest msg = new ArticleVodUpdateRequest(articleId, vodLook);
      producer.sendVodHandlerTask(msg);
      log.info("视频处理工作完成 发送更新数据库消息 " + JsonUtil.toJson(msg));
      return true;
    }
    return false;
  }

  private ArticleEntity getArticleEntity(AddOrEditArticleRequest request) {
    ArticleEntity entity = new ArticleEntity();
    entity.setCreateUserType(request.getCreateUserType());
    entity.setArticleContent(request.getArticleContent());
    if (ObjectUtil.isNotNull(request.getPublishTime())) {
      entity.setPublishTime(request.getPublishTime());
    } else {
      entity.setPublishTime(0L);
    }
    entity.setStatus(0);
    if (StringUtil.isNotBlank(request.getArticleTitle())) {
      entity.setArticleTitle(request.getArticleTitle());
    }
    //上传无处理的json
    entity.setArticleLook(JsonUtil.toJson(request.getArticleLook()));
    //更新look的类型
    entity.setArticleLookType(DiscoverUtil.getArticleLookType(request.getArticleLook()));
    entity.setArticleStatus(request.getArticleStatus());
    entity.setCreateTime(System.currentTimeMillis());
    entity.setUpdateTime(System.currentTimeMillis());
    if (ObjectUtil.isNotNull(request.getCurrentUserId())) {
      entity.setCreateUser(request.getCurrentUserId());
    }
    return entity;
  }

  private void saveSubjectArticleRealtion(AddOrEditArticleRequest request, Long articleId) {
    if (ObjectUtil.isNotNull(request.getSubjectId())) {
      request.getSubjectId().forEach(subjectid -> {
        RelationEntity relationEntity = new RelationEntity();
        relationEntity.setFromId(subjectid);
        relationEntity.setToId(articleId);
        relationEntity.setCreateTime(System.currentTimeMillis());
        relationEntity.setUpdateTime(System.currentTimeMillis());
        relationEntity.setCreateUser(request.getCurrentUserId());
        relationEntity.setBiztype(RelationTypeEnum.SUBJECT_ARTICLE.getCode());
        relationEntity.setStatus(0);
        relationMapper.save(relationEntity);
      });
    }
  }

  private void saveProductArticleRelation(AddOrEditArticleRequest request, Long articleId) {
    List<String> productNos = request.getProductNo();
    if (ObjectUtil.isNotNull(productNos)) {
      productNos.forEach(productNo -> {
        String productId = productDao.selectIdByCode(productNo);
        relationMapper.save(new RelationEntity() {{
          setFromId(Long.valueOf(productId));
          setToId(articleId);
          setCreateTime(System.currentTimeMillis());
          setUpdateTime(System.currentTimeMillis());
          setBiztype(RelationTypeEnum.GOODS_ARTICLE.getCode());
          setStatus(0);
          setCreateUser(request.getCurrentUserId());
        }});
      });
    }
  }

  public CommonResponse<CommonPageResult<ArticleListResponse>> listArticle(
      ListArticleRequest request) {
    try {
      setOrderField(request);
      ListArticleRequestBO bo = BeanCopyUtils.copyProperties(ListArticleRequestBO.class, request);
      List<ArticleListBO> articleListBOList = articleMapper
          .listArticle(bo, PageUtils.getStartPage(request), request.getPageSize());
      int count = articleMapper.listArticleCount(bo);
      articleListBOList.parallelStream().forEach(articleListBO -> {
        //设置图片数量,图片详情
        setPictureNums(articleListBO);
        //设置关联话题
        setSubject(articleListBO);
        //设置关联商品
        setProduct(articleListBO);
        //设置点赞数
        articleListBO.setLikeCount(getArticleLikeCount(articleListBO.getArticleId()));
      });
      CommonPageResult<ArticleListResponse> result = new CommonPageResult<>();
      List<ArticleListResponse> list = JsonUtil
          .parseObject(JsonUtil.toJSON(articleListBOList).toString(),
              new TypeReference<List<ArticleListResponse>>() {
              });
      result.setData(list);
      result.setTotalCount(count);
      result.setTotalPage(PageUtils.getPageCount(request.getPageSize(), count));
      return ResponseManage.success(result);
    } catch (Exception e) {
      log.error("[{}]分页文章查询动态失败, Exception:{}", SerialNo.getSerialNo(),
          ExceptionUtil.getAsString(e));
      return ResponseManage.fail("分页查询动态失败");
    }
  }

  private void setProduct(ArticleListBO articleListBO) {
    List<ProductIdNameBO> productIdNameBOList = articleMapper
        .getArticleRelateProduct(articleListBO.getArticleId(),
            RelationTypeEnum.GOODS_ARTICLE.getCode());
    if (!ObjectUtils.isEmpty(productIdNameBOList)) {
      articleListBO.setProduct(productIdNameBOList);
    } else {
      articleListBO.setProduct(new ArrayList<>(0));
    }
  }

  private void setSubject(ArticleListBO articleListBO) {
    List<SubjectInfoBO> relateSubjectName = articleMapper
        .getRelateSubjectName(articleListBO.getArticleId(),
            RelationTypeEnum.SUBJECT_ARTICLE.getCode());
    if (!ObjectUtils.isEmpty(relateSubjectName)) {
      articleListBO.setSubject(relateSubjectName);
    } else {
      articleListBO.setSubject(new ArrayList<>(0));
    }
  }

  private void setPictureNums(ArticleListBO articleListBO) {
    ArticleLook look = JsonUtil
        .parseObject(articleListBO.getArticleLook(), new TypeReference<ArticleLook>() {
        });
    articleListBO.setArticleLooks(JsonUtil.parseObject(articleListBO.getArticleLook(),
        new TypeReference<com.mall.discover.persistence.bo.ArticleLook>() {
        }));
    if (look != null && look.getPictureLookList() != null) {
      articleListBO.setPictureNums(look.getPictureLookList().size());
    } else {
      articleListBO.setPictureNums(0);
    }
  }

  private void setOrderField(ListArticleRequest request) {
    if (ObjectUtil.isNotNull(request.getSortField()) && ObjectUtil
        .isNotNull(request.getSortOrder())) {
      switch (request.getSortField()) {
        case "1":
          request.setSortField("article_status");
          break;
        case "2":
          request.setSortField("da.create_time");
          break;
        case "3":
          request.setSortField("article_id");
          break;
      }
      if (request.getSortOrder().equals("ascend")) {
        request.setSortOrder("asc");
      }
      if (request.getSortOrder().equals("descend")) {
        request.setSortOrder("desc");
      }
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public CommonResponse<Boolean> deleteArticle(DeleteArticleRequest request) {
    articleMapper.deleteArticleById(request.getArticleId());
    relationMapper.deleteArticleRelation(request.getArticleId());
    articleCountMapper.deleteArticleCount(request.getArticleId());
    return ResponseManage.success(Boolean.TRUE);
  }

  public CommonResponse<ArticleInfoResponse> getArticleInfo(Long articleId) {
    try {
      ArticleInfoBO info = articleMapper.getArticleInfo(articleId);
      List<SubjectInfoBO> subject = articleMapper
          .getRelateSubjectName(articleId, RelationTypeEnum.SUBJECT_ARTICLE.getCode());
      info.setSubject(subject);
      ArticleInfoResponse result = BeanCopyUtils.copyProperties(ArticleInfoResponse.class, info);
      result.setPictureUrls(
          JsonUtil.parseObject(info.getArticleLook(), new TypeReference<ArticleLook>() {
          }));
      List<Long> productIds = articleMapper.getRelateProduct(articleId);
      List<ProductInfoResponse> productInfos = new ArrayList<>(productIds.size());
      productIds.forEach(productId -> {
        ProductInfoResponse infoResponse = productServiceBiz
            .queryProductInfo(productDao.searchNoByID(productId));
        if (ObjectUtil.isNotNull(infoResponse)) {
          productInfos.add(infoResponse);
        }
      });
      if (ObjectUtil.isNotNull(productInfos)) {
        result.setProductInfo(productInfos);
      }

      return ResponseManage.success(result);
    } catch (Exception e) {
      log.error("[{}]获取文章详情失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
      return ResponseManage.fail("获取文章详情失败");
    }
  }


  public CommonResponse<CommonPageResult<DiscoverCountListResponse>> listDiscoverCount(
      ListDiscoverCountRequest request) {
    try {
      CommonPageResult<DiscoverCountListResponse> result = new CommonPageResult<>();
      int count = articleCountMapper.countByType(request.getBiztype());
      List<ArticleCountEntity> list = articleCountMapper.queryPageList(
          request.getBiztype(), PageUtils.getStartPage(request), request.getPageSize());
      List<DiscoverCountListResponse> data = BeanCopyUtils
          .copyList(DiscoverCountListResponse.class, list);
      result.setData(data);
      result.setTotalCount(count);
      result.setTotalPage(PageUtils.getPageCount(request.getPageSize(), count));
      return ResponseManage.success(result);
    } catch (Exception e) {
      log.error("[{}]分页discovercount查询动态失败, Exception:{}", SerialNo.getSerialNo(),
          ExceptionUtil.getAsString(e));
      return ResponseManage.fail("分页查询discover_count失败");
    }
  }

  public CommonResponse<Boolean> editArticleSortClient(EditArticleSortClientRequest request) {
    try {
      List<DiscoverCountBO> data = BeanCopyUtils
          .copyList(DiscoverCountBO.class, request.getBachList());
      articleCountMapper.editDiscoverCountSortClient(data);
      return ResponseManage.success(Boolean.TRUE);
    } catch (Exception e) {
      log.error("[{}]修改文章client_sort失败, Exception:{}", SerialNo.getSerialNo(),
          ExceptionUtil.getAsString(e));
      return ResponseManage.fail("修改文章排序失败");
    }
  }


  public CommonResponse<Boolean> updateReadShareCount() {
    int pageSize = 50;
    Long count = articleCountMapper.countAllExist();
    List<CountReadShareUpdateBO> bo = null;
    int totalPage = (int) (count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
    for (int i = 1; i <= totalPage; i++) {
      int startRecord = (i - 1) * pageSize;
      bo = articleCountMapper.queryCountPageList(startRecord, pageSize);
      List<CountReadShareUpdateBO> updateBOList = new ArrayList<>(bo.size());
      try {
        bo.parallelStream().forEach(obj -> {
          getCacheCount(updateBOList, obj);
        });
        if (!CollectionUtils.isEmpty(updateBOList)) {
          articleCountMapper.batchUpate(updateBOList);
        }
      } catch (Exception e) {
        log.error("[{}]定时更新阅读数分享数出错, Exception:{}", SerialNo.getSerialNo(),
            ExceptionUtil.getAsString(e) + JsonUtils.toJson(updateBOList));
        return ResponseManage.fail("定时更新阅读数分享数出错");
      }
    }
    return ResponseManage.success(Boolean.TRUE);
  }

  private void getCacheCount(List<CountReadShareUpdateBO> updateBOList,
      CountReadShareUpdateBO obj) {
    Long originReadCount = obj.getReadCount();
    Long originShareCount = obj.getShareCount();
    Long readCount = 0L;
    Long shartCount = 0L;
    String readKey = null;
    String shareKey = null;
    if (BizTypeEnum.ARTICLE.getCode().equals(obj.getBiztype())) {
      readKey = DiscoverCountEnum.ARTICLE_READ_COUNT.getCode() + obj.getBizId();
      shareKey = DiscoverCountEnum.ARTICLE_SHARE_COUNT.getCode() + obj.getBizId();
      readCount = RedisTemplateUtil.get(readKey, Long.class);
      shartCount = RedisTemplateUtil.get(shareKey, Long.class);
    } else if (BizTypeEnum.PRODUCT.getCode().equals(obj.getBiztype())) {
      readKey = DiscoverCountEnum.PRODUCT_READ_COUNT.getCode() + obj.getBizId();
      shareKey = DiscoverCountEnum.PRODUCT_SHARE_COUNT.getCode() + obj.getBizId();
      readCount = RedisTemplateUtil.get(readKey, Long.class);
      shartCount = RedisTemplateUtil.get(shareKey, Long.class);
    } else if (BizTypeEnum.SUBJECT.getCode().equals(obj.getBiztype())) {
      readKey = DiscoverCountEnum.SUBJECT_READ_COUNT.getCode() + obj.getBizId();
      shareKey = DiscoverCountEnum.SUBJECT_SHARE_COUNT.getCode() + obj.getBizId();
      readCount = RedisTemplateUtil.get(readKey, Long.class);
      shartCount = RedisTemplateUtil.get(shareKey, Long.class);
    }
    if (ObjectUtil.isNotNull(readCount) && obj.getReadCount() < readCount) {
      obj.setReadCount(readCount);
    } else {
      RedisTemplateUtil.set(readKey, originReadCount);
    }
    if (ObjectUtil.isNotNull(shartCount) && obj.getShareCount() < shartCount) {
      obj.setShareCount(shartCount);
    } else {
      RedisTemplateUtil.set(shareKey, originShareCount);
    }

    if (ObjectUtil.isNotNull(obj.getReadCount()) && ObjectUtil.isNotNull(obj.getShareCount())) {
      if (!obj.getReadCount().equals(originReadCount) || !obj.getShareCount()
          .equals(originShareCount)) {
        updateBOList.add(obj);
      }
    }
  }

  public CommonResponse<Boolean> publishArticle() {
    //更新话题发布状态
    List<Long> subjectIds = subjectMapper.queryWaitPublishSubjectId(System.currentTimeMillis(),
        SubjectStatusEnum.TO_PUBLISHED.getCode());
    if (!ObjectUtils.isEmpty(subjectIds)) {
      subjectMapper.updateWaitPublishSubStatus(System.currentTimeMillis(),
          SubjectStatusEnum.PUBLISHED.getCode(), subjectIds);
    }

    //更新文章发布状态
    Long count = articleMapper.selectWaitPublishCount(System.currentTimeMillis());
    if (ObjectUtil.isNotNull(count) && count > 0) {
      int pageSize = 200;
      int totalPage = (int) (count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
      for (int i = 1; i <= totalPage; i++) {
        List<WaitPublishArticleBO> waitPublishArticles = articleMapper.
            selectWaitPublishList(System.currentTimeMillis(), (i - 1) * pageSize, pageSize);
        waitPublishArticles.forEach(obj -> {
          obj.setArticleStatus(ArticleStatusEnum.PUBLISHED.getCode());
          obj.setUpdateTime(System.currentTimeMillis());
        });
        articleMapper.updateArticleStatusBatch(waitPublishArticles);
      }
    }
    return ResponseManage.success(Boolean.TRUE);
  }


  public CommonResponse<String> getSignature() {
    Assert.notNull(qcloudConfigProperties.getSecretId(), "密钥ID不能为空");
    Assert.notNull(qcloudConfigProperties.getSecretKey(), "密钥KEY不能为空");
    Assert.notNull(qcloudConfigProperties.getRegion(), "区域不能为空");
    SignatureUtil sign = new SignatureUtil();
    sign.setSecretId(qcloudConfigProperties.getSecretId());
    sign.setSecretKey(qcloudConfigProperties.getSecretKey());
    sign.setStorageRegion(qcloudConfigProperties.getRegion());
    sign.setCurrentTime(System.currentTimeMillis() / 1000);
    sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
    sign.setSignValidDuration(3600 * 24 * 2);
    sign.setVodSubAppId(qcloudConfigProperties.getVodSubAppId());
    sign.setTaskNotifyMode(qcloudConfigProperties.getTaskNotifyMode());
    sign.setProcedure(qcloudConfigProperties.getProcedure());
    String signature = sign.getUploadSignature();
    System.out.println("signature : " + signature);
    return ResponseManage.success(signature);
  }

  public CommonResponse<Boolean> publishVodLook() {
    PullEventsResponse response = getPullEventsResponse();
    if (ObjectUtil.isNotNull(response) && ObjectUtil.isNotNull(response.getEventSet())) {
      EventContent[] eventContents = response.getEventSet();//如果[]太长
      for (EventContent eventContent : eventContents) {
        try {
          if ("ProcedureStateChanged".equals(eventContent.getEventType())) {
            VodLook vodLook = generateVodLook(eventContent);
            updateArticleLook(eventContent, vodLook);
          } else {
            confirmEvent(eventContent);
            //处理非任务流完成的事件通知
            log.error("处理非任务流完成的事件通知" + JsonUtil.toJson(eventContent));
          }
        } catch (Exception e) {
          log.error("处理任务流完成的事件通知失败" + e.getMessage());
        }
      }
    }
    return ResponseManage.success(Boolean.TRUE);
  }

  public CommonResponse<Boolean> publishVodLookGeneral() {
//        VodLook vodLook = vodUwpLoadEventCache.get(fileId);
//        if (vodLook != null && StringUtil.isNotBlank(vodLook.getWebpUrl())) {
//            updateArticleLook(vodLook.getFileId(), request.getArticleId());
//        }
    return ResponseManage.success(Boolean.TRUE);
  }

  private PullEventsResponse getPullEventsResponse() {
    PullEventsRequest req = new PullEventsRequest();
    req.setSubAppId(Long.valueOf(qcloudConfigProperties.getVodSubAppId()));
    PullEventsResponse response = null;
    try {
      response = vodClient.PullEvents(req);
    } catch (TencentCloudSDKException e) {
      log.error("回调获取视频任务流失败" + e.getMessage());
      throw new ServiceException(BussinessErrCodeEnum.POST_TENCENT_CLOUD);
    }
    return response;
  }

  private void updateArticleLook(EventContent eventContent, VodLook vodLook) {
    Long articleId = vodIdArticleIdCache
        .get(eventContent.getProcedureStateChangeEvent().getFileId());
    if (ObjectUtil.isNull(articleId)) {
      articleId = articleMapper
          .selectLikeArticleLook(eventContent.getProcedureStateChangeEvent().getFileId());
      if (articleId == null) {
        confirmEvent(eventContent);
      }
    }
    List<VodLook> vodLooks = new ArrayList<>(1);
    if (!StringUtil.isNull(vodLook.getWebpUrl())
        && !StringUtil.isNull(vodLook.getCoverUrl())
        && !ObjectUtil.isNull(articleId)) {
      String articleLookStr = articleMapper.selectArticleLook(articleId);
      ArticleLook articleLook = JsonUtil
          .parseObject(articleLookStr, new TypeReference<ArticleLook>() {
          });
      vodLooks.add(vodLook);
      articleLook.setVodLookList(vodLooks);
      articleMapper.updateArticleLook(articleId, JsonUtil.toJson(articleLook));
      confirmEvent(eventContent);
    } else {
      //如果超过多长时间未确认则如何操作
//            eventContent.getProcedureStateChangeEvent().
    }
  }


  private VodLook generateVodLook(EventContent eventContent) {
    MediaProcessTaskResult[] medias = eventContent.getProcedureStateChangeEvent()
        .getMediaProcessResultSet();
    VodLook vodLook = new VodLook();
    vodLook.setFileId(eventContent.getProcedureStateChangeEvent().getFileId());
    for (MediaProcessTaskResult media : medias) {
      if ("Transcode".equals(media.getType())) {
        vodLook.setVodUrl(media.getTranscodeTask().getOutput().getUrl());
        vodLook.setHeight(Math.toIntExact(media.getTranscodeTask().getOutput().getHeight()));
        vodLook.setWidth(Math.toIntExact(media.getTranscodeTask().getOutput().getWidth()));
        if (!ObjectUtil.isNull(vodLook.getWidth()) && ObjectUtil.isNotNull(vodLook.getHeight())) {
          vodLook.setWhRatio(
              new BigDecimal(vodLook.getWidth()).divide(new BigDecimal(vodLook.getHeight()), 2));
        }
      }
      if ("AnimatedGraphics".equals(media.getType())) {
        vodLook.setWebpUrl(media.getAnimatedGraphicTask().getOutput().getUrl());
      }
      if ("CoverBySnapshot".equals(media.getType())) {
        vodLook.setCoverUrl(media.getCoverBySnapshotTask().getOutput().getCoverUrl());
      }
    }
    return vodLook;
  }

  private void confirmEvent(EventContent eventContent) {
    ConfirmEventsRequest confirmEventsRequest = new ConfirmEventsRequest();
    confirmEventsRequest.setSubAppId(Long.valueOf(qcloudConfigProperties.getVodSubAppId()));
    String[] EventHandles = new String[1];
    EventHandles[0] = eventContent.getEventHandle();
    confirmEventsRequest.setEventHandles(EventHandles);
    try {
      vodClient.ConfirmEvents(confirmEventsRequest);
    } catch (TencentCloudSDKException e) {
      log.error("回调确认消费失败" + JsonUtil.toJson(eventContent.getEventHandle()) + e.getMessage());
    }
  }

  /**
   * 用于清理之前数据
   *
   * @return
   */
  public void confirmEvents() {
    PullEventsResponse response = getPullEventsResponse();
    if (ObjectUtil.isNotNull(response) && ObjectUtil.isNotNull(response.getEventSet())) {
      EventContent[] eventContents = response.getEventSet();//如果[]太长
      for (EventContent eventContent : eventContents) {
        confirmEvent(eventContent);
      }
    }
  }

  public VodLook searchTencentCloudVodLookByField(String fileId) {
    PullEventsResponse response = getPullEventsResponse();
    if (ObjectUtil.isNotNull(response) && ObjectUtil.isNotNull(response.getEventSet())) {
      EventContent[] eventContents = response.getEventSet();//如果[]太长
      for (EventContent eventContent : eventContents) {
        if ("ProcedureStateChanged".equals(eventContent.getEventType())
            && eventContent.getProcedureStateChangeEvent().getFileId().equals(fileId)) {
          VodLook vodLook = generateVodLook(eventContent);
          return vodLook;
        }
      }
    }
    return null;
  }

  public CommonResponse<Boolean> editRelateArticleSort(EditRelateArticleSortRequest request) {
    long sort = request.getSort() <= 0 || request.getSort() > discoverProperties.getMaxSort() ?
        discoverProperties.getMaxSort() : request.getSort();
    relationMapper.editRelateArticleSort(request.getRelationId(), sort);
    return ResponseManage.success(Boolean.TRUE);
  }
}