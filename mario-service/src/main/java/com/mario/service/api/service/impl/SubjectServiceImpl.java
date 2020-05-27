package com.mario.service.api.service.impl;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.exception.BusinessException;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.subject.SubjectLook;
import com.mall.discover.common.util.BeanCopyUtils;
import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.persistence.dao.mysql.DiscoverSubjectMapper;
import com.mall.discover.persistence.dao.mysql.RelationMapper;
import com.mall.discover.request.subject.*;
import com.mall.discover.response.subject.*;
import com.mall.discover.service.SubjectService;
import com.mario.service.api.service.impl.biz.SubjectBiz;
import com.mario.service.api.util.DiscoverUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Slf4j
@Service(
    group = "${dubbo.provider.group}",
    version = "${dubbo.provider.version}",
    application = "${dubbo.application.id}",
    protocol = "${dubbo.protocol.id}",
    registry = "${dubbo.registry.id}"
)
public class SubjectServiceImpl implements SubjectService {

  @Autowired
  DiscoverSubjectMapper subjectMapper;
  @Autowired
  RelationMapper relationMapper;
  @Autowired
  private SubjectBiz subjectBiz;
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public CommonResponse<Boolean> subjectSubmit(SubjectSubmitRequestDto request) {
    if (request.getSubjectId() != null && request.getSubjectId() > 0) {
      Long publishTime = subjectMapper.queryPublishTime(request.getSubjectId());
      int countSubjectArticle = relationMapper.countSubjectArticle(request.getSubjectId());
      if (publishTime != null && !publishTime.equals(request.getPublishTime())
          && countSubjectArticle > 0) {
        return ResponseManage.fail("话题关联文章后不可编辑发布时间");
      }
    }
    Boolean result = subjectBiz.subjectSubmit(request);
    if (result == null || !result) {
      return ResponseManage.fail("新建话题失败");
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<CommonPageResult<SubjectResponse>> querySubjectPage(
      SubjectPageRequestDto request) {
    CommonPageResult<SubjectBO> subjectCommonPageResult = subjectBiz.querySubjectPage(request);
    CommonPageResult<SubjectResponse> result = new CommonPageResult<>();
    //封装返回值
    if (subjectCommonPageResult.getData().size() > 0) {
      List<SubjectBO> bolist = subjectCommonPageResult.getData();
      List<SubjectResponse> subjectResponses = new ArrayList<>(bolist.size());
      SubjectResponse subjectResponse = null;
      for (SubjectBO bo : bolist) {
        subjectResponse = BeanCopyUtils.copyProperties(SubjectResponse.class, bo);
        try {
          subjectResponse
              .setSubjectLook(objectMapper.readValue(bo.getSubjectLook(), SubjectLook.class));
        } catch (IOException e) {
          e.printStackTrace();
//                    throw new BusinessException("返回值转换失败");
        }
        if (ObjectUtil.isNotNull(subjectResponse)) {
          subjectResponses.add(subjectResponse);
        }
      }
      result.setData(subjectResponses);
    }

    result.setTotalCount(subjectCommonPageResult.getTotalCount());
    result.setTotalPage(subjectCommonPageResult.getTotalPage());
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<SubjectInfoResponse> querySubjectInfo(SubjectInfoRequestDto request) {
    SubjectBO subject = subjectBiz.querySubjectInfo(request);
    SubjectInfoResponse result = new SubjectInfoResponse();
    if (subject == null) {
      return ResponseManage.success(result);
    }
    BeanUtils.copyProperties(subject, result);
    try {
      SubjectLook subjectLook = objectMapper.readValue(subject.getSubjectLook(), SubjectLook.class);
      result.setSubjectLook(subjectLook);
    } catch (IOException e) {
      throw new BusinessException("返回值转换失败");
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<CommonPageResult<SubjectArticleResponse>> querySubjectArticlePage(
      SubjectArticlePageRequestDto request) {
    CommonPageResult<SubjectArticleResponse> subjectArticleResponseCommonPageResult = subjectBiz
        .querySubjectArticlePage(request);
    return ResponseManage.success(subjectArticleResponseCommonPageResult);
  }

  @Override
  public CommonResponse<Boolean> deleteSubject(SubjectDeleteRequestDto request) {
    Boolean result = subjectBiz.deleteSubject(request);
    if (result == null || !result) {
      return ResponseManage.fail("有关联文章，无法删除");
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<Boolean> deleteSubjectArticle(SubjectArticleDeleteRequestDto request) {
    Boolean result = subjectBiz.deleteSubjectArticle(request);
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<CommonPageResult<SubjectNameResponse>> querySubjectByName(
      SubjectNameRequestDto request) {
    CommonPageResult<SubjectBO> subjectList = subjectBiz.querySubjectByName(request);
    CommonPageResult<SubjectNameResponse> result = new CommonPageResult<>();
    //封装返回值
    if (subjectList.getData().size() > 0) {
      result = DiscoverUtil.convertResponse(subjectList, CommonPageResult.class);
    }
    return ResponseManage.success(result);
  }

  @Override
  public CommonResponse<ArticleHistorySubjectResponse> articleHistorySubject() {
    return subjectBiz.articleHistorySubject();
  }


}
