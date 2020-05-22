package com.mall.discover.persistence.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberEntity implements Serializable{

    private Integer id;
    private String name;
}
