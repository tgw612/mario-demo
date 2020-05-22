package com.mall.discover.response.article;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleRelateProduct implements Serializable {
    private static final long serialVersionUID = 334139373662479736L;
    private Long productId;

    private String productName;
}
