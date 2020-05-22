package com.mall.discover.request.client;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "话题详情")
@Data
public class ClientSubjectInfoRequest extends CommonRequest {

    @ApiModelProperty(value = "话题ID", required = true)
    @Min(value = 1, message = "话题ID不能小于0")
    private long subjectId;
}
