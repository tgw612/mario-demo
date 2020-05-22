package com.mall.discover.request.admin;

import com.doubo.common.model.request.PageQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class AdminPageQueryDiscoverListRequest extends PageQueryRequest {

    private String q_name;

    private String q_phone;

    private String q_status;

    private String q_recomend;

    private String q_startTime;

    private String q_endTime;

}