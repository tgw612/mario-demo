package com.mall.discover.task.config.properties;

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
     * 缓存最大页数
     */
    private Integer cacheMaxPage = 5;
}
