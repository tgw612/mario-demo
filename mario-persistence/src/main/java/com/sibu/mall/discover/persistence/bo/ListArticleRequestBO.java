package com.mall.discover.persistence.bo;

import lombok.Data;

@Data
public class ListArticleRequestBO {
    private String sortField;
    private String articleId;
    private String articleContent;
    private String sortOrder;
}
