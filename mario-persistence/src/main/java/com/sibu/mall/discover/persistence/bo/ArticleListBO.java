package com.mall.discover.persistence.bo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ArticleListBO {
    private Long articleId;
    private Integer articleStatus;
    private Long createTime;
    private String articleTitle;
    private String articleContent;
    private String articleLook;
    private Integer pictureNums;
    private List<SubjectInfoBO> subject;
    private List<ProductIdNameBO> product;
    private Long readCount;
    private Long shareCount;
    private Long articleSort;
    private Long total;
    private Long publishTime;
    private ArticleLook articleLooks;
    private Integer likeCount;
}
