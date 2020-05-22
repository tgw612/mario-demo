package com.mall.discover.response.api;

import lombok.Data;

import java.io.Serializable;

/**desc:首页发现
 * author:wang.zhiliang
 * date:2018.11.22
 */
@Data
public class CommendDiscoverConfig implements Serializable {

    private Integer id;

    private String content;

    private String pictures;
}
