package com.mario.common.enums;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
public enum ArticleStatusEnum {

  DRAFT(1, "草稿"),

  PUBLISHED(2, "已发布"),

  TO_PUBLISHED(3, "待发布");


  private Integer code;
  private String msg;

  ArticleStatusEnum(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}
