package com.mall.discover.response.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@Data
@ApiModel(value = "话题详情")
public class ClientSubjectInfoResponse implements Serializable {

    private static final long serialVersionUID = -6790824298186846296L;

    @ApiModelProperty(value = "话题ID")
    private Long subjectId;

    @ApiModelProperty(value = "话题名称")
    private String subjectName;

    @ApiModelProperty(value = "话题简介")
    private String subjectContent;

    @ApiModelProperty(value = "话题图片URL")
    private String subjectPictureUrl;

    @ApiModelProperty(value = "文章数量")
    private int articleCount;

    @ApiModelProperty(value = "阅读次数")
    private long readCount;

    @ApiModelProperty(value = "阅读次数")
    private String viewCountString;
}
