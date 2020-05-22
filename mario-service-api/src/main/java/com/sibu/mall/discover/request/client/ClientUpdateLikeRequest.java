package com.mall.discover.request.client;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: huangzhong
 * @Date: 2019/10/30
 * @Description:
 */
@ApiModel(value = "更新点赞")
@Data
public class ClientUpdateLikeRequest extends CommonRequest {

    @ApiModelProperty(value = "通用id", notes = "文章id、话题id、商品id", required = true)
    @Min(value = 1, message = "主键id不能小于0")
    private long commonId;

    @ApiModelProperty(value = "类型", notes = "文章：1、商品：2、话题：3", required = true)
    @NotNull(message = "类型不能为空")
    private Integer bizTypeId;

    @ApiModelProperty(value = "是否点赞", notes = "true:点赞，false：取消点赞", required = true)
    @NotNull(message = "点赞状态不能为空")
    private Boolean likeStatus;
}
