package com.mall.discover.request.product;

import com.mall.search.response.ActiveItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString(callSuper = true)
public class SearchPageProductResponse implements Serializable {

    private static final long serialVersionUID = 8543772437202611776L;
    /**
     * 商品的分类关系的名称集合（最细粒度的分类粒度）
     */
    @ApiModelProperty("商品的分类关系的名称集合（最细粒度的分类粒度）")
    String categoryName;
    /**
     * 商品销量（包含 虚拟销量 + 实际销量）
     */
    @ApiModelProperty("商品销量（包含 虚拟销量 + 实际销量）")
    Integer sales;
    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    String brandName;
    /**
     * 该SPU隶属于的店铺id
     */
    @ApiModelProperty("该SPU隶属于的店铺id")
    Integer shopId;
    /**
     * 该SPU隶属于的店铺名称
     */
    @ApiModelProperty("该SPU隶属于的店铺名称")
    String shopName;

    /**
     * 佣金（采用基础会员关系的佣金）
     */
    @ApiModelProperty("佣金（采用基础会员关系的佣金）")
    BigDecimal commission;
    /**
     * 商品的id
     */
    @ApiModelProperty("商品的id")
    Integer id;
    /**
     * 商品的名称
     */
    @ApiModelProperty("商品的名称")
    String name;
    /**
     * 商品编码【SPU】
     */
    @ApiModelProperty("商品编码【SPU】")
    String code;
    /**
     * 商品的价格（作为普通商品的时候的价格）
     */
    @ApiModelProperty("商品的价格（作为普通商品的时候的价格）")
    BigDecimal price;
    /**
     * 商品的市场价格
     */
    @ApiModelProperty("商品的市场价格")
    BigDecimal marketPrice;
    /**
     * 商品的分类id（最细粒度的分类）
     */
    @ApiModelProperty("商品的分类id（最细粒度的分类）")
    Integer categoryId;
    /**
     * 商品的状态值（枚举类型值：ProductStateEnum）
     */
    @ApiModelProperty(" 商品的状态值（枚举类型值：ProductStateEnum）")
    Integer status;
    /**
     * 主图（eg：https://domain/xx/xx.jpg）
     */
    @ApiModelProperty("主图（eg：https://domain/xx/xx.jpg）")
    String masterImg;
    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    Integer brandId;

    /**
     * 商品的类型（基于Product 的ProductTypeEnum的枚举）
     */
    @ApiModelProperty("商品的类型（基于Product 的ProductTypeEnum的枚举）")
    Integer type;

    /**
     * 商品业务类别(0.普通商品(默认);1.全球购)
     */
    @ApiModelProperty("商品业务类别(0.普通商品(默认);1.全球购)")
    private Integer saleType;

    /**
     * 商品编号
     */
    @ApiModelProperty("商品编号")
    private String productNum;

    /**
     * 商品参加的活动集合（商品支持同时参加多个活动）
     * 普通商品的状态下 该数据项为null，只有在该商品参加某些活动的时候，才会有该数据项
     */
    @ApiModelProperty("商品参加的活动集合（商品支持同时参加多个活动）普通商品的状态下 该数据项为null，只有在该商品参加某些活动的时候，才会有该数据项")
    List<ActiveItem> actives;

    /**
     * 一级分类
     */
    @ApiModelProperty("一级分类")
    Integer categoryIdFirst;

    /**
     * 二级分类
     */
    @ApiModelProperty("二级分类")
    Integer categoryIdSecond;
}
