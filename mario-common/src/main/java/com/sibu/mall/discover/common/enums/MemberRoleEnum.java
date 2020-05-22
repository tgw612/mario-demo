package com.mall.discover.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MemberRoleEnum {

    /**
     * 游客
     */
    VISITOR("1,3", "visitor"),
    /**
     * 经销商
     */
    DISTRIBUTOR("1,2", "distributor"),
    /**
     * 普通用户
     */
    ORDINARY_USER("1,3", "user");

    private String showType; //用户对应商品的展示类型值，用逗号分隔。1、所有人；2、经销商；3、普通用户'
    @Getter
    private String cacheKey;//缓存键值

    MemberRoleEnum(String showType, String cacheKey) {
        this.showType = showType;
        this.cacheKey = cacheKey;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public List<String> getShowTypeList() {
        return Arrays.asList(this.showType.split(","));
    }

    public List<Integer> getShowTypeListInteger(){
        return Arrays.stream(this.showType.split(",")).map((p) -> {
            return Integer.parseInt(p);
        }).collect(Collectors.toList());
    }
}
