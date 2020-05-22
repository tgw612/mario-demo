package com.mall.discover.response.api;

import com.doubo.common.model.response.CommonResponse;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class MemberDiscoverCateListResponse extends CommonResponse {

    private String id;

    private String cateName;

}