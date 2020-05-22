package com.mall.discover.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.HighProductStatusEnum;
import com.mall.discover.common.enums.HotProductStatusEnum;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.config.properties.DiscoverProperties;
import com.mall.discover.persistence.bo.ArticleBO;
import com.mall.discover.persistence.bo.ProductBO;
import com.mall.discover.request.client.ClientProductArticlePageRequest;
import com.mall.discover.request.client.ClientProductInfoRequest;
import com.mall.discover.request.client.ClientProductPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientProductInfoResponse;
import com.mall.discover.response.client.ClientProductPageResponse;
import com.mall.discover.service.ClientProductService;
import com.mall.discover.service.impl.biz.ClientProductBiz;
import com.mall.discover.util.DiscoverUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
public class ClientProductServiceImpl implements ClientProductService {
    @Autowired
    private ClientProductBiz clientProductBiz;

    @Autowired
    private DiscoverProperties discoverProperties;

    /**
     *  爆款商品列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.HOT_PRODUCT_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientProductPageResponse> > hotProductQueryPage;

    /**
     *  高佣金商品列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.HIGH_PRODUCT_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientProductPageResponse> > highProductQueryPage;

    /**
     *  商品详情缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.PRODUCT_QUERY_INFO,
            cacheType = CacheType.REMOTE)
    private Cache<Long, ClientProductInfoResponse> productQueryInfo;

    /**
     *  商品内文章缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.PRODUCT_QUERY_ARTICLE_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<String, CommonPageResult<ClientArticleResponse>> productQueryArtilePage;

    /**
     *  商品编号和商品id对应关系
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.PRODUCT_NO_ID_RELATION,
            cacheType = CacheType.REMOTE)
    private Cache<String, Long> productNoIdRelation;

    /**
     *  商品列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.ORDER_PAYED_PRODUCT_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientProductPageResponse>> productQueryPage;

    @Override
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHotProductPage(ClientProductPageRequest request, boolean cache) {
        CommonPageResult<ClientProductPageResponse> result = new CommonPageResult<>();
        Integer totalCount = clientProductBiz.queryHotProductPageCount(HotProductStatusEnum.TRUE.getCode());

        //封装返回值
        if (DiscoverUtil.checkTotalCount(totalCount)) {
            List<ProductBO> productList = clientProductBiz.queryHotProductPage(request.getCurrentPage(), HotProductStatusEnum.TRUE.getCode());
            //缓存有效时间
            long clientExpireTime = discoverProperties.getClientExpireTime();
            List<ClientProductPageResponse> responseList = new ArrayList<>();
            getProductResponseList(result, productList, responseList);
            //获取总页数
            result.setTotalCount(totalCount);
            result.setTotalPage(PageUtils.getPageCount(discoverProperties.getClientPageSize(), totalCount));

            //缓存商品详情
            int clientCacheInfoMaxPage = discoverProperties.getClientCacheInfoMaxPage();
            if (cache && request.getCurrentPage() < clientCacheInfoMaxPage){
                cacheProductInfo(responseList, clientExpireTime);
            }

            //缓存爆款商品列表
            hotProductQueryPage.put(request.getCurrentPage(), result, clientExpireTime, TimeUnit.SECONDS);
        }
        return ResponseManage.success(result);
    }

    @Override
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHighProductPage(ClientProductPageRequest request, boolean cache) {
        CommonPageResult<ClientProductPageResponse> result = new CommonPageResult<>();
        Integer totalCount = clientProductBiz.queryHighProductPageCount(HighProductStatusEnum.TRUE.getCode());

        //封装返回值
        List<ClientProductPageResponse> responseList = new ArrayList<>();
        if (DiscoverUtil.checkTotalCount(totalCount)) {
            List<ProductBO> productList = clientProductBiz.queryHighProductPage(request.getCurrentPage(), HighProductStatusEnum.TRUE.getCode());
            getProductResponseList(result, productList, responseList);
            //获取总页数
            result.setTotalCount(totalCount);
            result.setTotalPage(PageUtils.getPageCount(discoverProperties.getClientPageSize(), totalCount));

            //缓存有效时间
            long clientExpireTime = discoverProperties.getClientExpireTime();
            //缓存爆款商品列表
            highProductQueryPage.put(request.getCurrentPage(), result, clientExpireTime, TimeUnit.SECONDS);
            //缓存商品详情
            int clientCacheInfoMaxPage = discoverProperties.getClientCacheInfoMaxPage();
            if (cache && request.getCurrentPage() < clientCacheInfoMaxPage) {
                cacheProductInfo(responseList, clientExpireTime);
            }
        }
        return ResponseManage.success(result);
    }

    @Override
    public CommonResponse<ClientProductInfoResponse> queryProductInfo(ClientProductInfoRequest request, boolean cache) {
        ProductBO productBO = clientProductBiz.queryProductInfo(request);
        ClientProductInfoResponse result = null;
        if (productBO != null) {
            result = DiscoverUtil.convertResponse(productBO, ClientProductInfoResponse.class);
            result.setProductUrlList(Collections.singletonList(productBO.getMasterImg()));
            productQueryInfo.put(request.getProductId(), result, discoverProperties.getClientExpireTime(), TimeUnit.SECONDS);
            //缓存商品内文章
            if (cache) {
                cacheProductArticlePage(request.getProductId());
            }
        }
        return ResponseManage.success(result);
    }

    @Override
    public CommonResponse<CommonPageResult<ClientArticleResponse>> queryProductArticlePage(ClientProductArticlePageRequest request) {
        CommonPageResult<ClientArticleResponse> result = new CommonPageResult<>();
        Integer totalCount = clientProductBiz.queryProductArticlePageCount(request.getProductId());

        if (DiscoverUtil.checkTotalCount(totalCount)) {
            List<ArticleBO> articleList = clientProductBiz.queryProductArticlePage(request.getProductId(), PageUtils.getStartPage(request), discoverProperties.getClientPageSize());
            //获取话题名称
            List<ClientArticleResponse> clientArticleResponses = new ArrayList<>();
            articleList.forEach(articleBO -> clientArticleResponses.add(DiscoverUtil.getClientArticleResponse(articleBO)));

            //获取总页数、总条数
            result.setData(clientArticleResponses);
            result.setTotalCount(totalCount);
            result.setTotalPage(PageUtils.getPageCount(discoverProperties.getClientPageSize(), totalCount));
            //缓存有效时间
            long clientExpireTime = discoverProperties.getClientExpireTime();
            //缓存商品内文章
            productQueryArtilePage.put(request.getProductId() + "-" + request.getCurrentPage(), result, clientExpireTime, TimeUnit.SECONDS);
        }

        return ResponseManage.success(result);
    }

    @Override
    public  CommonResponse<Long> queryProductNoIdRelation(String productNo) {
        List<ProductBO> products = clientProductBiz.queryProductIdNoList();
        for (ProductBO product : products) {
            productNoIdRelation.put(product.getProductNo(), product.getProductId());
        }
        Long productId = productNoIdRelation.get(productNo);
        return ResponseManage.success(productId);
    }

    @Override
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryProductPage(ClientProductPageRequest request, boolean cache) {
        //每页数据大小
        int clientPageSize = discoverProperties.getClientPageSize();
        CommonPageResult<ClientProductPageResponse> result = new CommonPageResult<>();
        //获取爆款商品
        Integer hotCount = clientProductBiz.queryHotProductPageCount(HotProductStatusEnum.TRUE.getCode());
        //获取高佣金商品
        Integer highCount = clientProductBiz.queryHighProductPageCount(HighProductStatusEnum.TRUE.getCode());

        //无效请求
        if (request.getCurrentPage() * clientPageSize > (hotCount + highCount)) {
            return ResponseManage.success(result);
        }

        List<ProductBO> hotProductPage = clientProductBiz.queryHotProductPage(HotProductStatusEnum.TRUE.getCode(), 0, hotCount);
        List<ProductBO> highProductPage = clientProductBiz.queryHighProductPage(HighProductStatusEnum.TRUE.getCode(), 0, highCount);
        //去重
        Set<Long> productIds = hotProductPage.stream().map(ProductBO::getProductId).collect(Collectors.toSet());
        for (ProductBO productBO : highProductPage) {
            if (!productIds.contains(productBO.getProductId())) {
                hotProductPage.add(productBO);
            }
        }
        //总页数
        int pageCount = PageUtils.getPageCount(clientPageSize, hotProductPage.size());

        //封装
        result.setTotalCount(hotProductPage.size());
        result.setTotalPage(pageCount);

        //缓存
        int count = 1;
        long clientExpireTime = discoverProperties.getClientExpireTime();
        while (pageCount >= count) {
            List<ProductBO> collect = hotProductPage.stream().skip(PageUtils.getStartPage(count, clientPageSize)).limit(clientPageSize).collect(Collectors.toList());
            getProductResponseList(result, collect);
            productQueryPage.put(count, result, clientExpireTime, TimeUnit.SECONDS);
            count++;
        }

        //返回结果
        return ResponseManage.success(productQueryPage.get(request.getCurrentPage()));
    }

    /**
     * 分页缓存商品内文章
     */
    private void cacheProductArticlePage(Long productId) {
        //缓存页数
        int clientArticleMaxPage = discoverProperties.getClientArticleMaxPage();
        for (int i = 1; i <= clientArticleMaxPage; i++) {
            ClientProductArticlePageRequest request = new ClientProductArticlePageRequest();
            request.setProductId(productId);
            request.setCurrentPage(i);
            //缓存文章
            queryProductArticlePage(request);
        }
    }

