package com.mall.discover.web.controller.discover2;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.client.ClientLikePageRequest;
import com.mall.discover.request.client.ClientUpdateLikeRequest;
import com.mall.discover.response.client.ClientLikePageResponse;
import com.mall.discover.web.service.impl.biz.ClientLikeBiz;
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
@Api(value = "点赞模块", tags = {"点赞模块"})
@RestController
@Slf4j
@RequestMapping(value = "v2.0/client/like")
public class ClientLikeController2 {

   @Autowired
   private ClientLikeBiz clientLikeBiz;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "点赞或取消",httpMethod = "POST",  notes = "点赞或取消")
    @PostMapping(value = "/updateLike")
    public CommonResponse<Boolean> updateLike(@Valid ClientUpdateLikeRequest request) {
        return clientLikeBiz.updateLike(request);
    }


    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "点赞列表分页", httpMethod = "GET", notes = "点赞列表分页")
    @GetMapping("/queryLikePage")
    public CommonResponse<CommonPageResult<ClientLikePageResponse>> queryLikePage(@Valid ClientLikePageRequest request){
      return clientLikeBiz.queryLikePage(request);
    }

}
