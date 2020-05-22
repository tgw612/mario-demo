package com.mall.discover.web.controller.discover2;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.client.ClientArticleInfoRequest;
import com.mall.discover.request.client.ClientArticlePageRequest;
import com.mall.discover.request.client.ClientUpdateCountRequest;
import com.mall.discover.response.client.ClientArticleInfoResponse;
import com.mall.discover.response.client.ClientDiscoverResponse;
import com.mall.discover.web.service.impl.biz.ClientArticleBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: huangzhong
 * @Date: 2019/9/29
 * @Description:
 */
@Api(value = "文章模块", tags = {"文章模块"})
@RestController
@Slf4j
@RequestMapping(value = "v2.0/client/article")
public class ClientArticleController2 {

   @Autowired
   private ClientArticleBiz clientArticleBiz;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "文章列表分页", httpMethod = "GET", notes = "文章列表分页,只需要页码")
    @GetMapping("/queryArticlePage")
    public CommonResponse<CommonPageResult<ClientDiscoverResponse>> queryArticlePage(@Valid ClientArticlePageRequest request){
        return clientArticleBiz.queryArticlePage(request);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "文章详情", httpMethod = "GET", notes = "文章详情")
    @GetMapping(value = "/queryArticleInfo")
    public CommonResponse<ClientArticleInfoResponse> articleInfo(@Valid ClientArticleInfoRequest request) {
        return clientArticleBiz.articleInfo(request);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "更新分享数",httpMethod = "POST",  notes = "更新分享数")
    @PostMapping(value = "/updateCount")
    public CommonResponse<Boolean> updateCount(@Valid ClientUpdateCountRequest request) {
       return clientArticleBiz.updateCount(request);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "更新文章音频状态",httpMethod = "POST",  notes = "更新分享数")
    @PostMapping(value = "/batchUpdateLookType")
    public CommonResponse<Boolean> batchUpdateLookType() {
        return clientArticleBiz.batchUpdateLookType();
    }
}
