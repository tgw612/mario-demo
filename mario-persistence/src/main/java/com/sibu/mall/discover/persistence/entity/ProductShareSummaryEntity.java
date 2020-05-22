package com.mall.discover.persistence.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;

@Data
@Document(collection = "product_share_summary")
public class ProductShareSummaryEntity implements Serializable {

    @Field
    private Integer productId;

    @Field
    private Integer shareCount;

}