package com.mall.discover.persistence.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: huangzhong
 * @Date: 2019/10/9
 * @Description:
 */
@Data
public class ProductBO {
    private Long productId;

    /**
     * 商品编号
     */
    private String productNo;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 销量
     */
    private Long mallCount;

    /**
     * 佣金
     */
    private BigDecimal commission;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 图片
     */
    private String masterImg;

    /**
     * 商品二级类别
     */
    private Integer productCategory;
}
