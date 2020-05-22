package com.mall.discover.response.client;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Data
@ApiModel(value = "发现页统一返回")
public class ClientDiscoverResponse implements Serializable {
    private static final long serialVersionUID = -6790824298186846296L;
}
