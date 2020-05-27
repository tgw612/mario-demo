package com.mario.mq.enums;

import lombok.Getter;

/**
 * @ClassName SwitchEnum MQ消费者开关
 * @Description SwitchEnum
 * @Author lanze
 * @Date 2019-09-29 16:56
 * @Version 1.0
 **/
public enum SwitchEnum {
  LEVEL_1("1", "开"),
  LEVEL_0("0", "关");

  @Getter
  private String code;

  @Getter
  private String desc;

  SwitchEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
