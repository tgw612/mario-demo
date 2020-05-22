package com.mall.discover.task.config;

import com.mall.discover.task.util.SpringUtil;
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
public class TaskAppConfig {
    private static TaskAppConfig taskAppConfig = null;

    public static TaskAppConfig getInstance() {
        if (taskAppConfig == null) {
            synchronized (TaskAppConfig.class) {
                if (taskAppConfig == null) {
                    taskAppConfig = SpringUtil.getBean(TaskAppConfig.class);
                }
            }
        }
        return taskAppConfig;
    }

    //应用的环境
    private String appEnvName;
    //密码加密因子
    private String encryptKey;
}
