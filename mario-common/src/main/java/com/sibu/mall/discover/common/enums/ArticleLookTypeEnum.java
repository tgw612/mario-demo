package com.mall.discover.common.enums;

/**
 * @author: huangzhong
 * @Date: 2019/11/14
 * @Description:
 */
public enum ArticleLookTypeEnum {
    NONE(0, "默认值，容错"),

    ONLY_PICTURE(1, "仅图片"),

    ONLY_VIDEO(2, "仅视频"),

    PICTURE_VIDEO(3,"图片和视频");


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ArticleLookTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
