package com.mall.discover.persistence.bo;

import lombok.Data;

@Data
public class ProductListRequestBO {
    private Boolean hot;
    private Boolean highFee;
    private String goodsName;
    private String goodsCode;
}
