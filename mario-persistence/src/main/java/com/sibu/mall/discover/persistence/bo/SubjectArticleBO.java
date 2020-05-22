package com.mall.discover.persistence.bo;

import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/10/6
 * @Description:
 */
@Data
public class SubjectArticleBO {
    private Long relateId;

    private Long articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * '文章状态：1-草稿 2-已发布',
     */
    private Integer articleStatus;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 包含文章照片URL等信息
     */
    private String articleLook;

    /**
     * 定时发布时间
     */
    private Long publishTime;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 阅读数
     */
    private long readCount;

    /**
     * 分享数
     */
    private long shareCount;

    /**
     * 创建时间
     */
    private long createTime;
}
