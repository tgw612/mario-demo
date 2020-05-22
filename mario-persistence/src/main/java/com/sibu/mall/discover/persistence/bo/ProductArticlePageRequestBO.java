package com.mall.discover.persistence.bo;

import com.doubo.common.model.request.PageQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class ProductArticlePageRequestBO extends PageQueryRequest {
    private static final long serialVersionUID = -2448575409010948958L;
    private Long articleId;

    private String articleContent;

    private Long productId;
}
