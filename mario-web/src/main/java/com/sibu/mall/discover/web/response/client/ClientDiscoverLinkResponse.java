package com.mall.discover.web.response.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/10/14
 * @Description:
 */
@Data
@ApiModel(value = "发现页h5域名")
public class ClientDiscoverLinkResponse implements Serializable {
    private static final long serialVersionUID = -6790824298186846296L;

    @ApiModelProperty(value = "h5域名")
    private String link;
}
