package com.mall.discover.request.product;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("修改商品信息值")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductEditRequestDto extends CommonRequest {
    private static final long serialVersionUID = -2188078681137738115L;
    @NotNull(message = "商品id不能为空")
    @ApiModelProperty(value = "商品ID", notes = "",required = true)
    private String goodsId;
    @ApiModelProperty(value = "是否爆款", notes = "是否爆款和是否高佣金都没有值,直接返回")
    private Boolean hot;
    @ApiModelProperty(value = "是否高佣金", notes = "是否爆款和是否高佣金都没有值,直接返回")
    private Boolean highFee;
}