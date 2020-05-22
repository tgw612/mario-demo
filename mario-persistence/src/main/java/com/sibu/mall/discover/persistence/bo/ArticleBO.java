package com.mall.discover.persistence.bo;

import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/10/9
 * @Description:
 */
@Data
public class ArticleBO {

    private Long articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 图片音频等集合
     */
    private String articleLook;

    /**
     * 阅读次数
     */
    private long readCount;

    /**
     * 排序
     */
    private Long sort;
}
