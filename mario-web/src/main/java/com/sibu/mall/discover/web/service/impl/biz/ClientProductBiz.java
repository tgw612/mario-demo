package com.mall.discover.web.service.impl.biz;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.redis.util.RedisTemplateUtil;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.common.enums.DiscoverCountEnum;
import com.mall.discover.request.client.ClientProductArticlePageRequest;
import com.mall.discover.request.client.ClientProductInfoRequest;
import com.mall.discover.request.client.ClientProductPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientProductInfoResponse;
import com.mall.discover.response.client.ClientProductPageResponse;
import com.mall.discover.service.ClientProductService;
import com.mall.discover.web.common.constants.DubboConstants;
import com.mall.discover.web.util.DiscoverUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashSet;

/**
 * @author: huangzhong
 * @Date: 2019/11/4
 * @Description:
 */
@Service
@Slf4j
public class ClientProductBiz {
    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientProductService clientProductService;

    /**
     *  爆款商品列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.HOT_PRODUCT_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientProductPageResponse>> hotProductQueryPage;

    /**
     *  商品列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.ORDER_PAYED_PRODUCT_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientProductPageResponse>> productQueryPage;

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
     *  文章点赞用户缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.LIKE_USER_ARTICLE_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Long, LinkedHashSet<Integer>> articleLikeUserListCache;

    /**
     *  商品编号和商品id对应关系
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.PRODUCT_NO_ID_RELATION,
            cacheType = CacheType.REMOTE)
    private Cache<String, Long> productNoIdRelation;

    public ClientProductBiz() {
    }

    /**
     * 爆款商品列表
     */
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHotProductPage(ClientProductPageRequest request) {
        CommonPageResult<ClientProductPageResponse> result = new CommonPageResult<>();
        try {
            result = hotProductQueryPage.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("获取缓存爆款商品列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            return clientProductService.queryHotProductPage(request, false);
        }
        return ResponseManage.success(result);
    }

    /**
     * 高佣金商品列表
     */
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHighProductPage(ClientProductPageRequest request) {
        CommonPageResult<ClientProductPageResponse> result = new CommonPageResult<>();
        try {
            result = highProductQueryPage.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("获取缓存高佣金商品列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            return clientProductService.queryHighProductPage(request, false);
        }
        return ResponseManage.success(result);
    }

    /**
     * 商品详情
     */
    public CommonResponse<ClientProductInfoResponse>  queryGoodsInfo(ClientProductInfoRequest request) {
        ClientProductInfoResponse infoResponse = new ClientProductInfoResponse();

        //兼容商品编号改造
        Long productId =  DiscoverUtil.getProductId(request.getProductId(), request.getProductNo(), productNoIdRelation, clientProductService);
        if (ObjectUtils.isEmpty(productId)) {
            return ResponseManage.fail("该商品详情不存在");
        }
        request.setProductId(productId);

        try {
            //自增阅读数
            RedisTemplateUtil.incrLong(DiscoverCountEnum.PRODUCT_READ_COUNT.getCode() + productId);
            //获取返回值
            infoResponse = productQueryInfo.get(productId);
        } catch (Exception e) {
            log.error("获取缓存商品详情出错,错误信息：{}", e.getMessage());
        }

        if (infoResponse == null) {
            CommonResponse<ClientProductInfoResponse> result = clientProductService.queryProductInfo(request, false);
            if (!result.isSuccess()) {
                log.error("DB中获取商品详情出错,错误信息：{}", result);
                return ResponseManage.fail("该商品详情不存在");
            }else {
                return result;
            }
        }
        return ResponseManage.success(infoResponse);
    }

    /**
     * 商品内文章列表
     */
    public CommonResponse<CommonPageResult<ClientArticleResponse>> queryProductArticlePage(ClientProductArticlePageRequest request) {
        CommonPageResult<ClientArticleResponse> result = new CommonPageResult<>();

        //兼容商品编号改造
        Long productId = DiscoverUtil.getProductId(request.getProductId(), request.getProductNo(), productNoIdRelation, clientProductService);
        if (ObjectUtils.isEmpty(productId)) {
            return ResponseManage.success(result);
        }
        request.setProductId(productId);

        try {
            result = productQueryArtilePage.get(request.getProductId() + "-" + request.getCurrentPage());
        } catch (Exception e) {
            log.error("获取缓存商品内文章列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            CommonResponse<CommonPageResult<ClientArticleResponse>> response = clientProductService.queryProductArticlePage(request);
            if (!response.isSuccess()) {
                log.error("DB中获取商品详情的文章列表出错,错误信息：{}", response);
                return ResponseManage.success(new CommonPageResult<>());
            }
            result = response.getResult();
        }
        DiscoverUtil.getUserLikePage(articleLikeUserListCache, result.getData());
        return ResponseManage.success(result);
    }

    /**
     * 订单支付成功返回的商品列表
     */
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryProductPage(ClientProductPageRequest request) {
        CommonPageResult<ClientProductPageResponse> result = new CommonPageResult<>();
        try {
            result = productQueryPage.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("获取缓存商品列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            return clientProductService.queryProductPage(request, false);
        }
        return ResponseManage.success(result);
    }


}
