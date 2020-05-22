package com.mall.discover.persistence.bo;

import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Data
public class SubjectBO {

    /**
     * 话题ID
     */
    private Long subjectId;

    /**
     * 话题名称
     */
    private String subjectName;

    /**
     * 话题简介
     */
    private String subjectContent;

    /**
     * 话题图片信息
     */
    private String subjectLook;

    /**
     * 话题排序
     */
    private Long sort;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 文章数量
     */
    private int articleCount;

    /**
     * 阅读数
     */
    private long readCount;

    /**
     * 分享数
     */
    private long shareCount;
    /**
     * 发布时间
     */
    private Long publishTime;
    /**
     * 话题状态
     */
    private Integer subjectStatus;
}
