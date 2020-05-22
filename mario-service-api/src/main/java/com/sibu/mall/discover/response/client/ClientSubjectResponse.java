package com.mall.discover.response.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "话题")
@Data
public class ClientSubjectResponse extends ClientDiscoverResponse implements Serializable {
    private static final long serialVersionUID = -6790824298186846296L;

    @ApiModelProperty(value = "话题ID")
    private Long subjectId;

    @ApiModelProperty(value = "话题名称")
    private String subjectName;

    @ApiModelProperty(value = "话题简介")
    private String subjectContent;

    @ApiModelProperty(value = "话题图片URL")
    private String subjectPictureUrl;

    @ApiModelProperty(value = "资源位")
    private Integer resourceSort;
}
