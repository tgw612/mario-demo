package com.mario.service.api.service.impl.biz;

import com.alibaba.fastjson.JSON;
import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.request.PrimaryIdRequest;
import com.doubo.common.model.request.PrimarySeqRequest;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.threadlocal.SerialNo;
import com.doubo.common.util.ConverterUtil;
import com.doubo.common.util.ExceptionUtil;
import com.doubo.common.util.StringUtil;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.util.BeanCopyUtils;
import com.mall.discover.persistence.dao.MemberDiscoverDao;
import com.mall.discover.persistence.entity.MemberDiscoverCateEntity;
import com.mall.discover.persistence.entity.MemberDiscoverEntity;
import com.mall.discover.persistence.vo.DiscoverPictureVo;
import com.mall.discover.request.admin.AdminPageQueryDiscoverListRequest;
import com.mall.discover.request.admin.AdminUpdateDiscoverRequest;
import com.mall.discover.request.api.*;
import com.mall.discover.response.api.MemberDiscoverCateListResponse;
import com.mall.discover.response.api.MemberDiscoverDetailResponse;
import com.mall.discover.response.api.MemberPageQueryDiscoverListResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberDiscoverBiz {

  @Autowired
  private MemberDiscoverDao memberDiscoverDao;

  public CommonResponse<Boolean> submitDiscover(SubmitDiscoverRequest request) {
    try {
      MemberDiscoverEntity memberDiscoverEntity = BeanCopyUtils
          .copyProperties(MemberDiscoverEntity.class, request);
      memberDiscoverEntity.setCreateTime(com.doubo.common.util.DateUtil.getNowDate());
      memberDiscoverEntity.setViews(0);
      memberDiscoverEntity.setForwards(0);
      memberDiscoverEntity.setRecomend(false);
      memberDiscoverEntity.setDisplay(true);
      memberDiscoverDao.saveMemberDiscover(memberDiscoverEntity);
      return ResponseManage.success(Boolean.TRUE);
    } catch (Exception e) {
      log.error("[{}]发布动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("发布动态失败");
  }

  public CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> pageDiscover(
      MemberPageQueryDiscoverListRequest request) {
    try {
      Map queryMap = BeanUtils.describe(request);
      queryMap.put("pageSize", request.getPageSize());
      queryMap.put("pageNow", request.getCurrentPage());
      Long count = memberDiscoverDao.count(queryMap);
      List<MemberDiscoverEntity> memberDiscoveraEntities = memberDiscoverDao.pageDiscover(queryMap);
      List<MemberPageQueryDiscoverListResponse> datas = BeanCopyUtils
          .copyList(MemberPageQueryDiscoverListResponse.class, memberDiscoveraEntities);
      return ResponseManage.success(ConverterUtil.convertToPageResult(datas, count.intValue(),
          ConverterUtil.calculateTotalPage(request.getPageSize(), count.intValue())));
    } catch (Exception e) {
      log.error("[{}]分页查询动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("分页查询动态失败");
  }

  public CommonResponse<Integer> findShareCount(String productId) {
    Map queryMap = new HashMap();
    queryMap.put("productId", productId);
    int count = (int) memberDiscoverDao.count(queryMap);
    return ResponseManage.success(count);
  }

  public CommonResponse<CommonPageResult<MemberPageQueryDiscoverListResponse>> adminPageDiscover(
      AdminPageQueryDiscoverListRequest request) {
    try {
      Map queryMap = BeanUtils.describe(request);
      queryMap.put("pageSize", request.getPageSize());
      queryMap.put("pageNow", request.getCurrentPage());
      Long count = memberDiscoverDao.getMemberDiscoverPageCount(queryMap);

      List<MemberDiscoverEntity> list = memberDiscoverDao.getMemberDiscoverPage(queryMap);
      List<MemberPageQueryDiscoverListResponse> datas = BeanCopyUtils
          .copyList(MemberPageQueryDiscoverListResponse.class, list);
      return ResponseManage.success(ConverterUtil.convertToPageResult(datas, count.intValue(),
          ConverterUtil.calculateTotalPage(request.getPageSize(), count.intValue())));
    } catch (Exception e) {
      log.error("[{}]分页查询动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("分页查询动态失败");
  }

  public CommonResponse<MemberDiscoverDetailResponse> getDetail(PrimarySeqRequest request) {
    try {
      MemberDiscoverEntity entity = memberDiscoverDao.findById(request.getId());
      MemberDiscoverDetailResponse memberDiscoverDetailResponse = BeanCopyUtils
          .copyProperties(MemberDiscoverDetailResponse.class, entity);
      return ResponseManage.success(memberDiscoverDetailResponse);
    } catch (Exception e) {
      log.error("[{}]获取动态详情失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("获取动态详情失败");
  }

  public CommonResponse<Integer> getProductShareCount(PrimaryIdRequest request) {
    try {
      int productShareCount = memberDiscoverDao.getProductShareCount(request.getId().intValue());
      return ResponseManage.success(productShareCount);
    } catch (Exception e) {
      log.error("[{}]获取动态详情失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("获取动态详情失败");
  }

  public CommonResponse<Boolean> updateByPrimaryKeySelective(AdminUpdateDiscoverRequest request) {
    try {
      Map map = BeanUtils.describe(request);
      long count = memberDiscoverDao.updateByPrimaryKeySelective(map);
      //response.setResult(count > 0 ? Boolean.TRUE : Boolean.FALSE);
      return ResponseManage.successIfNotNull(count > 0 ? Boolean.TRUE : null, "更新发现内容失败");
    } catch (Exception e) {
      log.error("[{}]更新发现内容失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("更新发现内容失败");
  }

  public CommonResponse<Boolean> views(PrimarySeqRequest request) {
    try {
      long count = memberDiscoverDao.updateViews(request.getId());
      //response.setResult(count > 0 ? Boolean.TRUE : Boolean.FALSE);

      return ResponseManage.successIfNotNull(count > 0 ? Boolean.TRUE : null, "动态浏览人数增加失败");
    } catch (Exception e) {
      log.error("[{}]动态浏览人数增加失败, Exception:{}", SerialNo.getSerialNo(),
          ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("动态浏览人数增加失败");
  }

  public CommonResponse<Boolean> forward(PrimarySeqRequest request) {
    try {
      long count = memberDiscoverDao.updateForward(request.getId());
      MemberDiscoverEntity memberDiscoverEntity = memberDiscoverDao.findById(request.getId());
      if (!StringUtil.isNull(memberDiscoverEntity.getVideo())) {
        memberDiscoverDao.incProductShare(memberDiscoverEntity.getProductId());
      } else {
        List<DiscoverPictureVo> discoverPictureVos = JSON
            .parseArray(memberDiscoverEntity.getPictures(), DiscoverPictureVo.class);
        discoverPictureVos.stream().map(DiscoverPictureVo::getProductId).distinct()
            .filter(o -> !StringUtil.isNull(o)).collect(Collectors.toList()).forEach(p -> {
          memberDiscoverDao.incProductShare(p);
        });
      }
      //response.setResult(count > 0 ? Boolean.TRUE : Boolean.FALSE);
      return ResponseManage.successIfNotNull(count > 0 ? Boolean.TRUE : null, "转发动态失败");
    } catch (Exception e) {
      log.error("[{}]转发动态失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("转发动态失败");
  }

  public CommonResponse<Boolean> submitCate(SubmitCateRequest request) {
    CommonResponse<Boolean> response = new CommonResponse<>();
    try {
      MemberDiscoverCateEntity memberDiscoverCateEntity = BeanCopyUtils
          .copyProperties(MemberDiscoverCateEntity.class, request);
      memberDiscoverCateEntity.setCreateTime(com.doubo.common.util.DateUtil.getNowDate());
      memberDiscoverCateEntity.setDisplay(1);
      memberDiscoverDao.saveMemberDiscoverCate(memberDiscoverCateEntity);
      //response.setResult(Boolean.TRUE);
      return ResponseManage.success(Boolean.TRUE);
    } catch (Exception e) {
      log.error("[{}]保存发现分类失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("保存发现分类失败");
  }

  public CommonResponse<List<MemberDiscoverCateListResponse>> listCate(ListCateRequest request) {
    try {

      List<MemberDiscoverCateEntity> memberDiscoverCateEntities = memberDiscoverDao.listCate();
      List<MemberDiscoverCateListResponse> list = BeanCopyUtils
          .copyList(MemberDiscoverCateListResponse.class, memberDiscoverCateEntities);
      return ResponseManage.success(list);
    } catch (Exception e) {
      log.error("[{}]查询发现分类失败, Exception:{}", SerialNo.getSerialNo(), ExceptionUtil.getAsString(e));
    }
    return ResponseManage.fail("查询发现分类失败");
  }

}
