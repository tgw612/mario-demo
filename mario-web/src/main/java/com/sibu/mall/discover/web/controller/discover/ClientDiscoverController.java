package com.mall.discover.web.controller.discover;

import com.doubo.common.model.response.CommonResponse;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.web.response.client.ClientDiscoverLinkResponse;
import com.mall.discover.web.response.client.ClientDiscoverSwitchResponse;
import com.mall.discover.web.support.config.properties.DiscoverProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: huangzhong
 * @Date: 2019/10/14
 * @Description:
 */
@Api(value = "发现页基础模块", tags = {"发现页基础模块"})
@RestController
@Slf4j
@RequestMapping(value = "/client/discover")
public class ClientDiscoverController {

    @Autowired
    private DiscoverProperties discoverProperties;

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "发现页开关", httpMethod = "GET", notes = "发现页开关: true-开")
    @GetMapping(value = "/switch")
    public CommonResponse<ClientDiscoverSwitchResponse> queryDiscoverSwitch() {
        Integer discoverSwitch = discoverProperties.getDiscoverSwitch();
        ClientDiscoverSwitchResponse response = new ClientDiscoverSwitchResponse();
        response.setDiscoverSwitch(true);

        if (discoverSwitch == null || discoverSwitch <= 0) {
            response.setDiscoverSwitch(false);
            return ResponseManage.success(response);
        }
        return ResponseManage.success(response);
    }

    @ApiResponse(code = 0, message = "成功")
    @ApiOperation(value = "h5域名", httpMethod = "GET", notes = "h5域名")
    @GetMapping(value = "/queryLink")
    public CommonResponse<ClientDiscoverLinkResponse> queryLink() {
        String link = discoverProperties.getLink();
        ClientDiscoverLinkResponse response = new ClientDiscoverLinkResponse();
        response.setLink(link);
        return ResponseManage.success(response);
    }
}
