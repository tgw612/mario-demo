package com.mall.discover.web.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class MemberDiscoverCateResponse implements Serializable {

    private String id;

    private String cateName;

}
