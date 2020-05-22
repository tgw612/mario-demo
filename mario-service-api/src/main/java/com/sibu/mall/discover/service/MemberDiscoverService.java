package com.mall.discover.service;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.request.PrimaryIdRequest;
import com.doubo.common.model.request.PrimarySeqRequest;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.admin.AdminPageQueryDiscoverListRequest;
import com.mall.discover.request.api.*;
import com.mall.discover.request.admin.AdminUpdateDiscoverRequest;
import com.mall.discover.response.api.MemberDiscoverCateListResponse;
import com.mall.discover.response.api.MemberDiscoverDetailResponse;
import com.mall.discover.response.api.MemberPageQueryDiscoverListResponse;

import java.util.List;

public interface MemberDiscoverService {

    /**
     * 提交动态
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> submitDiscover(SubmitDiscoverRequest request);

    /**
     * 动态列表
     *
     * @param request
     * @return
     */
    CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> pageDiscover(MemberPageQueryDiscoverListRequest request);

    /**
     * 后台动态列表
     *
     * @param request
     * @return
     */
    CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> adminPageDiscover(AdminPageQueryDiscoverListRequest request);

    /**
     * 详情
     *
     * @param request
     * @return
     */
    CommonResponse<MemberDiscoverDetailResponse> detail(PrimarySeqRequest request);

    /**
     * 获取商品推广次数
     *
     * @param productIdRequest
     * @return
     */
    CommonResponse<Integer> getProductShareCount(PrimaryIdRequest productIdRequest);

    /**
     * 转发
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> forward(PrimarySeqRequest request);

    /**
     * 浏览
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> views(PrimarySeqRequest request);

    /**
     * 更新发现
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> updateByPrimaryKeySelective(AdminUpdateDiscoverRequest request);

    /**
     * 发现分类增加
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> submitCate(SubmitCateRequest request);

    /**
     * 查询发现分类
     *
     * @param request
     * @return
     */
    CommonResponse<List<MemberDiscoverCateListResponse>> listCate(ListCateRequest request);


    /**
     * 查询分享数量
     * @param request
     * @return
     */
    CommonResponse<Integer> findShareCount(FindShareCountRequest request);

}
