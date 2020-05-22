package com.mall.discover.common.enums;


/**
 * 平台枚举
 */
public enum PlatformEnum {

    /**
     * app客户端
     */
    PLATFORM_1(1, "app客户端"),
    /**
     * app商家端
     */
    PLATFORM_2(2, "app商家端"),
    /**
     * 小程序客户端
     */
    PLATFORM_3(3,"小程序客户端"),
    /**
     * 小程序商家端
     */
    PLATFORM_4(4,"小程序商家端"),
    /**
     * 公众号
     */
    PLATFORM_5(5,"公众号");


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    PlatformEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}