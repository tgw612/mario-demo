package com.mall.discover.request.subject;

import com.doubo.common.model.request.PageQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "获取话题分页列表")
@Data
public class SubjectPageRequestDto extends PageQueryRequest {

    @ApiModelProperty(value = "话题名称")
    private String subjectName;
}
