package com.mall.discover.response.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class DiscoverCountListResponse implements Serializable {
    private static final long serialVersionUID = 7198778836691896660L;
    private Long id;

    private Long bizId;

    private Long readCount;

    private Long shareCount;

    private Long sortClient;

    private Long sort;

    private Integer biztype;

    private Long createTime;
}
