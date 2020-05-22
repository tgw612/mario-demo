package com.mall.discover.config;

import com.ctrip.framework.apollo.spring.boot.ApolloAutoConfiguration;
import com.doubo.ahas.autoconfigure.AhasAutoConfiguration;
import com.doubo.ali.mq.autoconfigure.AliMqAutoConfiguration;
import com.doubo.boot.autoimport.CustomImportAutoConfiguration;
import com.doubo.redis.serializer.MyGenericFastjsonRedisSerializer;
import com.doubo.redis.template.RedisTemplate;
import com.doubo.shardingjdbc.autoconfigure.ShardingJdbcBootConfiguration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.discover.common.validator.bean.CustomValidatorFactoryBean;
import com.mall.discover.persistence.support.AppConfig;
import com.mall.discover.persistence.util.SpringUtil;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.apache.dubbo.spring.boot.autoconfigure.DubboRelaxedBinding2AutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;

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
        DataSourceTransactionManagerAutoConfiguration.class,
        TransactionAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        DubboRelaxedBinding2AutoConfiguration.class,
        DubboAutoConfiguration.class,
        ApolloAutoConfiguration.class,
        ShardingJdbcBootConfiguration.class,
        RedisAutoConfiguration.class,
        AliMqAutoConfiguration.class,
        AhasAutoConfiguration.class
})
@EnableConfigurationProperties(AppConfig.class)
@ComponentScan(value = "com.mall.discover")
public class ServiceConfig {

    @Bean
    public CustomValidatorFactoryBean localValidator() {
        CustomValidatorFactoryBean customValidator = new CustomValidatorFactoryBean();
        customValidator.setProviderClass(org.hibernate.validator.HibernateValidator.class);
        customValidator.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
        return customValidator;
    }

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        MyGenericFastjsonRedisSerializer genericFastJsonRedisSerializer = new MyGenericFastjsonRedisSerializer();
        template.setHashKeySerializer(genericFastJsonRedisSerializer);
        template.setValueSerializer(genericFastJsonRedisSerializer);
        template.setEnableRedisTemplateUtil(true);
        return template;
    }

}
