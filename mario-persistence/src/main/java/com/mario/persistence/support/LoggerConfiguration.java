package com.mario.persistence.support;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Component;

/**
 * @author qiujingwang
 * @version 1.0
 * @date 2018/09/19 上午9:48
 * @Description: 动态调整日志级别
 */
@Component
@Slf4j
public class LoggerConfiguration {

  private static final String LOGGER_TAG = "log.log-level";

  @Autowired
  private LoggingSystem loggingSystem;

  @ApolloConfig
  private Config config;

  private static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
      return false;
    }
    int len = searchStr.length();
    int max = str.length() - len;
    for (int i = 0; i <= max; i++) {
      if (str.regionMatches(true, i, searchStr, 0, len)) {
        return true;
      }
    }
    return false;
  }

  @ApolloConfigChangeListener(value = {"application"}, interestedKeys = {"log.log-level"})
  private void onChange(ConfigChangeEvent changeEvent) {
    refreshLoggingLevels();
  }

  private void refreshLoggingLevels() {
    Set<String> keyNames = config.getPropertyNames();
    for (String key : keyNames) {
      if (containsIgnoreCase(key, LOGGER_TAG)) {
        String strLevel = config.getProperty(key, "info");
        LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
        loggingSystem.setLogLevel("com", level);
        loggingSystem.setLogLevel(null, level);
        log.info("set log property[{}]=[{}]", key, strLevel);
      }
    }
  }
}
