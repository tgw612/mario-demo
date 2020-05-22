package com.mall.discover.request.admin;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * author:wang.zhiliang
 * date:2018.11.22
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AdminUpdateDiscoverRequest extends CommonRequest implements Serializable {
    private  String id;

    private  Boolean recomend;

    private  Boolean display;
}
