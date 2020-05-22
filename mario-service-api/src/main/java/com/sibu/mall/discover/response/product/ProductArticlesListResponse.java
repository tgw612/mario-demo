package com.mall.discover.response.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class ProductArticlesListResponse {
    @ApiModelProperty(value = "文章ID", notes = "")
    private String articleId;
    @ApiModelProperty(value = "发布状态", notes = "")
    private Integer publishStatus;
    @ApiModelProperty(value = "创建时间", notes = "")
    private Date createTime;
    @ApiModelProperty(value = "文章内容", notes = "")
    private String articleContent;
    @ApiModelProperty(value = "图片数量", notes = "")
    private Integer pictureNums;
    @ApiModelProperty(value = "话题名称", notes = "")
    private List<String> subject;
    @ApiModelProperty(value = "阅读数", notes = "")
    private Long readNums;
    @ApiModelProperty(value = "分享数", notes = "")
    private Long shareNunms;
    @ApiModelProperty(value = "排序  前端根据排序值显示 是否置顶", notes = "前端根据排序值显示 是否置顶")
    private Long sort;
    @ApiModelProperty(value = "总条数 筛选条件下的文章总条数", notes = "筛选条件下的文章总条数")
    private Long total;
}
