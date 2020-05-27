package com.mario.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Description: Author: 陈二伟 Date: 2018/11/05
 */
public class JsonUtils {


  public static String toJson(Object object) {
    return JSON.toJSONString(object, SerializerFeature.WriteDateUseDateFormat);
  }

  public static <T> T fromJson(String json, Class<T> type) {
    return JSON.parseObject(json, type);
  }
}
