package com.mall.discover.web.support.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.mall.discover.web.support.config.properties.DiscoverProperties;
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
     * h5链接域名
     */
    private static String LINK = "discover.params.link";

    /**
     * 发现页开关
     */
    private static String DISCOVER_SWITCH = "discover.params.discoverSwitch";

    /**
     * 详情页点赞头像显示数量
     */
    private static String DISCOVER_ICON_URL_MAX_SIZE = "discover.params.iconUrlMaxSize";


    @Autowired
    private DiscoverProperties discoverProperties;

    @ApolloConfig
    private Config config;

    @ApolloConfigChangeListener()
    private void onChange(ConfigChangeEvent changeEvent) {
        //h5链接地址
        String link = config.getProperty(LINK, "https://find.m.weilaijishi.cn");
        //发现页开关
        String discoverSwitch = config.getProperty(DISCOVER_SWITCH, "1");
        //详情点赞头像最大数量
        String iconUrlMaxSize = config.getProperty(DISCOVER_ICON_URL_MAX_SIZE, "6");
        discoverProperties.setLink(link);
        discoverProperties.setDiscoverSwitch(Integer.valueOf(discoverSwitch));
        discoverProperties.setIconUrlMaxSize(Integer.valueOf(iconUrlMaxSize));
    }
}
