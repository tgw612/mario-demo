package com.mall.discover.web.controller.discover2;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.client.ClientSubjectArticlePageRequest;
import com.mall.discover.request.client.ClientSubjectInfoRequest;
import com.mall.discover.request.client.ClientSubjectPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientSubjectInfoResponse;
import com.mall.discover.response.client.ClientSubjectPageResponse;
import com.mall.discover.web.service.impl.biz.ClientSubjectBiz;
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
@Api(value = "话题模块", tags = {"话题模块"})
@RestController
@Slf4j
@RequestMapping(value = "v2.0/client/subject")
public class ClientSubjectController2 {

    @Autowired
    private ClientSubjectBiz clientSubjectBiz;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "话题列表分页", httpMethod = "GET", notes = "话题列表分页,只需要页码")
    @GetMapping("/querySubjectPage")
    public CommonResponse<CommonPageResult<ClientSubjectPageResponse>> querySubjectPage(@Valid ClientSubjectPageRequest request){
       return clientSubjectBiz.querySubjectPage(request);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "话题详情", httpMethod = "GET", notes = "话题详情")
    @GetMapping("/querySubjectInfo")
    public CommonResponse<ClientSubjectInfoResponse> querySubjectInfo(@Valid ClientSubjectInfoRequest request){
       return clientSubjectBiz.querySubjectInfo(request);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "话题内文章列表", httpMethod = "GET", notes = "话题内文章列表")
    @GetMapping("/querySubjectArticlePage")
    public CommonResponse<CommonPageResult<ClientArticleResponse>> querySubjectArticlePage(@Valid ClientSubjectArticlePageRequest request){
       return clientSubjectBiz.querySubjectArticlePage(request);
    }
}
