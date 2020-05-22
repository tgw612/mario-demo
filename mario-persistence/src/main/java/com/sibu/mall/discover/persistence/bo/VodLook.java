package com.mall.discover.persistence.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Data
public class VodLook {
    private static final long serialVersionUID = -5747402125297698498L;
    private String fileId;

    private String content;

    private String vodUrl;

    private String coverUrl;

    private String webpUrl;
    private Integer width;

    private Integer height;

    private BigDecimal whRatio;
}
