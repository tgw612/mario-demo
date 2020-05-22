package com.mall.discover.request.client;

import com.doubo.common.model.request.PageQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "文章列表")
@Data
public class ClientLikePageRequest extends PageQueryRequest {
    @ApiModelProperty(value = "通用id", notes = "文章id、话题id、商品id", required = true)
    @Min(value = 1, message = "主键id不能小于0")
    private long commonId;

    @ApiModelProperty(value = "类型", notes = "文章：1、商品：2、话题：3", required = true)
    @NotNull(message = "类型不能为空")
    private Integer bizTypeId;
}
