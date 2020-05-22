package com.mall.discover.persistence.entity.product;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.mall.discover.persistence.entity.article.CommonEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("discover_product")
public class ProductEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = -8645222107662909683L;
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;
    @TableField("product_no")
    private String productNo;
    @TableField("product_name")
    private String productName;
    @TableField("mall_count")
    private Integer mallCount;
    @TableField("commission")
    private BigDecimal commission;
    @TableField("price")
    private BigDecimal price;
    @TableField("hot_product_status")
    private Integer hotProductStatus;
    @TableField("product_category")
    private Integer productCategory;
    @TableField("high_commission_status")
    private Integer highCommissionStatus;
    @TableField("master_img")
    private String masterImg;
    @TableField("product_status")
    private Integer productStatus;
    //一级分类
    @TableField("category_id_first")
    private Integer categoryIdFirst;
    //二级分类
    @TableField("category_id_second")
    private Integer categoryIdSecond;
}
