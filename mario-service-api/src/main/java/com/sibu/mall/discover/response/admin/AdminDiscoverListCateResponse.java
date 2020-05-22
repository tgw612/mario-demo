package com.mall.discover.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class AdminDiscoverListCateResponse implements Serializable {
    private static final long serialVersionUID = 2285675288642889474L;

    private String id;

    private String cateName;

    private  Integer order;

    private Integer display;

    private Date createTime;
}
