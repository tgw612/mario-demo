package com.mall.discover.web.support.config;

import com.mall.discover.web.util.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * user: qiujingwang
 * date: 2016/2/29 16:34
 * 配置
 */
@ConfigurationProperties(prefix = "appconfig")
@Data
@Slf4j
public class WebAppConfig {
    private static WebAppConfig webAppConfig = null;

    public static WebAppConfig getInstance() {
        if (webAppConfig == null) {
            synchronized (WebAppConfig.class) {
                if (webAppConfig == null) {
                    webAppConfig = SpringUtil.getBean(WebAppConfig.class);
                }
            }
        }
        return webAppConfig;
    }

    //应用的环境
    private String appEnvName;
    //密码加密因子
    private String encryptKey;
}
