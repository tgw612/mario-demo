package com.mall.discover.request.subject;

import com.doubo.common.model.request.common.CommonRequest;
import com.mall.discover.common.subject.SubjectLook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "提交话题")
@Data
public class SubjectSubmitRequestDto extends CommonRequest {

    @ApiModelProperty(value = "话题ID", notes = "商品或者话题id")
    private Long subjectId;

    @ApiModelProperty(value = "话题名称", notes = "话题名称", required = true)
    @NotNull(message = "话题名称不能为空")
    private String subjectName;

    @ApiModelProperty(value = "话题简介", notes = "话题简介", required = true)
    @NotNull(message = "话题简介不能为空")
    private String subjectContent;

    @ApiModelProperty(value = "话题图片、音频等汇总信息", required = true)
    @NotNull(message = "话题图片不能为空")
    private SubjectLook subjectLook;

    @ApiModelProperty(value = "话题排序")
    private Long sort;

    @ApiModelProperty(value = "发布时间")
    private Long publishTime;

    @ApiModelProperty(value = "话题状态")
    @NotNull(message = "话题状态不能为空")
    private Integer subjectStatus;
}
