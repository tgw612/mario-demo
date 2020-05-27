package com.mario.service.api.config;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.vod.v20180717.VodClient;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * 腾讯云相关配置
 *
 * @author kevin.jia
 * @since 2019/8/12 18:19
 */
@Configuration
public class QcloudConfig {

  @Resource
  private QcloudConfigProperties qcloudConfigProperties;

  @Bean
  public VodClient vodClient() {
    Assert.notNull(qcloudConfigProperties.getSecretId(), "密钥ID不能为空");
    Assert.notNull(qcloudConfigProperties.getSecretKey(), "密钥KEY不能为空");
    Assert.notNull(qcloudConfigProperties.getRegion(), "区域不能为空");
    Credential credential = new Credential(qcloudConfigProperties.getSecretId(),
        qcloudConfigProperties.getSecretKey());
    return new VodClient(credential, qcloudConfigProperties.getRegion());
  }

}