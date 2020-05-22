package com.mall.discover.persistence.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/10/9
 * @Description:
 */
@Data
public class ArticleVo extends BaseVo {
    private Long articleId;
    /**
     * 通用类型
     */
    private Integer bizType;
    /**
     *  通用关联关系
     */
    private Integer relation;
    /**
     *  look类型集合
     */
    private List<Integer> lookTypeList;
}
