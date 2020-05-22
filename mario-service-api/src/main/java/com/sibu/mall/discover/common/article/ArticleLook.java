package com.mall.discover.common.article;

import com.mall.discover.common.PictureLook;
import com.mall.discover.common.VodLook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/10/6
 * @Description:
 */
@Data
@ApiModel(value = "文章图片、音频的汇总")
public class ArticleLook implements Serializable {

    private static final long serialVersionUID = -7403737520747552375L;
    @ApiModelProperty(value = "图片集合",notes = "最多九张")
    private List<PictureLook> pictureLookList;

    @ApiModelProperty(value = "视频集合",notes = "最多1个")
    private List<VodLook> vodLookList;
}
