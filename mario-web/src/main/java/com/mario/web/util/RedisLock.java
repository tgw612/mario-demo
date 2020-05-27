package com.mario.web.util;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author lhh
 * @Date 2019/3/7 0007
 */
@Slf4j
@Component
public class RedisLock {

  @CreateCache(
      area = RedisConstant.Area.DISCOVER,
      name = RedisConstant.Key.LOCK_PRE,
      cacheType = CacheType.REMOTE)
  private Cache<String, Object> userCache;

  /**
   * 加锁
   *
   * @param key
   * @return
   */
  public boolean lock(String key) {
    try {
      while (true) {
        AutoReleaseLock lock = userCache.tryLock(key, 2, TimeUnit.SECONDS);
        if (lock != null) {
//                    log.info("加锁key:{}", key);
          return true;
        }
        Thread.sleep(100);
      }
    } catch (Exception e) {
      log.error("加锁异常key:{},{}", key, e.getMessage());
    }
    return false;
  }

  /**
   * 解锁
   *
   * @param key
   */
  public boolean unlock(String key) {
    try {
      boolean result = userCache.remove(key);
      log.debug("解锁key:{},result:{}", key, result);
      return result;
    } catch (Exception e) {
      log.error("解锁异常key:{},{}", key, e.getMessage());
    }
    return false;
  }


  /**
   * 生成key
   */
  public String getKey(Object... ids) {
    if (ids == null || ids.length == 0) {
      return "discover_default";
    }
    int length = ids.length;
    StringBuilder sb = new StringBuilder();
    sb.append(ids[0]);
    for (int i = 1; i < length; i++) {
      sb.append("_").append(ids[i]);
    }
    return sb.toString();
  }

}
