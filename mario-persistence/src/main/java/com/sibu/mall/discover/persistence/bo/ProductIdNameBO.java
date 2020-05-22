package com.mall.discover.persistence.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductIdNameBO implements Serializable {
    private static final long serialVersionUID = 8653928969881584585L;
    private Long productId;

    private String productName;
}
