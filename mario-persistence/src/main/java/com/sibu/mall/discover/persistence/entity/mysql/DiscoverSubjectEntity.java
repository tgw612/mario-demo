package com.mall.discover.persistence.entity.mysql;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Data
public class DiscoverSubjectEntity implements Serializable {
    /**
     * 话题主键
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
     * 话题图片、音频等汇总信息
     */
    private String subjectLookString;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 更新时间
     */
    private long updateTime;
    /**
     * 话题发布时间
     */
    private long publishTime;
    /**
     * 话题状态：1-草稿 2-已发布;3-待发布
     */
    private Integer subjectStatus;
}
