package com.mall.discover.web.request;

import com.doubo.common.model.request.common.CommonRequest;
import com.mall.discover.web.vo.DiscoverPictureVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * author:wang.zhiliang
 * date :2018.11.23
 */
@ApiModel(value = "发布动态模型")
@Data
public class DiscoverSubmitRequest extends CommonRequest {

    @ApiModelProperty(value = "分类ID")
    @NotBlank(message = "分类ID不能为空")
    private String cateId;

    @ApiModelProperty(value = "分类名称")
    private String cateName;

    @ApiModelProperty(value = "文案")
    @Length(max = 800,message = "文字不能超过800个字")
    private String content;

    @ApiModelProperty(value = "小视频地址")
    private String video;

    @ApiModelProperty(value = "小视频缩略图")
    private String thumbnailSmall;

    @ApiModelProperty(value = "小视频高度")
    private String videoHeight;

    @ApiModelProperty(value = "小视频宽度")
    private String videoWidth;

    @ApiModelProperty(value = "小视频关联商品ID")
    private Integer productId;

    @ApiModelProperty(value = "图片列表")
    private List<DiscoverPictureVo> pictureVoList;

}
