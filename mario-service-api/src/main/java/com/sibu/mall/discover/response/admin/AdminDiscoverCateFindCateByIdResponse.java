package com.mall.discover.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class AdminDiscoverCateFindCateByIdResponse implements Serializable{

    private String id;


    private String cateName;


    private Integer order;


    private Integer display;


    private Date createTime;


    private Date updateTime;
}
