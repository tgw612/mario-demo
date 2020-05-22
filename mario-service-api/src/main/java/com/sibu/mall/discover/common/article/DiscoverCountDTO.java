package com.mall.discover.common.article;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class DiscoverCountDTO implements Serializable {
    private static final long serialVersionUID = -8724054367629750106L;
    private Long id;
    private Long sortClient;
}
