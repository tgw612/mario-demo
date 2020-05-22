package com.mall.discover.persistence.bo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ArticleInfoBO {
    private String articleId;
    private Integer articleStatus;
    private Long createTime;
    private Long sort;
    private String articleTitle;
    private String articleContent;
    private String articleLook;
    private Long publishTime;
    private List<SubjectInfoBO> subject;
    private String productInfo;
}
