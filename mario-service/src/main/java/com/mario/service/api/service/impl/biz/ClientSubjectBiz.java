package com.mario.service.api.service.impl.biz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.discover.common.util.PageUtils;
import com.mall.discover.persistence.bo.SubjectArticleBO;
import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.persistence.dao.mysql.ClientSubjectMapper;
import com.mall.discover.persistence.vo.SubjectVo;
import com.mall.discover.request.client.ClientSubjectArticlePageRequest;
import com.mall.discover.request.client.ClientSubjectInfoRequest;
import com.mall.discover.request.client.ClientSubjectPageRequest;
import com.mario.service.api.config.properties.DiscoverProperties;
import com.mario.service.api.util.DiscoverUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Slf4j
@Service
public class ClientSubjectBiz {

  @Autowired
  private ClientSubjectMapper clientSubjectMapper;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DiscoverProperties discoverProperties;

  /**
   * 获取话题列表
   *
   * @param request
   * @return
   */
  public List<SubjectBO> querySubjectPage(ClientSubjectPageRequest request) {
    //每页条数固定,不需要计算总条数和总页数
    int pageSize = discoverProperties.getClientPageSize();
    request.setPageSize(pageSize);

    //获取话题列表
    SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
    List<SubjectBO> subjectList = clientSubjectMapper
        .querySubjectPage(subjectVo, PageUtils.getStartPage(request), request.getPageSize());

    //获取文章数量
    List<Long> subjectIdList = subjectList.stream().map(SubjectBO::getSubjectId)
        .collect(Collectors.toList());
    List<SubjectBO> articleCountList = new ArrayList<>();
    if (subjectIdList.size() > 0) {
      articleCountList = clientSubjectMapper.queryArticleCount(subjectVo, subjectIdList);
    }

    return getArticleCount(subjectList, articleCountList);
  }

  /**
   * 获取话题列表count
   *
   * @return
   */
  public Integer querySubjectPageCount() {
    SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
    return clientSubjectMapper.querySubjectPageCount(subjectVo);
  }


  /**
   * 获取话题详情
   *
   * @param request
   * @return
   */
  public SubjectBO querySubjectInfo(ClientSubjectInfoRequest request) {
    SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
    subjectVo.setSubjectId(request.getSubjectId());

    SubjectBO subject = clientSubjectMapper.querySubjectInfo(subjectVo);
    //获取文章数量
    List<SubjectBO> subjectList = clientSubjectMapper
        .queryArticleCount(subjectVo, Collections.singletonList(request.getSubjectId()));
    if (subjectList.size() > 0 && subject != null) {
      subject.setArticleCount(subjectList.get(0).getArticleCount());
    }

    return subject;
  }

  /**
   * 获取话题名称
   *
   * @param request
   * @return
   */
  public SubjectBO querySubjectName(ClientSubjectArticlePageRequest request) {
    SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
    subjectVo.setSubjectId(request.getSubjectId());

    return clientSubjectMapper.querySubjectInfo(subjectVo);
  }

  /**
   * 获取话题内文章列表
   *
   * @param request
   * @return
   */
  public List<SubjectArticleBO> querySubjectArticlePage(ClientSubjectArticlePageRequest request) {
    //每页条数固定,不需要计算总条数和总页数
    int pageSize = discoverProperties.getClientPageSize();
    request.setPageSize(pageSize);

    SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
    subjectVo.setSubjectId(request.getSubjectId());

    List<SubjectArticleBO> subjectArticles = clientSubjectMapper
        .querySubjectArticlePage(subjectVo, PageUtils.getStartPage(request), request.getPageSize());
    return subjectArticles;
  }

  /**
   * 获取话题内文章列表count
   *
   * @param request
   * @return
   */
  public Integer querySubjectArticlePageCount(ClientSubjectArticlePageRequest request) {
    SubjectVo subjectVo = DiscoverUtil.getSubjectVo();
    subjectVo.setSubjectId(request.getSubjectId());

    return clientSubjectMapper.querySubjectArticlePageCount(subjectVo);
  }

  private List<SubjectBO> getArticleCount(List<SubjectBO> subjectList,
      List<SubjectBO> articleCountList) {
    subjectList.forEach(subject -> {
      int articleCount = articleCountList.stream()
          .filter(a -> subject.getSubjectId().equals(a.getSubjectId()))
          .findFirst()
          .orElseGet(SubjectBO::new)
          .getArticleCount();
      subject.setArticleCount(articleCount);
    });
    return subjectList;
  }


  /**
   * 获取话题列表
   *
   * @return
   */
  public List<Long> queryAllSubjectId() {
    return clientSubjectMapper.queryAllSubjectId();
  }
}
