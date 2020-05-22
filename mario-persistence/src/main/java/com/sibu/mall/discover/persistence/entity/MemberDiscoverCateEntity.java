package com.mall.discover.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "member_discover_cate")
public class MemberDiscoverCateEntity implements Serializable {

    @Id
    private String id;

    @Field
    private String cateName;

    @Field
    @Indexed
    private Integer order;

    @Field
    @Indexed
    private Integer display;

    @Field
    private Date createTime;

    @Field
    private Date updateTime;


}