package com.mall.discover.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "member_discover")
public class MemberDiscoverEntity implements Serializable {

    @Id
    private String id;

    @Field
    @Indexed
    private Integer memberId;

    @Field
    private String memberLogo;

    @Field
    private String memberName;

    @Field
    private String memberPhone;

    @Field
    @Indexed
    private String cateId;

    @Field
    private String  cateName;

    @Field
    private String content;

    @Field
    private String pictures;

    @Field
    private String thumbnailSmall;

    @Field
    private String video;

    @Field
    private Integer videoHeight;

    @Field
    private Integer videoWidth;

    @Field
    @Indexed
    private Integer productId;

    @Field
    private Boolean recomend;

    @Field
    private Boolean display;

    @Field
    private Integer views;

    @Field
    private Integer forwards;

    @Field
    @Indexed
    private Date createTime;

    @Field
    private Date updateTime;

    @Field
    private Boolean sameProductFlag;

    @Field
    private Integer sameProductId;

    @Field
    private Integer order;

    @Field
    private String title;

}