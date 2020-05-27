package com.mario.mq.enums;

import com.doubo.common.topic.MqTopic;
import com.doubo.common.topic.env.ITopicEnv;
import com.doubo.common.topic.env.TopicEnv;
import com.doubo.common.util.EnumUtil;

/**
 * @author EDZ
 */

public enum DdiscoverProductEnum implements MqTopic {
  PRODUCT_BASIC_INFO_CHANGE_TOPIC("topic_product_basicinfo_change", "商品名称变动", "name"),
  ;
  private static final String DEFAULT_TAG = "*";
  /**
   * 默认不需要通过后缀来区分环境
   *
   * @see TopicEnv
   */
  public static ITopicEnv env = TopicEnv.EMPTY;
  private String code;
  private String desc;
  /**
   * Tag，即消息标签、消息类型，用来区分某个MQ的Topic下的消息分类。MQ允许消费者按照Tag对消息进行过滤，确保消费者最终只消费到他关心的消息类型。
   */
  private String tag;
  private boolean isBroadcast;//是否为广播模式主题（默认值为false）

  DdiscoverProductEnum(String code, String desc, String tag) {
    this(code, false, desc, tag);
  }

  DdiscoverProductEnum(String code, boolean isBroadcast, String desc, String tag) {
    this.code = code;
    this.desc = desc;
    this.isBroadcast = isBroadcast;
    this.tag = tag;
  }

  public static DdiscoverProductEnum resolve(String value) {
    return EnumUtil.fromEnumValue(DdiscoverProductEnum.class, "code", value);
  }

  public static void setEnv(TopicEnv env) {
    DdiscoverProductEnum.env = env;
  }

  @Override
  public String getCode() {
    return env.getCode(code);
  }

  @Override
  public String getDesc() {
    return desc;
  }

  @Override
  public String getTag() {
    return tag;
  }

  public boolean isBroadcast() {
    return isBroadcast;
  }

  public void env(TopicEnv env) {
    DdiscoverProductEnum.env = env;
  }
}
