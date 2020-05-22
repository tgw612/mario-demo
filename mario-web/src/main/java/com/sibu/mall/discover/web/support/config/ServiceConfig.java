package com.mall.discover.web.support.config;

import com.ctrip.framework.apollo.spring.boot.ApolloAutoConfiguration;
import com.doubo.ahas.autoconfigure.AhasAutoConfiguration;
import com.doubo.boot.autoimport.CustomImportAutoConfiguration;
import com.doubo.redis.serializer.MyGenericFastjsonRedisSerializer;
import com.doubo.redis.template.RedisTemplate;
import com.mall.discover.web.util.SpringUtil;
import com.mall.user.token.configure.UserAccessTokenAutoConfiguration;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.apache.dubbo.spring.boot.autoconfigure.DubboRelaxedBinding2AutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018-03-19
 * Description:
 */
@EnableConfigurationProperties(WebAppConfig.class)
@CustomImportAutoConfiguration({
        AopAutoConfiguration.class,
        DubboRelaxedBinding2AutoConfiguration.class,
        DubboAutoConfiguration.class,
        ApolloAutoConfiguration.class,
        UserAccessTokenAutoConfiguration.class,
        RedisAutoConfiguration.class,
        AhasAutoConfiguration.class
})
@ComponentScan(value = "com.mall.discover.web")
public class ServiceConfig {

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
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
