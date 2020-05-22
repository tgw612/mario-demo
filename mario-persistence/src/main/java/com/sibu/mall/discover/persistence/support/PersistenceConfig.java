package com.mall.discover.persistence.support;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.doubo.common.crypto.PasswordDecrypter;
import com.doubo.common.hash.StringConsistentHashCoding;
import com.doubo.common.interfaces.StringHashCoding;
import com.mall.common.algorithms.ShardingDBAlgorithms;
import com.mall.common.algorithms.ShardingTBAlgorithms;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2018-03-15
 * Description:
 */
@Configuration
@MapperScan("com.mall.discover.persistence.dao.mysql")
public class PersistenceConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 设置mybatis 日志
     *
     * @return
     */
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return (configuration) -> {
            configuration.setLogImpl(Slf4jImpl.class);
        };
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        // return new MyBatisInterceptor();
        return new PaginationInterceptor();
    }

    /**
     * Mybatis-plus-baomidou
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        globalConfiguration.setIdType(IdType.AUTO.getKey());
        globalConfiguration.setDbColumnUnderline(true);

        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfiguration);
        mybatisSqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        mybatisSqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));
        mybatisSqlSessionFactoryBean.setPlugins(new Interceptor[]{paginationInterceptor()});
        return mybatisSqlSessionFactoryBean;
    }

    /**
     * 密码解密
     * @return
     */
    @Bean
    public PasswordDecrypter passwordDecrypter() {
        return new PasswordDecrypter(appConfig.getEncryptKey());
    }

    /**
     * 分库负载算法
     *
     * @return
     */
    @Bean
    public StringHashCoding dataBaseHashCoding() {
        StringConsistentHashCoding dataBaseHashCoding = new StringConsistentHashCoding(appConfig.getShardingRealCount(), appConfig.getShardingVirtualCount());
        //设置静态分库负载算法
        ShardingDBAlgorithms.setDataBaseHashCoding(dataBaseHashCoding);
        return dataBaseHashCoding;
    }
    /**
     * 分表负载算法
     *
     * @return
     */
    @Bean
    public StringHashCoding tableHashCoding() {
        StringConsistentHashCoding tableHashCoding = new StringConsistentHashCoding(appConfig.getShardingVirtualCount(), appConfig.getShardingRealCount());
        //设置静态分表负载算法
        ShardingTBAlgorithms.setTableHashCoding(tableHashCoding);
        return tableHashCoding;
    }
}