    /**
     * 缓存商品详情
     */
    private void cacheProductInfo(List<ClientProductPageResponse> responseList, long clientExpireTime) {
        for (ClientProductPageResponse response : responseList) {
            ClientProductInfoResponse infoResponse = DiscoverUtil.convertResponse(response, ClientProductInfoResponse.class);
            productQueryInfo.put(infoResponse.getProductId(), infoResponse, clientExpireTime, TimeUnit.SECONDS);
            //缓存商品内文章
            cacheProductArticlePage(infoResponse.getProductId());
        }
    }

    /**
     * 转换商品列表 BO -> response
     */
    private void getProductResponseList(CommonPageResult<ClientProductPageResponse> result, List<ProductBO> productList, List<ClientProductPageResponse> responseList) {
        for (ProductBO product : productList) {
            ClientProductPageResponse response = DiscoverUtil.convertResponse(product, ClientProductPageResponse.class);
            //获取图片URL
            response.setProductUrlList(Collections.singletonList(product.getMasterImg()));
            responseList.add(response);
            //计算佣金比例
            response.setCommissionRatio(DiscoverUtil.computerCommissionRatio(response.getPrice(), response.getCommission()));
            //获取文章列表，只显示前三个。  兼容老版本上限改为五个。
            List<ArticleBO> articleBOS = clientProductBiz.queryProductArticlePage(product.getProductId(), 0, discoverProperties.getProductArticleCount());
            List<ClientArticleResponse> clientArticleResponses = new ArrayList<>();
            articleBOS.forEach(articleBO -> clientArticleResponses.add(DiscoverUtil.getClientArticleResponse(articleBO)));
            response.setArticleList(clientArticleResponses);
        }

        //忽略总页数、总条数
        result.setData(responseList);
    }

    /**
     * 订单支付成功页 转换商品列表 BO -> response
     */
    private void getProductResponseList(CommonPageResult<ClientProductPageResponse> result, List<ProductBO> productList) {
        List<ClientProductPageResponse> responseList = new ArrayList<>();
        for (ProductBO product : productList) {
            ClientProductPageResponse response = DiscoverUtil.convertResponse(product, ClientProductPageResponse.class);
            //获取图片URL
            response.setProductUrlList(Collections.singletonList(product.getMasterImg()));
            //计算佣金比例
            response.setCommissionRatio(DiscoverUtil.computerCommissionRatio(response.getPrice(), response.getCommission()));
            responseList.add(response);
        }
        result.setData(responseList);
    }

}
