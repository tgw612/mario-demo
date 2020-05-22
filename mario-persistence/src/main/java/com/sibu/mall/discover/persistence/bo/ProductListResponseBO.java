package com.mall.discover.persistence.bo;

import com.mall.discover.persistence.entity.product.ProductEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductListResponseBO extends ProductEntity {
    private static final long serialVersionUID = -6879771653274687345L;
    private Long sort;
    private Long readCount;
    private Long shareCount;
    private Integer articleNums;
    //根据接口获取
    private Integer goodsStatus;
}
