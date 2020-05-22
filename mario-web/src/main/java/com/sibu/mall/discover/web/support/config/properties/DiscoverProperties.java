package com.mall.discover.web.support.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: huangzhong
 * @Date: 2019/10/6
 * @Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "discover.params")
public class DiscoverProperties {

    /**
     * h5链接域名
     */
    private String link = "https://find.m.weilaijishi.cn";

    /**
     * 发现页开关：大于零为开
     */
    private Integer discoverSwitch = 1;

    /**
     * 详情页点赞头像显示数量
     */
    private Integer iconUrlMaxSize = 6;
}
