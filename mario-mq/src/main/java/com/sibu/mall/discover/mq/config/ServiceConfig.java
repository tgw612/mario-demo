package com.mall.discover.mq.config;

import com.ctrip.framework.apollo.spring.boot.ApolloAutoConfiguration;
import com.doubo.ali.mq.autoconfigure.AliMqAutoConfiguration;
import com.doubo.boot.autoimport.CustomImportAutoConfiguration;
import com.doubo.json.spring.FastJsonConfigBean;
import com.mall.discover.mq.util.SpringUtil;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.apache.dubbo.spring.boot.autoconfigure.DubboRelaxedBinding2AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018-9-10
 * Description:
 */
@CustomImportAutoConfiguration({
//        ServletWebServerFactoryAutoConfiguration.class,
//        EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
//        AopAutoConfiguration.class,
        DubboRelaxedBinding2AutoConfiguration.class,
        DubboAutoConfiguration.class,
        AliMqAutoConfiguration.class,
        ApolloAutoConfiguration.class,
//        CuratorZookeeperRegCenterAutoConfiguration.class
})
@EnableConfigurationProperties(MqAppConfig.class)
@ComponentScan(value = "com.mall.discover.mq")
public class ServiceConfig {


    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    public FastJsonConfigBean fastJsonConfig() {
        FastJsonConfigBean fastJsonConfig = new FastJsonConfigBean();
        fastJsonConfig.setEnableDefault(true);
        fastJsonConfig.setEnableJsonUtil(true);
        return fastJsonConfig;
    }
}
