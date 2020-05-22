package com.mall.discover.service.impl.biz;


import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.threadlocal.SerialNo;
import com.doubo.common.util.ConverterUtil;
import com.doubo.common.util.ExceptionUtil;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.util.BeanCopyUtils;
import com.mall.discover.persistence.dao.AdminDiscoverDao;
import com.mall.discover.persistence.entity.MemberDiscoverCateEntity;
import com.mall.discover.persistence.entity.MemberDiscoverEntity;
import com.mall.discover.request.admin.*;
import com.mall.discover.response.admin.AdminDiscoverCateFindCateByIdResponse;
import com.mall.discover.response.admin.AdminDiscoverDetailResponse;
import com.mall.discover.response.admin.AdminDiscoverListCateResponse;
import com.mall.discover.response.admin.AdminPageDiscoverListResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminDiscoverBiz {

    @Autowired
    private AdminDiscoverDao adminDiscoverDao;

    public CommonResponse<CommonPageResult<AdminPageDiscoverListResponse>> listPage(AdminPageDiscoverListRequest request) {
        try {
            Map queryMap = BeanUtils.describe(request);
            queryMap.put("pageSize", request.getPageSize());
            queryMap.put("pageNow", request.getCurrentPage());
            queryMap.put("q_cateName", request.getCateName());
            queryMap.put("q_cateId", request.getCateId());
            queryMap.put("q_content",request.getContent());
            queryMap.put("q_title",request.getTitle());
            Long count = adminDiscoverDao.count(queryMap);
            List<MemberDiscoverEntity> memberDiscoverEntityList = adminDiscoverDao.selectList(queryMap);
            List<AdminPageDiscoverListResponse> datas = BeanCopyUtils.copyList(AdminPageDiscoverListResponse.class, memberDiscoverEntityList);
            return ResponseManage.success(
                    ConverterUtil.convertToPageResult(datas,
                            count,
                            ConverterUtil.calculateTotalPage(request.getPageSize(), count.intValue())
                    )
            );
        } catch (Exception e) {
            log.error("[{}]分页查询动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
        }
        return ResponseManage.fail("分页查询动态失败");
    }

    public CommonResponse<List<AdminDiscoverListCateResponse>> listCate(AdminDiscoverListCateRequest request) {
        try {
            Map queryMap = BeanUtils.describe(request);
            queryMap.put("pageSize", Integer.MAX_VALUE);
            queryMap.put("pageNow", 1);
            queryMap.put("q_cateName", "");
            List<MemberDiscoverCateEntity> cateEntityList = adminDiscoverDao.selectCateListPage(queryMap);
            List<AdminDiscoverListCateResponse> datas = BeanCopyUtils.copyList(AdminDiscoverListCateResponse.class, cateEntityList);
            return ResponseManage.success(datas);
        } catch (Exception e) {
            log.error("[{}]分页查询动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
        }
        return ResponseManage.fail("分页查询动态失败");
    }

    public CommonResponse<CommonPageResult<AdminDiscoverListCateResponse>> listCatePage(AdminDiscoverListCateRequest request) {
        try {
            Map queryMap = BeanUtils.describe(request);
            queryMap.put("pageSize", request.getPageSize());
            queryMap.put("pageNow", request.getCurrentPage());
            queryMap.put("q_cateName", request.getCateName());
            queryMap.put("q_cateId", request.getCateId());
            queryMap.put("q_content", request.getContent());
            Long count = adminDiscoverDao.selectListCateCount(queryMap);
            List<MemberDiscoverCateEntity> cateEntityList = adminDiscoverDao.selectCateListPage(queryMap);
            List<AdminDiscoverListCateResponse> datas = BeanCopyUtils.copyList(AdminDiscoverListCateResponse.class, cateEntityList);
            return ResponseManage.success(ConverterUtil.convertToPageResult(datas, count, ConverterUtil.calculateTotalPage(request.getPageSize(), count.intValue())));
        } catch (Exception e) {
            log.error("[{}]分页查询动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
        }
        return ResponseManage.fail("分页查询动态失败");
    }

    public CommonResponse<Boolean> add(AdminDiscoverAddRequest request) {
        try {
            MemberDiscoverEntity entity = BeanCopyUtils.copyProperties(MemberDiscoverEntity.class, request);
            entity.setCreateTime(com.doubo.common.util.DateUtil.getNowDate());
            entity.setViews(0);
            entity.setForwards(0);
            entity.setRecomend(false);
            entity.setDisplay(true);
            adminDiscoverDao.add(entity);
            return ResponseManage.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("[{}]发布动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
        }
        return ResponseManage.fail("发布动态失败");
    }

    public CommonResponse<Boolean> update(AdminDiscoverAddRequest request) {
        try {
            MemberDiscoverEntity entity = BeanCopyUtils.copyProperties(MemberDiscoverEntity.class, request);
            entity.setUpdateTime(com.doubo.common.util.DateUtil.getNowDate());
            //entity.setViews(0);
            //entity.setForwards(0);
            //entity.setRecomend(false);
            //entity.setDisplay(true);
            adminDiscoverDao.update(entity);
            return ResponseManage.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("[{}]修改动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
        }
        return ResponseManage.fail("修改动态失败");
    }

    public CommonResponse<AdminDiscoverDetailResponse> detail(AdminDiscoverDetailRequest request) {
        MemberDiscoverEntity item = adminDiscoverDao.findById(request.getId());
        AdminDiscoverDetailResponse response = BeanCopyUtils.copyProperties(AdminDiscoverDetailResponse.class, item);
        return ResponseManage.success(response);
    }

    public CommonResponse<Integer> delete(AdminDiscoverDetailRequest request) {
        adminDiscoverDao.delete(request.getId());
        return ResponseManage.success(1);
    }

    public CommonResponse<Boolean> addCate(AdminDiscoverCateAddCateRequest request) {
        try {
            MemberDiscoverCateEntity entity = BeanCopyUtils.copyProperties(MemberDiscoverCateEntity.class, request);
            entity.setCreateTime(com.doubo.common.util.DateUtil.getNowDate());
            adminDiscoverDao.addCate(entity);
            return ResponseManage.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("[{}]添加分类失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
        }
        return ResponseManage.fail("添加分类失败");
    }

    public CommonResponse<Integer> deleteCateById(AdminDiscoverCateFindCateByIdRequest request) {
        adminDiscoverDao.deleteCate(request.getId());
        return ResponseManage.success(1);
    }

    public CommonResponse<Boolean> updateCateById(AdminDiscoverCateAddCateRequest request) {
        try {
            MemberDiscoverCateEntity entity = BeanCopyUtils.copyProperties(MemberDiscoverCateEntity.class, request);
            entity.setCreateTime(com.doubo.common.util.DateUtil.getNowDate());
            adminDiscoverDao.updateCateById(entity);
            return ResponseManage.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("[{}]修改分类失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
        }
        return ResponseManage.fail("修改分类失败");
    }

    public CommonResponse<AdminDiscoverCateFindCateByIdResponse> findCateById(AdminDiscoverCateFindCateByIdRequest request) {
        MemberDiscoverCateEntity item = adminDiscoverDao.findCateById(request.getId());
        AdminDiscoverCateFindCateByIdResponse response = BeanCopyUtils.copyProperties(AdminDiscoverCateFindCateByIdResponse.class, item);
        return ResponseManage.success(response);
    }

}
