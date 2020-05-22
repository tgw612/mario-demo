package com.mall.discover.web.controller.discover;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.constants.RedisConstant;
import com.mall.discover.request.client.ClientProductArticlePageRequest;
import com.mall.discover.request.client.ClientProductInfoRequest;
import com.mall.discover.request.client.ClientProductPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientProductInfoResponse;
import com.mall.discover.response.client.ClientProductPageResponse;
import com.mall.discover.service.ClientProductService;
import com.mall.discover.web.common.constants.DubboConstants;
import com.mall.discover.web.service.impl.biz.ClientProductBiz;
import com.mall.discover.web.util.DiscoverUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: huangzhong
 * @Date: 2019/9/29
 * @Description:
 */
@Api(value = "商品模块", tags = {"商品模块"})
@RestController
@Slf4j
@RequestMapping(value = "/client/product")
public class ClientProductController {

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
     *  高佣金商品列表缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.HIGH_PRODUCT_QUERY_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<Integer, CommonPageResult<ClientProductPageResponse> > highProductQueryPage;

    /**
     *  商品内文章缓存
     */
    @CreateCache(
            area = RedisConstant.Area.DISCOVER,
            name = RedisConstant.Key.PRODUCT_QUERY_ARTICLE_PAGE,
            cacheType = CacheType.REMOTE)
    private Cache<String, CommonPageResult<ClientArticleResponse>> productQueryArtilePage;

    @Autowired
    private ClientProductBiz clientProductBiz;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "爆款商品列表", httpMethod = "GET", notes = "爆款商品列表,只需要页码")
    @GetMapping(value = "/queryHotProductPage")
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHotProductPage(@Valid ClientProductPageRequest request) {
        CommonPageResult<ClientProductPageResponse> result = new CommonPageResult<>();
        try {
            result = hotProductQueryPage.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("老版本，获取缓存爆款商品列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            CommonResponse<CommonPageResult<ClientProductPageResponse>> response = clientProductService.queryHotProductPage(request, false);
            if (!response.isSuccess()) {
                log.error("老版本，DB中获取爆款商品列表出错,错误信息：{}", response);
                return ResponseManage.success(new CommonPageResult<>());
            }
            result = response.getResult();
        }

        //筛选文章列表,兼容老版本
        filterPictureLookArticle(result);

        return ResponseManage.success(result);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "高佣金商品列表", httpMethod = "GET", notes = "高佣金商品列表,只需要页码")
    @GetMapping(value = "/queryHighProductPage")
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHighProductPage(@Valid ClientProductPageRequest request) {
        CommonPageResult<ClientProductPageResponse> result = new CommonPageResult<>();
        try {
            result = highProductQueryPage.get(request.getCurrentPage());
        } catch (Exception e) {
            log.error("老版本，获取缓存高佣金商品列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            CommonResponse<CommonPageResult<ClientProductPageResponse>> response = clientProductService.queryHighProductPage(request, false);
            if (!response.isSuccess()) {
                log.error("老版本，DB中获取高佣金商品列表出错,错误信息：{}", response);
                return ResponseManage.success(new CommonPageResult<>());
            }
            result = response.getResult();
        }

        //筛选文章列表,兼容老版本
        filterPictureLookArticle(result);

        return ResponseManage.success(result);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "商品详情", httpMethod = "GET",notes = "商品详情")
    @GetMapping(value = "/queryProductInfo")
    public CommonResponse<ClientProductInfoResponse> queryGoodsInfo(@Valid ClientProductInfoRequest request) {
       return clientProductBiz.queryGoodsInfo(request);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "商品内文章列表", httpMethod = "GET",notes = "商品内文章列表")
    @GetMapping(value = "/queryProductArticlePage")
    public CommonResponse<CommonPageResult<ClientArticleResponse>> queryProductArticlePage(@Valid ClientProductArticlePageRequest request) {
        CommonPageResult<ClientArticleResponse> result = new CommonPageResult<>();
        try {
            result = productQueryArtilePage.get(request.getProductId() + "-" + request.getCurrentPage());
        } catch (Exception e) {
            log.error("老版本，获取缓存商品内文章列表出错,错误信息：{}", e.getMessage());
        }
        if (result == null) {
            CommonResponse<CommonPageResult<ClientArticleResponse>> response = clientProductService.queryProductArticlePage(request);
            if (!response.isSuccess()) {
                log.error("老版本，DB中获取商品详情的文章列表出错,错误信息：{}", response);
                return ResponseManage.success(new CommonPageResult<>());
            }
            result = response.getResult();
        }

        //老版本兼容，筛选数据（目前关联文章最多4条，客户端近期会有强更）
        List<ClientArticleResponse> data = result.getData();
        int initSize = data.size();
        List<ClientArticleResponse> articleResponseList = getArticleResponseList(data);
        result.setData(articleResponseList);
        result.setTotalCount(result.getTotalCount() - (initSize - articleResponseList.size()));
        return ResponseManage.success(result);
    }

    private void filterPictureLookArticle(CommonPageResult<ClientProductPageResponse> result) {
        List<ClientProductPageResponse> productPageResponses = result.getData();
        for (ClientProductPageResponse product : productPageResponses) {
            List<ClientArticleResponse> articleResponseList = getArticleResponseList(product.getArticleList());
            product.setArticleList(articleResponseList);
        }
    }

    private List<ClientArticleResponse> getArticleResponseList(List<ClientArticleResponse> articleList) {
        return articleList.stream()
                        .filter(article -> (!ObjectUtils.isEmpty(article.getPictureLook()) && !ObjectUtils.isEmpty(article.getPictureLook().getPictureUrl())))
                        .collect(Collectors.toList());
    }
}
