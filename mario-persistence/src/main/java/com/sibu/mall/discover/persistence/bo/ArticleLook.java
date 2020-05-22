package com.mall.discover.persistence.bo;

import lombok.Data;

import java.util.List;

/**
 * @author: huangzhong
 * @Date: 2019/10/6
 * @Description:
 */
@Data
public class ArticleLook {

    private List<PictureLook> pictureLookList;

    private List<VodLook> vodLookList;
}
