package com.mall.discover.common.enums;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
public enum  BizTypeEnum {
    /**
     * 文章
     */
    ARTICLE(1, "文章"),
    /**
     * 商品
     */
    PRODUCT(2, "商品"),
    /**
     * 话题
     */
    SUBJECT(3,"话题"),
    /**
     * 点赞
     */
    LIKE(4,"点赞");


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    BizTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
