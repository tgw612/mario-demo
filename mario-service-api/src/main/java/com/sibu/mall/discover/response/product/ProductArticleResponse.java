package com.mall.discover.response.product;

import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.response.article.ArticleRelateProduct;
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
@ApiModel(value = "商品内文章列表")
public class ProductArticleResponse implements Serializable {
    private static final long serialVersionUID = 4299138497655058216L;
    @ApiModelProperty(value = "关联id", notes = "")
    private Long relateId;
    @ApiModelProperty(value = "文章ID", notes = "")
    private Long articleId;

    @ApiModelProperty(value = "文章状态 1-草稿 2-已发布", notes = "1-草稿 2-已发布")
    private Integer articleStatus;

    @ApiModelProperty(value = "创建时间", notes = "创建时间")
    private long createTime;

    @ApiModelProperty(value = "文章内容", notes = "")
    private String articleContent;

    @ApiModelProperty(value = "图片数量", notes = "")
    private Integer pictureNums;

    @ApiModelProperty(value = "定时发布时间", notes = "")
    private Long publishTime;

    @ApiModelProperty(value = "话题名称", notes = "")
    private List<SubjectInfo> subject;

    @ApiModelProperty(value = "阅读数", notes = "")
    private Long readCount;

    @ApiModelProperty(value = "分享数", notes = "")
    private Long shareCount;

    @ApiModelProperty(value = "排序", notes = "")
    private Long sort;

    @Data
    public static class SubjectInfo implements Serializable {
        private static final long serialVersionUID = -8437110998745844853L;
        @ApiModelProperty(value = "话题ID")
        private Long subjectId;

        @ApiModelProperty(value = "话题名称")
        private String subjectName;
    }

    @ApiModelProperty(value = "文章点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "文章图片视频详情", notes = "")
    private ArticleLook articleLooks;

    @ApiModelProperty(
            value = "文章关联商品",
            notes = ""
    )
    private List<ArticleRelateProduct> product;

    @ApiModelProperty(
            value = "文章标题最多30汉字",
            notes = ""
    )
    private String articleTitle;
}
