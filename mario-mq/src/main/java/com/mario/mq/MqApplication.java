package com.mario.mq;

import com.doubo.common.util.DateUtil;
import com.mario.mq.config.ServiceConfig;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created with IntelliJ IDEA. User: qiujingwang Date: 2018-9-10 Description: 程序入口
 */
@EnableAsync
@Slf4j
public class MqApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(ServiceConfig.class, args);

    printDebugInfo(ctx);
  }

  private static void printDebugInfo(ConfigurableApplicationContext ctx) {
    if ("true".equalsIgnoreCase(ctx.getEnvironment().getProperty("debug"))) {
      StringBuilder allBeanNameStr = new StringBuilder(500);
      allBeanNameStr.append("----------------- spring beans ------------------------\n");
      String[] beanNames = ctx.getBeanDefinitionNames();
      allBeanNameStr.append("beanNames 总数：" + beanNames.length + "\n");
      Arrays.sort(beanNames);
      for (String bn : beanNames) {
        allBeanNameStr.append(bn + "\n");
      }
      allBeanNameStr.append("------------------ end -----------------------");
      log.info(allBeanNameStr.toString());

    }
    String successInfo = String
        .format("########[微服务启动成功]########[%s]########", DateUtil.getNowTimeYYYYMMddHHMMSS());
    log.info(successInfo);
    System.out.println(successInfo);
  }
}
