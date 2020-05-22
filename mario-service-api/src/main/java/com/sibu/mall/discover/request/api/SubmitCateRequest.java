package com.mall.discover.request.api;

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
public class SubmitCateRequest extends CommonRequest implements Serializable{

    private String id;

    private String  cateName;

    private Integer order;

}
