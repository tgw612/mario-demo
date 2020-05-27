package com.mario.persistence.support.cache.decoder;

import com.alicp.jetcache.CacheValueHolder;
import com.mall.common.Empty;
import java.io.UnsupportedEncodingException;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: Author: 陈二伟 Date:2018/10/31
 */
@Slf4j
public class ValueDecoder implements Function<byte[], Object> {

  public static final ValueDecoder INSTANCE = new ValueDecoder();

  @Override
  public Object apply(byte[] bytes) {
    try {
      CacheValueHolder v = new CacheValueHolder();
      //由于jetCache 必须要设置过期时间 但是这些数据 又不是过期数据，所以只能将过期时间设置很大 下面为68年后过期.
      v.setExpireTime(2147483647000l);
      String value = new String(bytes, "UTF-8");
      if ("null".equals(value)) {//为了解决雪崩效应 虽然使用JetCache时 保存的是Empty 但是 实际存储的是null.
        v.setValue(Empty.empty);
      } else {
        v.setValue(value);
      }
      return v;
    } catch (UnsupportedEncodingException e) {
      log.error("错误", e);
      throw new RuntimeException("错误啊");
    }
  }
}
