package com.mall.discover.web.controller.discover2;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.client.ClientProductArticlePageRequest;
import com.mall.discover.request.client.ClientProductInfoRequest;
import com.mall.discover.request.client.ClientProductPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientProductInfoResponse;
import com.mall.discover.response.client.ClientProductPageResponse;
import com.mall.discover.web.service.impl.biz.ClientProductBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: huangzhong
 * @Date: 2019/9/29
 * @Description:
 */
@Api(value = "商品模块", tags = {"商品模块"})
@RestController
@Slf4j
@RequestMapping(value = "v2.0/client/product")
public class ClientProductController2 {
    @Autowired
    private ClientProductBiz clientProductBiz;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "爆款商品列表", httpMethod = "GET", notes = "爆款商品列表,只需要页码")
    @GetMapping(value = "/queryHotProductPage")
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHotProductPage(@Valid ClientProductPageRequest request) {
       return clientProductBiz.queryHotProductPage(request);
    }


    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "高佣金商品列表", httpMethod = "GET", notes = "高佣金商品列表,只需要页码")
    @GetMapping(value = "/queryHighProductPage")
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryHighProductPage(@Valid ClientProductPageRequest request) {
       return clientProductBiz.queryHighProductPage(request);
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
       return clientProductBiz.queryProductArticlePage(request);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "商品列表", httpMethod = "GET", notes = "订单支付成功返回的商品列表，只需要页码，每页大小固定10条")
    @GetMapping(value = "/queryProductPage")
    public CommonResponse<CommonPageResult<ClientProductPageResponse>> queryProductPage(@Valid ClientProductPageRequest request) {
        return clientProductBiz.queryProductPage(request);
    }
}
