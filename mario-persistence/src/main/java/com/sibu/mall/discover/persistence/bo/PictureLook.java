package com.mall.discover.persistence.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Data
public class PictureLook{
    private static final long serialVersionUID = -5747402125297698498L;

    private int sort;

    private String pictureUrl;

    private Integer width;

    private Integer height;

    private BigDecimal whRatio;
}
