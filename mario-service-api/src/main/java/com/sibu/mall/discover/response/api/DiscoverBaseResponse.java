package com.mall.discover.response.api;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 发现基础响应类
 */
@Data
public class DiscoverBaseResponse implements Serializable {

    private Integer platform;

    private List<CommendDiscoverConfig> configs;

}
