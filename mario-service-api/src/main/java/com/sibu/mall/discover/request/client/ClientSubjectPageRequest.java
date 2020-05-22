package com.mall.discover.request.client;

import com.doubo.common.model.request.PageQueryRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@Data
@ApiModel(value = "获取话题分页列表")
public class ClientSubjectPageRequest extends PageQueryRequest {

}
