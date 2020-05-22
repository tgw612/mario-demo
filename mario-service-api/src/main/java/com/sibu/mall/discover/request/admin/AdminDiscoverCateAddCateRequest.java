package com.mall.discover.request.admin;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AdminDiscoverCateAddCateRequest extends CommonRequest {
    private String id;

    private String cateName;


    private Integer order;


    private Integer display;

}
