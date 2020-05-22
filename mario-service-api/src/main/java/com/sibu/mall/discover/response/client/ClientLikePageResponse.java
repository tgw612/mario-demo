package com.mall.discover.response.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/10/29
 * @Description:
 */
@Data
@ApiModel(value = "点赞用户信息列表")
public class ClientLikePageResponse implements Serializable {
    private static final long serialVersionUID = -6790824298186846296L;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "头像URL")
    private String iconUrl;

    @ApiModelProperty(value = "用户名称")
    private String nickName;
}
