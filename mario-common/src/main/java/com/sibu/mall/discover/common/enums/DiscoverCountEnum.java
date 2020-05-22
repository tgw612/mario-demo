package com.mall.discover.common.enums;

/**
 * @author: huangzhong
 * @Date: 2019/10/12
 * @Description:  发现页统计数据
 */
public enum DiscoverCountEnum {
    /**
     * 文章阅读次数
     */
    ARTICLE_READ_COUNT("discover-1-", "文章阅读次数"),
    /**
     * 文章分享次数
     */
    ARTICLE_SHARE_COUNT("discover-2-", "文章分享次数"),
    /**
     * 商品阅读次数
     */
    PRODUCT_READ_COUNT("discover-3-", "商品阅读次数"),
    /**
     * 商品分享次数
     */
    PRODUCT_SHARE_COUNT("discover-4-", "商品分享次数"),
    /**
     * 话题阅读次数
     */
    SUBJECT_READ_COUNT("discover-5-", "话题阅读次数"),
    /**
     * 话题分享次数
     */
    SUBJECT_SHARE_COUNT("discover-6-", "话题分享次数"),

    /**
     * 文章点赞次数
     */
    ARTICLE_LIKE_COUNT("discover-like-1-", "文章点赞次数");

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    DiscoverCountEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
