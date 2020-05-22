package com.mall.discover.persistence.vo;

import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Data
public class SubjectVo extends BaseVo {

    /**
     * 话题id
     */
    private Long subjectId;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 话题名称
     */
    private String subjectName;

    /**
     * 通用类型
     */
    private Integer bizType;

    /**
     *  通用关联关系
     */
    private Integer relation;
}
