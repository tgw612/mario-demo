package com.mall.discover.request.client;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@ApiModel(value = "更新文章、话题、商品的分享次数")
@Data
public class ClientUpdateCountRequest extends CommonRequest {

    @ApiModelProperty(value = "通用id", notes = "文章id、话题id、商品id")
    private long commonId;

    @ApiModelProperty(value = "商品编号", notes = "兼容老版本")
    private String productNo;

    @ApiModelProperty(value = "类型", notes = "文章：1、商品：2、话题：3", required = true)
    @NotNull(message = "类型不能为空")
    private Integer bizTypeId;
}
