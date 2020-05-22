package com.mall.discover.persistence.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@Data
public class ProductArticleBO implements Serializable {
    private static final long serialVersionUID = 292343117384147035L;
    private Long relateId;
    private Long articleId;

    private String articleTitle;

    private Integer articleStatus;

    private long createTime;

    private String articleContent;

    private String articleLook;
    private Integer pictureCount;

    private Long publishTime;

    private List<String> subjectNameList;

    private Long readCount;

    private Long shareCount;

    private Long sort;
}
