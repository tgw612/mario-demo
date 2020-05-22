package com.mall.discover.response.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class ProductInfoResponse implements Serializable {
    private static final long serialVersionUID = 6911154189331124559L;
    @ApiModelProperty(value = "商品id", notes = "")
    private Integer pid;
    @ApiModelProperty(value = "商品编号", notes = "")
    private String productNum;
    @ApiModelProperty(value = "商品名称", notes = "")
    private String name;
    @ApiModelProperty(value = "价格", notes = "")
    private BigDecimal price;
    @ApiModelProperty(value = "销量 商品销量（包含 虚拟销量 + 实际销量）", notes = "商品销量（包含 虚拟销量 + 实际销量）")
    private Integer sales;
    @ApiModelProperty(value = "佣金", notes = "")
    private BigDecimal commission;
    @ApiModelProperty(value = "排序序号 当前字段在前台的排序", notes = "当前字段在前台的排序")
    private Long sort;
    @ApiModelProperty(value = "是否爆款", notes = "")
    private Boolean hot;
    @ApiModelProperty(value = "是否高佣金", notes = "")
    private Boolean highFee;
    @ApiModelProperty(value = "阅读数 此商品的阅读数", notes = "此商品的阅读数")
    private Long readNums;
    @ApiModelProperty(value = "文章数量 此商品所有的文章总量", notes = "此商品所有的文章总量")
    private Integer articleNums;
    @ApiModelProperty(value = "分享次数  此商品的分享次数", notes = "此商品的分享次数")
    private Long shareNums;
    @ApiModelProperty("商品的状态值（枚举类型值：ProductStateEnum 1.刚创建 2.提交审核 3.审核通过 4申请驳回 5商品删除 6上架 7下架 8待设置价格 9申诉下架）")
    private Integer status;
    @ApiModelProperty("商品主图（eg：https://domain/xx/xx.jpg）")
    private String masterImg;
    @ApiModelProperty("商品的二级类别,C端")
    private Integer categoryId;
}
