package com.mall.discover.request.article;

import com.doubo.common.model.request.common.CommonRequest;
import com.mall.discover.common.article.ArticleLook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;


@ApiModel("新建or编辑文章")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AddOrEditArticleRequest extends CommonRequest {
    private static final long serialVersionUID = 7805058863355060057L;
    @ApiModelProperty(value = "文章ID", notes = "")
    private Long articleId;
    @ApiModelProperty(value = "话题图片、音频等汇总信息")
    private ArticleLook articleLook;
    @ApiModelProperty(value = "文章标题最多30汉字", notes = "")
    private String articleTitle;
    @ApiModelProperty(value = "文章内容", notes = "最多3000字节", required = true)
    private String articleContent;
    @ApiModelProperty(value = "话题ID", notes = "", required = false)
    private List<Long> subjectId;
    @ApiModelProperty(value = "商品编码", notes = "", required = true)
    private List<String> productNo;
    @ApiModelProperty(value = "文章状态,设为草稿 状态为1、发布文章 状态为2，状态可互相转换 3为待发布状态", required = true)
    private Integer articleStatus;
    @ApiModelProperty(value = "发布时间,非定时发布则为空")
    private Long publishTime;
    @ApiModelProperty(value = "排序值,为1则置顶,从小到大排序,sort值相等时,按照创建时间排序从大到小排")
    private Long sort;
    @ApiModelProperty(value = "创建用户类型 1-运营后台 2-商家 3-C端用户")
    private Integer createUserType;
}
