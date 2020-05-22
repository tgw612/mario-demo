package com.mall.discover.common.enums;

/**
 * @author: huangzhong
 * @Date: 2019/10/10
 * @Description: 高佣金商品
 */
public enum HighProductStatusEnum {
    /**
     * 不是高佣金
     */
    FLASE(0, "否"),
    /**
     * 是高佣金
     */
    TRUE(1, "是");


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    HighProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
