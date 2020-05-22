package com.mall.discover.response.subject;

import com.mall.discover.common.subject.SubjectLook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "话题列表")
@Data
public class SubjectResponse implements Serializable {

    @ApiModelProperty(value = "话题ID")
    private Long subjectId;

    @ApiModelProperty(value = "话题名称")
    private String subjectName;

    @ApiModelProperty(value = "话题简介")
    private String subjectContent;

    @ApiModelProperty(value = "话题图片、音频等汇总信息")
    private SubjectLook subjectLook;

    @ApiModelProperty(value = "话题排序")
    private Long sort;

    @ApiModelProperty(value = "创建时间")
    private long createTime;

    @ApiModelProperty(value = "文章数量")
    private int articleCount;

    @ApiModelProperty(value = "阅读数")
    private long readCount;

    @ApiModelProperty(value = "分享数")
    private long shareCount;

    @ApiModelProperty(value = "话题发布时间")
    private Long publishTime;

    @ApiModelProperty(value = "话题状态：1-草稿 2-已发布;3-待发布")
    private Integer subjectStatus;
}
