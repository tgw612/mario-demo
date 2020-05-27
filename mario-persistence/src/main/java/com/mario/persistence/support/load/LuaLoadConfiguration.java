package com.mario.persistence.support.load;

import com.mall.discover.common.util.LuaUtils;
import io.lettuce.core.RedisClient;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.BuilderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

/**
 * Description: Author: 陈二伟 Date:2018/10/31
 */
@Component
@Slf4j
public class LuaLoadConfiguration {

  @Autowired
  private RedisClient redisClient;

  @Autowired
  private Environment environment;


  /**
   * 加载配置
   */
  @PostConstruct
  public void init() throws IOException {
    String init = environment.getProperty(LuaUtils.LUA_SWITCH_INIT);
    if ("true".equals(init)) {
      String luaPath = environment.getProperty(LuaUtils.LUA_PATH);
      if (StringUtils.isBlank(luaPath)) {
        throw new BuilderException(LuaUtils.LUA_PATH + "属性 不存在");
      }

      PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      Resource[] resources = resolver.getResources(luaPath);
      if (resources != null) {
        for (int i = 0, len = resources.length; i < len; i++) {
          Resource resource = resources[i];
          //名称
          String name = resource.getFilename();
          //内容
          String content = FileCopyUtils
              .copyToString(new InputStreamReader(resource.getInputStream()));
          //添加脚本
          LuaUtils.LUA_MAP.put(name, content);
        }

      }
    }
  }

  // TODO: 2018/10/31  暂时还未看 阿波罗怎么实现的刷新 到时候要配置统一更新Lua 脚本
  public void refresh() {

  }

}

