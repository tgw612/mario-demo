package com.mall.discover.service;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.admin.*;
import com.mall.discover.response.admin.AdminDiscoverCateFindCateByIdResponse;
import com.mall.discover.response.admin.AdminDiscoverDetailResponse;
import com.mall.discover.response.admin.AdminDiscoverListCateResponse;
import com.mall.discover.response.admin.AdminPageDiscoverListResponse;

import java.util.List;

public interface AdminDiscoverService {

    /**
     * 发现内容列表
     *
     * @param request
     * @return
     */
    CommonResponse<CommonPageResult<AdminPageDiscoverListResponse>> listPage(AdminPageDiscoverListRequest request);


    /**
     * 增加发现内容
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> add(AdminDiscoverAddRequest request);

    /**
     * 修改内容
     * @param request
     * @return
     */
    CommonResponse<Boolean> update(AdminDiscoverAddRequest request);
    /**
     * 删除内容
     *
     * @param request
     * @return
     */
    CommonResponse<Integer> delete(AdminDiscoverDetailRequest request);
    /**
     * 详情
     *
     * @param request
     * @return
     */
    CommonResponse<AdminDiscoverDetailResponse> detail(AdminDiscoverDetailRequest request);


    /**
     * 分类列表
     * @param request
     * @return
     */
    CommonResponse<List<AdminDiscoverListCateResponse>> listCate(AdminDiscoverListCateRequest request);

    /**
     * 分类列表
     * @param request
     * @return
     */
    CommonResponse<CommonPageResult<AdminDiscoverListCateResponse>> listCatePage(AdminDiscoverListCateRequest request);


    CommonResponse<Boolean> addCate(AdminDiscoverCateAddCateRequest request);
    CommonResponse<Integer> deleteCateById(AdminDiscoverCateFindCateByIdRequest request);
    CommonResponse<Boolean> updateCateById(AdminDiscoverCateAddCateRequest request);
    CommonResponse<AdminDiscoverCateFindCateByIdResponse> findCateById(AdminDiscoverCateFindCateByIdRequest request);

}
