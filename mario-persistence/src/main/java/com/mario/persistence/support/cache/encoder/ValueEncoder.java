package com.mario.persistence.support.cache.encoder;

import com.alicp.jetcache.CacheValueHolder;
import com.mall.common.Empty;
import java.io.UnsupportedEncodingException;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: Author: 陈二伟 Date:2018/10/31
 */
@Slf4j
public class ValueEncoder implements Function<Object, byte[]> {

  public static final ValueEncoder INSTANCE = new ValueEncoder();

  @Override
  public byte[] apply(Object o) {
    try {
      CacheValueHolder v = (CacheValueHolder) o;
      if (Empty.class.isInstance(v.getValue())) {
        return "null".getBytes("UTF-8");
      } else {
        return String.valueOf(v.getValue()).getBytes("UTF-8");
      }
    } catch (UnsupportedEncodingException e) {
      log.error("错误", e);
      throw new RuntimeException("错误啊");
    }
  }
}
