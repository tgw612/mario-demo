package com.mall.discover.persistence.bo;

import lombok.Data;

@Data
public class CountReadShareUpdateBO {
    private Long bizId;
    private Long id;
    private Integer biztype;
    private Long readCount;
    private Long shareCount;
}
