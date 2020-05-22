package com.mall.discover.persistence.entity.mysql;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/10/31
 * @Description:
 */
@Accessors(chain = true)
@Data
public class RelationsEntity implements Serializable {
    private static final long serialVersionUID = 2014764067329788193L;

    private Long id;
    /**
     * (1,"商品-文章"),
     * (2,"话题-文章"),
     * (3,"商品-话题"),
     * (4,"用户-点赞文章"),
     */
    private Integer relationType;

    /**
     * 来源id
     */
    private Long fromId;

    /**
     * 目标id
     */
    private Long toId;

    /**
     * 统计值
     */
    private long count;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 更新时间
     */
    private long updateTime;

    /**
     * 创建人id
     */
    private String createUserId;
}
