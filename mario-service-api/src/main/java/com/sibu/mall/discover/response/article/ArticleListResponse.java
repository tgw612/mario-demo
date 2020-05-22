package com.mall.discover.response.article;

import com.mall.discover.common.article.ArticleLook;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class ArticleListResponse implements Serializable {
    private static final long serialVersionUID = 5139746126228046602L;
    @ApiModelProperty(value = "文章ID", notes = "")
    private Long articleId;
    @ApiModelProperty(value = "文章状态 1-草稿 2-已发布 3-待发布 4-待审核 5-未审核通过")
    private Integer articleStatus;
    @ApiModelProperty(value = "创建时间", notes = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "文章内容 文章内容太长，性能太低。优先级：低", notes = "文章内容太长，性能太低。优先级：低")
    private String articleContent;

    @ApiModelProperty(value = "图片数量", notes = "")
    private Integer pictureNums;
    @ApiModelProperty(value = "话题名称", notes = "")
    private List<SubjectInfoResponse> subject;
    @ApiModelProperty(value = "文章关联商品", notes = "")
    private List<ArticleRelateProduct> product;

    @ApiModelProperty(value = "阅读数", notes = "")
    private Long readCount;
    @ApiModelProperty(value = "分享数", notes = "")
    private Long shareCount;
    @ApiModelProperty(value = "文章排序 articleSort=1时显示置顶状态", notes = "articleSort=1时显示置顶状态")
    private Long articleSort;
    @ApiModelProperty(value = "文章标题最多30汉字", notes = "")
    private String articleTitle;
    @ApiModelProperty(value = "文章发布时间")
    private Long publishTime;
    @ApiModelProperty(value = "文章点赞数")
    private Integer likeCount;
    @ApiModelProperty(value = "文章图片视频详情", notes = "")
    private ArticleLook articleLooks;
}
