package com.mall.discover.service;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.subject.*;
import com.mall.discover.response.subject.*;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
public interface SubjectService {
    /**
     * 新建or更新话题
     */
    CommonResponse<Boolean> subjectSubmit(SubjectSubmitRequestDto request);

    /**
     * 话题列表分页
     *
     * @param request
     * @return
     */
    CommonResponse<CommonPageResult<SubjectResponse>> querySubjectPage(SubjectPageRequestDto request);

    /**
     * 话题详情
     */
    CommonResponse<SubjectInfoResponse> querySubjectInfo(SubjectInfoRequestDto request);

    /**
     * 话题内文章列表分页
     */
    CommonResponse<CommonPageResult<SubjectArticleResponse>> querySubjectArticlePage(SubjectArticlePageRequestDto request);

    /**
     * 删除话题
     */
    CommonResponse<Boolean> deleteSubject(SubjectDeleteRequestDto request);

    /**
     * 删除话题内文章
     */
    CommonResponse<Boolean> deleteSubjectArticle(SubjectArticleDeleteRequestDto request);

    /**
     * 文章名称搜索框
     */
    CommonResponse<CommonPageResult<SubjectNameResponse>> querySubjectByName(SubjectNameRequestDto request);

    /**
     *  历史话题
     * @return
     */
    CommonResponse<ArticleHistorySubjectResponse> articleHistorySubject();
}
