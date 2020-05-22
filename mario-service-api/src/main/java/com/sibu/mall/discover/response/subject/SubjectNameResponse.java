package com.mall.discover.response.subject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@Data
@ApiModel(value = "话题名称搜索")
public class SubjectNameResponse implements Serializable {

    @ApiModelProperty(value = "话题ID")
    private Long subjectId;

    @ApiModelProperty(value = "话题名称")
    private String subjectName;

    @ApiModelProperty(value = "话题定时发布时间")
    private Long publishTime;

    @ApiModelProperty(value = "话题发布状态")
    private Integer subjectStatus;
}
