package com.mall.discover.request.api;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class FindShareCountRequest extends CommonRequest {

    private static final long serialVersionUID = 1L;

    private String productId;
}