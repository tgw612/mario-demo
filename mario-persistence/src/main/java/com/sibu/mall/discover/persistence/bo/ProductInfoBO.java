package com.mall.discover.persistence.bo;

import com.mall.discover.persistence.entity.product.ProductEntity;
import lombok.Data;

@Data
public class ProductInfoBO extends ProductEntity {
    private static final long serialVersionUID = 6663334403988458313L;
    private Long sort;
    private Long readCount;
    private Long shareCount;
    private Integer articleNums;
//    //根据接口获取
//    private Integer productStatus;
}
