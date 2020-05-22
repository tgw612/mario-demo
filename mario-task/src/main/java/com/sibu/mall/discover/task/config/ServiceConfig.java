package com.mall.discover.task.config;

import com.ctrip.framework.apollo.spring.boot.ApolloAutoConfiguration;
import com.doubo.boot.autoimport.CustomImportAutoConfiguration;
import com.mall.discover.task.util.SpringUtil;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.apache.dubbo.spring.boot.autoconfigure.DubboRelaxedBinding2AutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018-9-10
 * Description:
 */
@CustomImportAutoConfiguration({
//        ServletWebServerFactoryAutoConfiguration.class,
//        EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
        AopAutoConfiguration.class,
//        ShardingJdbcBootConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        TransactionAutoConfiguration.class,
//        MybatisAutoConfiguration.class,
//        RedisAutoConfiguration.class,
        DubboRelaxedBinding2AutoConfiguration.class,
        DubboAutoConfiguration.class,
        ApolloAutoConfiguration.class

})
@EnableScheduling
@EnableConfigurationProperties(com.mall.discover.task.config.TaskAppConfig.class)
@ComponentScan(value = "com.mall.discover.task")
public class ServiceConfig {

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }
}
