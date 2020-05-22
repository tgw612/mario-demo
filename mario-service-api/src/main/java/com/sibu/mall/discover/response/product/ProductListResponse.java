package com.mall.discover.response.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ProductListResponse {
    @ApiModelProperty(value = "商品ID", notes = "")
    private Integer goodsId;
    @ApiModelProperty(value = "排序字段", notes = "")
    private String sortField;
    @ApiModelProperty(value = "商品编号", notes = "")
    private String goodsCode;
    @ApiModelProperty(value = "商品图片", notes = "")
    private List<String> goodsPictures;
    @ApiModelProperty(value = "商品名称", notes = "")
    private String goodsName;
    @ApiModelProperty(value = "价格", notes = "")
    private Double price;
    @ApiModelProperty(value = "商品状态", notes = "")
    private Integer goodsStatus;
    @ApiModelProperty(value = "销量", notes = "")
    private Integer sellNums;
    @ApiModelProperty(value = "佣金", notes = "")
    private Double fee;
    @ApiModelProperty(value = "文章数量", notes = "")
    private Integer articleNums;
    @ApiModelProperty(value = "是否爆款", notes = "")
    private Boolean hot;
    @ApiModelProperty(value = "是否高佣金", notes = "")
    private Boolean highFee;
    @ApiModelProperty(value = "总条数", notes = "")
    private Long total;
}
