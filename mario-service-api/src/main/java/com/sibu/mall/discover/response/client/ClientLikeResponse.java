package com.mall.discover.response.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/10/29
 * @Description:
 */
@Data
@ApiModel(value = "点赞用户信息")
public class ClientLikeResponse implements Serializable {
    private static final long serialVersionUID = -6790824298186846296L;

    @ApiModelProperty(value = "当前用户是否点赞")
    private Boolean likeStatus = false;

    @ApiModelProperty(value = "点赞数量字符串")
    private String likeCountString = "0";

    @ApiModelProperty(value = "点赞数量")
    private long likeCount = 0L;

    @ApiModelProperty(value = "点赞用户列表")
    private List<ClientLikePageResponse> likePageList;
}
