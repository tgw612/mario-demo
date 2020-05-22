package com.mall.discover.response.api;

import lombok.Data;
import java.io.Serializable;

@Data
public class CommendDiscoverResponse  implements Serializable {

    DiscoverBaseResponse discoverBaseResponse;

    /**
     * 当前数据版本号,格式yyyyMMddHHmmss
     */
    private String version;
}
