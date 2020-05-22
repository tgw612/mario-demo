package com.mall.discover.web.controller;

import com.doubo.common.model.request.PrimaryIdRequest;
import com.doubo.common.model.request.common.CommonRequest;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.response.api.MemberDiscoverCateListResponse;
import com.mall.discover.web.service.impl.MallDiscoverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Api(value = "发现模块", tags = {"发现模块"})
@RestController
@Slf4j
@RequestMapping(value = "/index")
public class IndexDiscoverController {

    @Autowired
    private MallDiscoverService mallDiscoverService;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "获取发现分类列表", notes = "获取发现分类列表")
    @RequestMapping(value = "/listCate", method = RequestMethod.GET)
    public CommonResponse<List<MemberDiscoverCateListResponse>> listCate(CommonRequest commonRequest) {
        return mallDiscoverService.listCate(commonRequest);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "获取商品推广次数", notes = "获取商品推广次数")
    @RequestMapping(value = "/getProductShareCount", method = RequestMethod.GET)
    public CommonResponse<Integer> getProductShareCount(@Valid PrimaryIdRequest primaryIdRequest) {
        return mallDiscoverService.getProductShareCount(primaryIdRequest);
    }

}
