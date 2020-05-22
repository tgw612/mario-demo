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
@ApiModel(value = "视频")
public class VodLook implements Serializable {
    private static final long serialVersionUID = -5747402125297698498L;
    @ApiModelProperty(value = "文件id")
    private String fileId;

    @ApiModelProperty(value = "文件校验内容")
    private String content;

    @ApiModelProperty(value = "视频URL")
    private String vodUrl;

    @ApiModelProperty(value = "视频封面截图URL,取视频第一帧")
    private String coverUrl;

    @ApiModelProperty(value = "视频前5s动图URL")
    private String webpUrl;

    @ApiModelProperty(value = "宽度")
    private Integer width;

    @ApiModelProperty(value = "高度")
    private Integer height;

    @ApiModelProperty(value = "宽高比", notes = "保留两位小数")
    private BigDecimal whRatio;

    @ApiModelProperty(value = "用户 主播id")
    private Integer memberId;
}
