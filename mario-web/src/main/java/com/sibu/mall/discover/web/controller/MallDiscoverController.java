package com.mall.discover.web.controller;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.request.PrimarySeqRequest;
import com.doubo.common.model.response.CommonResponse;
import com.mall.common.manage.ResponseManage;
import com.mall.common.utils.SeqGenUtil;
import com.mall.discover.web.request.DiscoverPageQueryRequest;
import com.mall.discover.web.request.DiscoverSubmitRequest;
import com.mall.discover.web.request.GetShareCountRequest;
import com.mall.discover.web.response.MemberDiscoverResponse;
import com.mall.discover.web.service.impl.MallDiscoverService;
import com.mall.discover.web.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(value = "发现模块", tags = {"发现模块"})
@RestController
@Slf4j
@RequestMapping(value = "/discover")
public class MallDiscoverController {

    @Autowired
    private MallDiscoverService mallDiscoverService;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "发布动态", notes = "发布动态")
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public CommonResponse<Boolean> discoverSubmit(@Valid @RequestBody DiscoverSubmitRequest request){
        request.setInitiationID(SeqGenUtil.getLogId());
        CommonResponse<UserInfoVo> userInfoResponseCommonResponse = mallDiscoverService.checkUser(request);
        if(!userInfoResponseCommonResponse.isSuccess()){
            return ResponseManage.fail(userInfoResponseCommonResponse.getErrorMsg());
        }

        return mallDiscoverService.submitDiscover(request,userInfoResponseCommonResponse.getResult());
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "获取发现动态列表", httpMethod = "GET", notes = "获取发现动态列表")
    @RequestMapping(value = "/pageDiscover", method = RequestMethod.GET)
    public CommonResponse<CommonPageResult<MemberDiscoverResponse>> pageDiscover(@Valid DiscoverPageQueryRequest discoverPageQueryRequest) {
        return mallDiscoverService.pageDiscover(discoverPageQueryRequest);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "转发动态", notes = "转发动态")
    @RequestMapping(value = "/forward", method = RequestMethod.POST)
    public CommonResponse<Boolean> forward(@Valid PrimarySeqRequest request){
        return mallDiscoverService.forward(request);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "获取分享材料次数", httpMethod = "GET", notes = "获取分享材料次数")
    @RequestMapping(value = "/getShareCount", method = RequestMethod.GET)
    public CommonResponse<Integer> getShareCount(@Valid GetShareCountRequest request) {
        return mallDiscoverService.findShareCount(request.getProductId());
    }

}
