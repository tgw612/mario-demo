package com.mall.discover.common.enums;

import lombok.Getter;

/**
 * @author: zhiming
 * @Date: 2019/9/30
 * @Description:
 */
@Getter
public enum RelationTypeEnum {

    GOODS_ARTICLE(1,"商品-文章"),
    SUBJECT_ARTICLE(2,"话题-文章"),
    GOODS_SUBJECT(3,"商品-话题"),
    USER_LIKE_ARTICLE(4,"用户-点赞文章");
    private Integer code;
    private String name;

    RelationTypeEnum() {
    }

    RelationTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
