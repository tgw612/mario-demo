package com.mario.persistence.support;

import com.mario.persistence.util.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * user: qiujingwang date: 2016/2/29 16:34 配置
 */
@ConfigurationProperties(prefix = "appconfig")
@Data
@Slf4j
public class AppConfig {

  private static AppConfig appConfig = null;

  //应用的环境
  private String appEnvName;
  //密码加密因子
  private String encryptKey;
  //真实节点
  private Integer shardingRealCount;
  //分片虚拟节点数
  private Integer shardingVirtualCount;
  //分库数
  private Integer dbShardingCount;
  //分表数
  private Integer tbShardingCount;

  public static AppConfig getInstance() {
    if (appConfig == null) {
      synchronized (AppConfig.class) {
        if (appConfig == null) {
          appConfig = SpringUtil.getBean(AppConfig.class);
        }
      }
    }
    return appConfig;
  }
}
