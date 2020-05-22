package com.mall.discover.common.subject;

import com.mall.discover.common.PictureLook;
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
@ApiModel(value = "话题图片、音频的汇总")
public class SubjectLook implements Serializable {
    private static final long serialVersionUID = -5747402125297698498L;

    @ApiModelProperty(value = "图片集合")
    private List<PictureLook> pictureLookList;
}
