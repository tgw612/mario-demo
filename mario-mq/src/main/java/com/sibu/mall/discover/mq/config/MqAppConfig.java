package com.mall.discover.mq.config;

import com.mall.discover.mq.util.SpringUtil;
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
public class MqAppConfig {
    private static MqAppConfig mqAppConfig = null;

    public static MqAppConfig getInstance() {
        if (mqAppConfig == null) {
            synchronized (MqAppConfig.class) {
                if (mqAppConfig == null) {
                    mqAppConfig = SpringUtil.getBean(MqAppConfig.class);
                }
            }
        }
        return mqAppConfig;
    }

    //应用的环境
    private String appEnvName;
    //密码加密因子
    private String encryptKey;
}
