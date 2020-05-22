package com.mall.discover.request;

import com.doubo.common.model.request.common.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * author:wang.zhiliang
 * date:2018.11.22
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IndexCommendDiscoverRequest extends CommonRequest implements Serializable{

    @NotNull
    private Integer platform;

}
