package com.mall.discover.web.response;

import com.mall.discover.web.vo.DiscoverPictureVo;
import com.mall.search.response.ActiveItem;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class MemberDiscoverResponse implements Serializable {

    private String id;

    private Integer memberId;

    private String memberLogo;

    private String memberName;

    private String content;

    private String video;

    private Integer views;

    private Integer forwards;

    private Date createTime;

    private Date updateTime;

    private String thumbnailSmall;

    private Integer videoHeight;

    private Integer videoWidth;

    private Integer productId;

    private String productName;

    private String productMasterImg;

    private String productUrl;
    /**
     * 销售价
     */
    private BigDecimal malMobilePrice;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 佣金
     */
    private BigDecimal commisson;

    private List<DiscoverPictureVo> pictureVoList;

    /**
     * 商品参加的活动集合（商品支持同时参加多个活动）
     * 普通商品的状态下 该数据项为null，只有在该商品参加某些活动的时候，才会有该数据项
     */
    List<ActiveItem> actives;
}
