package com.mall.discover.common.enums;

/**
 * @author: huangzhong
 * @Date: 2019/10/10
 * @Description: 爆款商品
 */
public enum HotProductStatusEnum {
    /**
     * 不是爆款
     */
    FLASE(0, "否"),
    /**
     * 是爆款商品
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

    HotProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
