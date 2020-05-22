package com.mall.discover.service.impl.biz;

import com.alibaba.fastjson.TypeReference;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.request.PageQueryRequest;
import com.doubo.common.model.request.common.CommonPrimaryKeyRequest;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.threadlocal.SerialNo;
import com.doubo.common.util.ExceptionUtil;
import com.doubo.common.util.ObjectUtil;
import com.doubo.json.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.contants.DubboConstants;
import com.mall.discover.common.enums.BizTypeEnum;
import com.mall.discover.common.enums.RelationTypeEnum;
import com.mall.discover.common.util.BeanCopyUtils;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.config.properties.DiscoverProperties;
import com.mall.discover.persistence.bo.*;
import com.mall.discover.persistence.dao.mysql.ArticleMapper;
import com.mall.discover.persistence.dao.mysql.CountMapper;
import com.mall.discover.persistence.dao.mysql.ProductDao;
import com.mall.discover.persistence.dao.mysql.RelationMapper;
import com.mall.discover.persistence.entity.article.ArticleCountEntity;
import com.mall.discover.persistence.entity.article.RelationEntity;
import com.mall.discover.persistence.entity.product.ProductEntity;
import com.mall.discover.request.product.*;
import com.mall.discover.response.article.ArticleRelateProduct;
import com.mall.discover.response.product.ProductArticleResponse;
import com.mall.discover.response.product.ProductInfoResponse;
import com.mall.search.request.ProductSearchRequest;
import com.mall.search.request.QueryPageRequest;
import com.mall.search.response.*;
import com.mall.search.service.ESProductSearchService;
import com.mall.search.service.ESProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceBiz {
    @Autowired
    ProductDao productDao;
    @Autowired
    CountMapper countMapper;
    @Autowired
    RelationMapper relationMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    DiscoverProperties discoverProperties;

    @Reference(consumer = "elasticsearch-consumer-config")
    ESProductService esProductService;
    @Autowired
    ArticleServiceBiz articleServiceBiz;

    public CommonPageResult<ProductDetailResponse> queryProductInfo(ProductESSearchRequestDto request) {
        CommonPageResult<ProductDetailResponse> result = new CommonPageResult<>();
        CommonPrimaryKeyRequest<String> requestDto = new CommonPrimaryKeyRequest<>();
        requestDto.setId(request.getPid());
        CommonResponse<ProductDetailSearchResponse> dto
                = esProductService.findProByNum(requestDto);
        if (ObjectUtil.isNotNull(dto.getResult())) {
            ProductDetailResponse data = com.mall.common.utils.BeanCopyUtils.copyProperties(
                    ProductDetailResponse.class, dto.getResult());
            result.setTotalCount(1);
            result.setData(Lists.newArrayList(data));
            result.setTotalPage(1);
        }
        return result;
    }

    public ProductInfoResponse queryProductInfo(String productNo) {
        ProductInfoResponse productInfo = new ProductInfoResponse();
        CommonPrimaryKeyRequest<String> requestDto = new CommonPrimaryKeyRequest<>();
        requestDto.setId(productNo);
        CommonResponse<ProductDetailSearchResponse> dto
                = esProductService.findProByNum(requestDto);
        log.debug("rpc proDetailResponse json:" + JsonUtil.toJson(dto));
        if (dto.isSuccess() && ObjectUtil.isNotNull(dto.getResult())) {
            productInfo = BeanCopyUtils.copyProperties(ProductInfoResponse.class, dto.getResult());
            //新增商品有爆款活动时,需要更新商品价格
            ProductDetailSearchResponse productDetail = dto.getResult();
            if (!CollectionUtils.isEmpty(productDetail.getActives())) {
                List<ProductDetailSearchResponse.ProductActive> actives = productDetail.getActives();
                //发现server time为null 改为自用时间
                ProductDetailSearchResponse.ProductActive currentProductInfo = actives.stream().filter(active -> {
                    //发现server time为null 改为自用时间
                    if (System.currentTimeMillis() < active.getEndTime().getTime()) {
                        return true;
                    } else {
                        return false;
                    }
                }).min(Comparator.comparing(ProductDetailSearchResponse.ProductActive::getPrice))
                        .orElseGet(ProductDetailSearchResponse.ProductActive::new);

                if (ObjectUtil.isNotNull(currentProductInfo.getPrice())) {
                    productInfo.setPrice(currentProductInfo.getPrice());
                }
                if (ObjectUtil.isNotNull(currentProductInfo.getCommission())) {
                    productInfo.setCommission(currentProductInfo.getCommission());
                }
            }
        }
        log.debug("rpc ProductInfoResponse json:" + JsonUtil.toJson(productInfo));
        return productInfo;
    }

    private CommonResponse<ProductDetailSearchResponse> getProductDetailResponse(String productId) {
        CommonPrimaryKeyRequest<String> productInfoRequest = new CommonPrimaryKeyRequest<>();
        productInfoRequest.setId(productId);
        CommonResponse<ProductDetailSearchResponse> proDetailResponse
                = esProductService.findProByNum(productInfoRequest);
        log.debug("rpc proDetailResponse json:" + JsonUtil.toJson(proDetailResponse));
        return proDetailResponse;
    }

    public CommonResponse<Boolean> addOrEditProduct(ProductAddOrEditRequestDto request) {
        request.getProductNo().parallelStream().forEach(productNo -> {
            ProductEntity productEntity = productDao.selectByProductNo(productNo);
            //查询商品详情
            ProductInfoResponse productInfo = queryProductInfo(productNo);
            if (ObjectUtil.isNotNull(productInfo)) {
                if (ObjectUtil.isNull(productEntity)) {
                    //新增商品
                    ProductEntity entity = saveProductEntity(productInfo, request.getCurrentUserId());
                    //新增商品排序
                    saveProductCount(productInfo, entity);
                } else {
                    //更新商品
                    updateProductEntity(productEntity, productInfo);
                }
            }
        });
        return ResponseManage.success(Boolean.TRUE);
    }


    private void saveProductArticleRelation(ProductAddOrEditRequestDto request, Integer productId) {
        relationMapper.save(new RelationEntity() {{
            setFromId(Long.valueOf(productId));
            setToId(request.getArticleId());
            setCreateTime(System.currentTimeMillis());
            setUpdateTime(System.currentTimeMillis());
            setCreateUser(request.getCurrentUserId());
            setBiztype(RelationTypeEnum.GOODS_ARTICLE.getCode());
            setStatus(0);
        }});
    }

    private void saveProductCount(ProductInfoResponse productInfo, ProductEntity entity) {
        ArticleCountEntity articleCountEntity = new ArticleCountEntity();
        articleCountEntity.setBizId(Long.valueOf(entity.getProductId()));
        articleCountEntity.setBiztype(BizTypeEnum.PRODUCT.getCode());
        articleCountEntity.setStatus(0);
        Long sort = productInfo.getSort();
        articleCountEntity.setSort(sort == null || sort <= 0 || sort > discoverProperties.getMaxSort() ? discoverProperties.getMaxSort() : sort);

        articleCountEntity.setUpdateTime(System.currentTimeMillis());
        articleCountEntity.setCreateTime(System.currentTimeMillis());
        countMapper.insert(articleCountEntity);
    }

    private void updateProductEntity(ProductEntity productEntity, ProductInfoResponse productInfo) {
        BeanCopyUtils.copyProperties(productEntity, productInfo);
        copyToProductEntity(productInfo, productEntity);
        productEntity.setUpdateTime(System.currentTimeMillis());
        productDao.updateById(productEntity);
    }

    private ProductEntity saveProductEntity(ProductInfoResponse productInfo, String currentUserId) {
        ProductEntity productEntity = new ProductEntity();
        copyToProductEntity(productInfo, productEntity);
        productEntity.setCreateTime(System.currentTimeMillis());
        productEntity.setStatus(0);
        productEntity.setCreateUser(currentUserId);
        productEntity.setHighCommissionStatus(0);
        productEntity.setHotProductStatus(0);
        productEntity.setUpdateTime(System.currentTimeMillis());
        log.debug("productEntity json" + JsonUtil.toJson(productEntity));
        productDao.save(productEntity);
        return productEntity;
    }

    private void copyToProductEntity(ProductInfoResponse productInfo, ProductEntity productEntity) {
        productEntity.setProductId(productInfo.getPid());
        productEntity.setPrice(productInfo.getPrice());
        productEntity.setMasterImg(productInfo.getMasterImg());
        productEntity.setProductName(productInfo.getName());
        productEntity.setMallCount(productInfo.getSales());
        productEntity.setCommission(productInfo.getCommission());
        productEntity.setProductNo(productInfo.getProductNum());
        productEntity.setProductStatus(productInfo.getStatus());
        productEntity.setProductCategory(productInfo.getCategoryId());
    }

    public CommonResponse<CommonPageResult<ProductInfoResponse>> listProduct(ProductListRequestDto request) {
        CommonPageResult<ProductInfoResponse> result = new CommonPageResult<>();
        ProductListRequestBO bo = BeanCopyUtils.copyProperties(ProductListRequestBO.class, request);
        List<ProductInfoBO> productInfos = productDao.listProduct(bo, PageUtils.getStartPage(request), request.getPageSize());

        int count = productDao.listProductCount(bo);
        List<ProductInfoResponse> data = productInfos.stream().map(info -> {
            return convertToProductInfoResponse(info);
        }).collect(Collectors.toList());
        result.setData(data);
        if (ObjectUtil.isNull(count)) {
            count = 0;
        }
        result.setTotalCount(count);
        result.setTotalPage(PageUtils.getPageCount(request.getPageSize(), count));
        return ResponseManage.success(result);
    }

    public CommonResponse<ProductInfoResponse> getProductInfo(Long productId) {
        ProductInfoBO info = productDao.getProductInfo(productId);
        ProductInfoResponse result = convertToProductInfoResponse(info);
        return ResponseManage.success(result);
    }

    private ProductInfoResponse convertToProductInfoResponse(ProductInfoBO info) {
        ProductInfoResponse result = new ProductInfoResponse();
        result.setName(info.getProductName());
        result.setPid(info.getProductId());
        result.setPrice(info.getPrice());
        result.setMasterImg(info.getMasterImg());
        result.setCategoryId(info.getProductCategory());
        if (ObjectUtil.isNotNull(info.getHighCommissionStatus()) && 1 == info.getHighCommissionStatus()) {
            result.setHighFee(true);
        } else {
            result.setHighFee(false);
        }
        if (ObjectUtil.isNotNull(info.getHotProductStatus()) && 1 == info.getHotProductStatus()) {
            result.setHot(true);
        } else {
            result.setHot(false);
        }
        result.setProductNum(info.getProductNo());
        result.setCommission(info.getCommission());
        result.setArticleNums(relationMapper.selectProductRelateArticleCount(info.getProductId(), RelationTypeEnum.GOODS_ARTICLE.getCode()));
        result.setReadNums(info.getReadCount());
        result.setShareNums(info.getShareCount());
        result.setSort(info.getSort());
        ProductInfoResponse response = queryProductInfo(info.getProductNo());
        result.setStatus(response.getStatus());
        result.setSales(response.getSales());
        return result;
    }

    public CommonResponse<Boolean> productSortEdit(ProductSortEditRequestDto request) {
        countMapper.editSort(request.getBiztype(), request.getSort(), request.getId());
        return ResponseManage.success(Boolean.TRUE);
    }

    public CommonResponse<Boolean> productEdit(ProductEditRequestDto request) {
        ProductEditBO bo = new ProductEditBO();
        bo.setProductId(request.getGoodsId());
        if (ObjectUtil.isNotNull(request.getHighFee())) {
            if (request.getHighFee()) {
                bo.setHighFee(1);
            } else {
                bo.setHighFee(0);
            }
        }

        if (ObjectUtil.isNotNull(request.getHot())) {
            if (request.getHot()) {
                bo.setHot(1);
            } else {
                bo.setHot(0);
            }
        }
        productDao.productEdit(bo);
        return ResponseManage.success(Boolean.TRUE);
    }

    public CommonResponse<Boolean> deleteProductArticle(ProductDeleteArticleRequestDto request) {
        try {
            relationMapper.deleteProductArticle(RelationTypeEnum.GOODS_ARTICLE.getCode(), request.getArticleId(), request.getGoodsId());
            return ResponseManage.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("[{}]删除商品内文章失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
            return ResponseManage.fail("删除商品内文章失败");
        }
    }


    public CommonResponse<CommonPageResult<ProductArticleResponse>> queryProductArticlePage(ProductArticlePageRequestDto request) {
        try {
            ProductArticlePageRequestBO bo = BeanCopyUtils.copyProperties(ProductArticlePageRequestBO.class, request);
            int count = productDao.queryProductArticlePageCount(bo);
            CommonPageResult<ProductArticleResponse> result = new CommonPageResult<>();
            if (count > 0) {
                List<ProductArticleBO> productArticleBOList = productDao.queryProductArticlePage(bo,
                        PageUtils.getStartPage(request), request.getPageSize());
                List<ProductArticleResponse> dataList = new ArrayList<>(productArticleBOList.size());
                productArticleBOList.forEach(productArticleBO -> {
                    ProductArticleResponse data = BeanCopyUtils.copyProperties(ProductArticleResponse.class, productArticleBO);
                    //设置articlelook和图片数量
                    String articleLookStr = productArticleBO.getArticleLook();
                    if (StringUtils.isNotBlank(articleLookStr)) {
                        ArticleLook look = JsonUtil.parseObject(articleLookStr, new TypeReference<ArticleLook>() {
                        });
                        data.setArticleLooks(look);
                        if (ObjectUtil.isNotNull(look) && ObjectUtil.isNotNull(look.getPictureLookList())) {
                            data.setPictureNums(look.getPictureLookList().size());
                        } else {
                            data.setPictureNums(0);
                        }
                    }
                    //设置排序
                    if (productArticleBO.getSort() == null) {
                        data.setSort(discoverProperties.getMaxSort());
                    }
                    //设置点赞
                    data.setLikeCount(articleServiceBiz.getArticleLikeCount(productArticleBO.getArticleId()));
                    //设置关联话题
                    List<SubjectInfoBO> relateSubjectName = articleMapper.getRelateSubjectName(productArticleBO.getArticleId(),
                            RelationTypeEnum.SUBJECT_ARTICLE.getCode());
                    List<ProductArticleResponse.SubjectInfo> subjectNameList = BeanCopyUtils
                            .copyList(ProductArticleResponse.SubjectInfo.class, relateSubjectName);
                    data.setSubject(subjectNameList);
                    //设置商品
                    List<ProductIdNameBO> productIdNameBOList = articleMapper.getArticleRelateProduct(productArticleBO.getArticleId(),
                            RelationTypeEnum.GOODS_ARTICLE.getCode());
                    List<ArticleRelateProduct> products = BeanCopyUtils.copyList(ArticleRelateProduct.class, productIdNameBOList);
                    data.setProduct(products);

                    dataList.add(data);
                });
                //分页封装
                result.setTotalCount(count);
                result.setData(dataList);
                result.setTotalPage(PageUtils.getPageCount(request.getPageSize(), count));
            }
            return ResponseManage.success(result);
        } catch (Exception e) {
            log.error("[{}]查询商品内文章失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
            return ResponseManage.fail("查询商品内文章失败");
        }
    }

    /*
     * 商品精确查询
     */
    public CommonResponse<Boolean> scheduleUpdateProductEntity() {
        int count = productDao.countEntity();
        int pageSize = 20;
        int totalPage = (int) (count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
        PageQueryRequest request = new PageQueryRequest();
        for (int i = 1; i <= totalPage; i++) {
            request.setCurrentPage(i);
            request.setPageSize(pageSize);
            List<ProductEntity> productEntities = productDao.queryProductPage(PageUtils.getStartPage(request), request.getPageSize());
            productEntities.forEach(productEntity -> {
                try {
                    ProductInfoResponse queryProductInfo = queryProductInfo(productEntity.getProductNo());
                    boolean flag = false;
                    if (ObjectUtil.isNotNull(queryProductInfo)) {
                        if (ObjectUtil.isNotNull(productEntity.getProductName()) &&
                                !productEntity.getProductName().equals(queryProductInfo.getName())) {
                            productEntity.setProductName(queryProductInfo.getName());
                            flag = true;
                        }
                        if (ObjectUtil.isNotNull(productEntity.getMasterImg()) &&
                                !productEntity.getMasterImg().equals(queryProductInfo.getMasterImg())) {
                            productEntity.setMasterImg(queryProductInfo.getMasterImg());
                            flag = true;
                        }
                        if (ObjectUtil.isNotNull(productEntity.getPrice()) &&
                                !productEntity.getPrice().equals(queryProductInfo.getPrice())) {
                            productEntity.setPrice(queryProductInfo.getPrice());
                            flag = true;
                        }
                        if (ObjectUtil.isNotNull(productEntity.getMallCount()) &&
                                !productEntity.getMallCount().equals(queryProductInfo.getSales())) {
                            productEntity.setMallCount(queryProductInfo.getSales());
                            flag = true;
                        }
                        if (ObjectUtil.isNotNull(productEntity.getCommission()) &&
                                !productEntity.getCommission().equals(queryProductInfo.getCommission())) {
                            productEntity.setCommission(queryProductInfo.getCommission());
                            flag = true;
                        }
                        if (ObjectUtil.isNotNull(productEntity.getProductStatus()) &&
                                !productEntity.getProductStatus().equals(queryProductInfo.getStatus())) {
                            productEntity.setProductStatus(queryProductInfo.getStatus());
                            flag = true;
                        }
                        if (flag) {
                            productEntity.setUpdateTime(System.currentTimeMillis());
                            productDao.updateById(productEntity);
                        }
                    }
                } catch (Exception e) {
                    log.error("[{}]更新productEntiry失败, Exception:{}", productEntity.getProductNo(), ExceptionUtil.getAsString(e));
                }
            });
        }
        return ResponseManage.success(Boolean.TRUE);
    }

    @Reference(consumer = DubboConstants.ELASTICSEARCH_CONSUMER)
    private ESProductSearchService esProductSearchService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 启动商品复杂查询接口
     *
     * @return
     */
    public CommonResponse<Boolean> scheduleUpdateProductEntityV2() {
        int count = productDao.countEntity();
        int pageSize = 20;
        int totalPage = (int) (count % pageSize == 0 ? count / pageSize : count / pageSize + 1);

        for (int i = 1; i <= totalPage; i++) {
            PageQueryRequest request = new PageQueryRequest();
            request.setCurrentPage(i);
            request.setPageSize(pageSize);
            List<ProductEntity> productEntities = productDao
                    .queryProductPage(PageUtils.getStartPage(request), request.getPageSize());

            List<String> productNoList = productEntities.stream().map(ProductEntity::getProductNo).collect(Collectors.toList());
            CommonResponse<PageResponse<SearchProductResponse>> response = searchProductPage(productNoList);
            List<SearchProductResponse> datas = response.getResult().getDatas();
            Map<String, SearchProductResponse> productNumInfoMap = new ConcurrentHashMap<>();
            if (!CollectionUtils.isEmpty(datas)) {
                datas.forEach(product -> {
                    activesHandel(product);
                    productNumInfoMap.put(product.getProductNum(), product);
                });
            }
            productEntities.forEach(productEntity -> {
                try {
                    SearchProductResponse queryProductInfo = productNumInfoMap.get(productEntity.getProductNo());
                    boolean flag = false;
                    if (ObjectUtil.isNotNull(queryProductInfo)) {
                        flag = compareChangeProductField(productEntity, queryProductInfo, flag);
                        if (flag) {
                            productEntity.setUpdateTime(System.currentTimeMillis());
                            productDao.updateById(productEntity);
                        }
                    }
                } catch (Exception e) {
                    log.error("[{}]更新productEntiry失败, Exception:{}", productEntity.getProductNo(), ExceptionUtil.getAsString(e));
                }
            });
        }
        return ResponseManage.success(Boolean.TRUE);
    }

    private boolean compareChangeProductField(ProductEntity productEntity, SearchProductResponse queryProductInfo, boolean flag) {
        if (ObjectUtil.isNotNull(productEntity.getProductName()) &&
                !productEntity.getProductName().equals(queryProductInfo.getName())) {
            productEntity.setProductName(queryProductInfo.getName());
            flag = true;
        }
        if (ObjectUtil.isNotNull(productEntity.getMasterImg()) &&
                !productEntity.getMasterImg().equals(queryProductInfo.getMasterImg())) {
            productEntity.setMasterImg(queryProductInfo.getMasterImg());
            flag = true;
        }
        if (ObjectUtil.isNotNull(productEntity.getPrice()) &&
                !productEntity.getPrice().equals(queryProductInfo.getPrice())) {
            productEntity.setPrice(queryProductInfo.getPrice());
            flag = true;
        }
        if (ObjectUtil.isNotNull(productEntity.getMallCount()) &&
                !productEntity.getMallCount().equals(queryProductInfo.getSales())) {
            productEntity.setMallCount(queryProductInfo.getSales());
            flag = true;
        }
        if (ObjectUtil.isNotNull(productEntity.getCommission()) &&
                !productEntity.getCommission().equals(queryProductInfo.getCommission())) {
            productEntity.setCommission(queryProductInfo.getCommission());
            flag = true;
        }
        if (ObjectUtil.isNotNull(productEntity.getProductStatus()) &&
                !productEntity.getProductStatus().equals(queryProductInfo.getStatus())) {
            productEntity.setProductStatus(queryProductInfo.getStatus());
            flag = true;
        }
        if (ObjectUtil.isNotNull(queryProductInfo.getCategoryIdFirst()) &&
                !queryProductInfo.getCategoryIdFirst().equals(productEntity.getCategoryIdFirst())) {
            productEntity.setCategoryIdFirst(queryProductInfo.getCategoryIdFirst());
            flag = true;
        }
        if (ObjectUtil.isNotNull(queryProductInfo.getCategoryIdSecond()) &&
                !queryProductInfo.getCategoryIdSecond().equals(productEntity.getCategoryIdSecond())) {
            productEntity.setCategoryIdSecond(queryProductInfo.getCategoryIdSecond());
            flag = true;
        }
        return flag;
    }

    private void activesHandel(SearchProductResponse product) {
        if (!CollectionUtils.isEmpty(product.getActives())) {
            List<ActiveItem> actives = product.getActives();
            ActiveItem currentProductInfo = actives.stream().filter(active -> {
                //发现server time为null 改为自用时间
                if (System.currentTimeMillis() < active.getEndTime().getTime()) {
                    return true;
                } else {
                    return false;
                }
            }).min(Comparator.comparing(ActiveItem::getPrice))
                    .orElseGet(ActiveItem::new);
            if (ObjectUtil.isNotNull(currentProductInfo.getPrice())) {
                product.setPrice(currentProductInfo.getPrice());
            }
            if (ObjectUtil.isNotNull(currentProductInfo.getCommission())) {
                product.setCommission(currentProductInfo.getCommission());
            }
        }
    }

    private CommonResponse<PageResponse<SearchProductResponse>> searchProductPage(List<String> productNoList) {
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setProductClaszz(SearchProductResponse.class);
        QueryPageRequest pageRequest = new QueryPageRequest();
        pageRequest.setCurrentPage(1);
        pageRequest.setPageSize(productNoList.size());
        productSearchRequest.setPageRequest(pageRequest);
        productSearchRequest.setProductNums(productNoList);
        CommonResponse<PageResponse<SearchProductResponse>> response = esProductSearchService.searchByPage(productSearchRequest);
        CommonResponse<PageResponse<SearchPageProductResponse>> result = null;
        try {
            result = objectMapper.readValue(JsonUtil.toJson(response),
                    new com.fasterxml.jackson.core.type.TypeReference<CommonResponse<PageResponse<SearchPageProductResponse>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void updateProductHotField(ProductEntity productEntity, ProductInfoResponse queryProductInfo) {
        productEntity.setProductName(queryProductInfo.getName());
        productEntity.setMasterImg(queryProductInfo.getMasterImg());
        productEntity.setPrice(queryProductInfo.getPrice());
        productEntity.setCommission(queryProductInfo.getCommission());
        productEntity.setMallCount(queryProductInfo.getSales());
        productEntity.setUpdateTime(System.currentTimeMillis());
        productDao.updateById(productEntity);
    }

    /**
     * 商品分享次数缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.PRODUCT_SHARE_COUNT,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, Long> productShareCount;

    /**
     * 商品阅读次数缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.PRODUCT_READ_COUNT,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, Long> productReadCount;

    public CommonResponse<Boolean> updateReadShareCount() {
        EntityWrapper<ArticleCountEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("biztype", 2);
        wrapper.eq("is_deleted", 0);
        List<ArticleCountEntity> countEntities = countMapper.selectList(wrapper);
        Map<Integer, Long> productShareCountMap = new HashMap<>();
        productShareCountMap = productShareCount.getAll(countEntities.stream().map(entity -> {
            return Math.toIntExact(entity.getBizId());
        }).collect(Collectors.toSet()));
        Map<Integer, Long> productReadCountMap = productReadCount.getAll(countEntities.stream().map(entity -> {
            return Math.toIntExact(entity.getBizId());
        }).collect(Collectors.toSet()));
        List<CountReadShareUpdateBO> boList = new ArrayList<>(productReadCountMap.size());
        CountReadShareUpdateBO bo;
        for (Integer key : productShareCountMap.keySet()) {
            bo = new CountReadShareUpdateBO();
            bo.setBizId(Long.valueOf(key));
            bo.setReadCount(productShareCountMap.get(key));
            bo.setShareCount(productShareCountMap.get(key));
        }
        countMapper.batchUpate(boList);
        return ResponseManage.success(Boolean.TRUE);
    }

    /**
     * 1.查询出非高佣金/爆款的商品
     * 2.判断关联文章数量,为0的 删除
     *
     * @return
     */
    public CommonResponse<Boolean> scheduleDeleteProductEntity() {
        List<Integer> productIds = productDao.selectColdProduct();
        if (!CollectionUtils.isEmpty(productIds)) {
            productIds.forEach(productId -> {
                int articleNum = relationMapper.selectProductRelateArticleCount(productId, RelationTypeEnum.GOODS_ARTICLE.getCode());
                if (articleNum == 0) {
                    productDao.deleteProduct(productId);
                }
            });
        }
        return ResponseManage.success(Boolean.TRUE);
    }
}
