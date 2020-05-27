package com.mario.web.support.config;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.redis.lettuce.RedisLettuceCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import com.alicp.jetcache.support.JavaValueDecoder;
import com.alicp.jetcache.support.JavaValueEncoder;
import com.mall.discover.common.constants.RedisConstant;
import com.mario.web.support.config.properties.DiscoverRedisProperties;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCreateCacheAnnotation
@EnableConfigurationProperties({
    DiscoverRedisProperties.class})
public class JetCacheConfig {

  @Autowired
  private DiscoverRedisProperties discoverRedisProperties;

  @Bean(destroyMethod = "shutdown")
  public RedisClient discoverRedisClient() {
    RedisURI redisURI = RedisURI.builder()
        .withDatabase(0)
        .withHost(discoverRedisProperties.getHost())
        .withPort(discoverRedisProperties.getPort())
        .withPassword(discoverRedisProperties.getPassword())
        .withTimeout(discoverRedisProperties.getTimeout())
        .build();
    RedisClient redisClient = RedisClient.create(redisURI);
    redisClient.setOptions(ClientOptions.builder()
        .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
        .build()
    );
    return redisClient;
  }

  @Bean
  public SpringConfigProvider springConfigProvider() {
    return new SpringConfigProvider();
  }

  @Bean
  public GlobalCacheConfig config(SpringConfigProvider configProvider) {
    //本地缓存配置
    Map localBuilders = new HashMap();

    //远程缓存配置
    Map remoteBuilders = new HashMap();
    //首页
    remoteBuilders.put(RedisConstant.Area.DISCOVER, this.createDiscoverRemoteCache());

    GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
    globalCacheConfig.setConfigProvider(configProvider);
    //本地缓存 一级缓存
    globalCacheConfig.setLocalCacheBuilders(localBuilders);
    //远程缓存 二级缓存
    globalCacheConfig.setRemoteCacheBuilders(remoteBuilders);
    //不开启统计
    globalCacheConfig.setStatIntervalMinutes(0);
    globalCacheConfig.setAreaInCacheName(false);
    //穿透保护
    globalCacheConfig.setPenetrationProtect(true);
    return globalCacheConfig;
  }

  /**
   * 创建Statistics 区域 远程缓存配置
   *
   * @return
   */
  private RedisLettuceCacheBuilder createDiscoverRemoteCache() {
    RedisLettuceCacheBuilder hPageRemoteCacheBuilder = RedisLettuceCacheBuilder
        .createRedisLettuceCacheBuilder()
        .keyConvertor(FastjsonKeyConvertor.INSTANCE)
        .valueEncoder(JavaValueEncoder.INSTANCE)
        .valueDecoder(JavaValueDecoder.INSTANCE)
        .redisClient(this.discoverRedisClient());
    return hPageRemoteCacheBuilder;
  }
}