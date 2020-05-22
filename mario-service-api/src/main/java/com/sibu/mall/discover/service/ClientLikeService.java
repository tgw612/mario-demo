package com.mall.discover.service;

import com.doubo.common.model.response.CommonResponse;

/**
 * @author: huangzhong
 * @Date: 2019/10/31
 * @Description: 客户端点赞服务
 */
public interface ClientLikeService {

    /**
     * 定时更新点赞关系表
     * @param relationEnumCode  1:商品-文章,2:话题-文章,4:用户-点赞文章
     *
     * @return
     */
    CommonResponse<Boolean> updateUserLike(Integer relationEnumCode);

}
