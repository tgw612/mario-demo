package com.mall.discover.persistence.entity.mysql;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Data
public class DiscoverCountEntity implements Serializable {
    private static final long serialVersionUID = 6685609415597945985L;
    /**
     * 通用id
     */
    private Long bizId;

    /**
     * 通用类型
     */
    private Integer bizType;

    /**
     * 阅读次数
     */
    private long readCount;

    /**
     * 分享次数
     */
    private long shareCount;

    /**
     * 排序
     */
    private long sort;

    /**
     * 客户端 排序值-X
     */
    private long sortClient;

    /**
     * 创建时间
     */
    private long createTime;


    /**
     * 更新时间
     */
    private long updateTime;
}
