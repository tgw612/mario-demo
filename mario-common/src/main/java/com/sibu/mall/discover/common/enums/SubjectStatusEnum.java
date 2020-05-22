package com.mall.discover.common.enums;

/**
 * @author: huangzhong
 * @Date: 2019/11/2
 * @Description:
 */
public enum SubjectStatusEnum {

    DRAFT(1, "草稿"),

    PUBLISHED(2, "已发布"),

    TO_PUBLISHED(3,"待发布");


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    SubjectStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
