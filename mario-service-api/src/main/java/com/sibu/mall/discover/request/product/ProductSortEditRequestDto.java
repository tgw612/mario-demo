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
public class ProductSortEditRequestDto extends CommonRequest {
    private static final long serialVersionUID = -2188078681137738115L;
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id包含文章;商品;话题", notes = "", required = true)
    private Long id;
    @ApiModelProperty(value = "排序值", required = true)
    private Long sort;
    @ApiModelProperty(value = "id类型1:文章 2:商品 3:话题", notes = "", required = true)
    private Integer biztype;
}
