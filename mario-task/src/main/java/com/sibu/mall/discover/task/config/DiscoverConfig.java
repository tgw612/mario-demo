package com.mall.discover.task.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.mall.discover.task.config.properties.DiscoverProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: huangzhong
 * @Date: 2019/10/6
 * @Description:  动态更改发现页配置参数
 */
@Component
@Slf4j
public class DiscoverConfig {

    /**
     * 缓存最大页数
     */
    private static String  CACHE_MAX_PAGE = "discover.params.cacheMaxPage";

    @Autowired
    private DiscoverProperties discoverProperties;

    @ApolloConfig
    private Config config;

    @ApolloConfigChangeListener()
    private void onChange(ConfigChangeEvent changeEvent) {
        //缓存最大页数
        String cacheMaxPage = config.getProperty(CACHE_MAX_PAGE, "5");

        discoverProperties.setCacheMaxPage(Integer.valueOf(cacheMaxPage));
    }
}
