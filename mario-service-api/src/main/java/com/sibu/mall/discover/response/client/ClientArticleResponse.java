package com.mall.discover.response.client;

import com.mall.discover.common.PictureLook;
import com.mall.discover.common.VodLook;
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
@ApiModel(value = "文章")
public class ClientArticleResponse extends ClientDiscoverResponse implements Serializable {
    private static final long serialVersionUID = -6790824298186846296L;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "第一张图片")
    private PictureLook pictureLook;

    @ApiModelProperty(value = "视频,如果为空则显示图片")
    private VodLook vodLook;

    @ApiModelProperty(value = "阅读次数")
    private long readCount;

    @ApiModelProperty(value = "阅读次数")
    private String viewCountString;

    @ApiModelProperty(value = "排序")
    private Long sort;

    @ApiModelProperty(value = "点赞信息")
    private ClientLikeResponse clientLikeResponse;

    @ApiModelProperty(value = "话题集合")
    private List<ClientSubjectResponse> subjectList;
}
