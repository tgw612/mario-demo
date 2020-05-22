package com.mall.discover.request.product;

import com.doubo.common.model.request.PageQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("商品列表查询")
@Data
@ToString
@EqualsAndHashCode
public class ProductListRequestDto extends PageQueryRequest {
    private static final long serialVersionUID = -54723631079375940L;

    @ApiModelProperty(value = "排序字段", notes = "暂时去掉该字段，默认ID倒序排序")
    private Integer sortField;
    @ApiModelProperty(value = "是否爆款", notes = "默认非爆款（与非高佣金同时存在时，显示所有)")
    private Boolean hot;
    @ApiModelProperty(value = "是否高佣金", notes = "默认非高佣金（与非爆款同时存在时，显示所有)")
    private Boolean highFee;
    @ApiModelProperty(value = "商品名称", notes = "模糊搜索")
    private String goodsName;
    @ApiModelProperty(value = "商品编号", notes = "模糊搜索")
    private String goodsCode;
}
