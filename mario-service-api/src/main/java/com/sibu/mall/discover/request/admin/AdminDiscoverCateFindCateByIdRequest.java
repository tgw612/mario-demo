package com.mall.discover.request.admin;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AdminDiscoverCateFindCateByIdRequest extends CommonRequest{

    private String id;
}
