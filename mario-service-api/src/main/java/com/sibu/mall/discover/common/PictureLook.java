package com.mall.discover.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Data
@ApiModel(value = "发现页图片")
public class PictureLook implements Serializable {
    private static final long serialVersionUID = -5747402125297698498L;

    @ApiModelProperty(value = "图片排序")
    private int sort;

    @ApiModelProperty(value = "图片URL")
    private String pictureUrl;

    @ApiModelProperty(value = "图片宽度")
    private Integer width;

    @ApiModelProperty(value = "图片高度")
    private Integer height;

    @ApiModelProperty(value = "宽高比", notes = "保留两位小数")
    private BigDecimal whRatio;
}
