package com.mall.discover.service;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.client.ClientSubjectArticlePageRequest;
import com.mall.discover.request.client.ClientSubjectInfoRequest;
import com.mall.discover.request.client.ClientSubjectPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientSubjectInfoResponse;
import com.mall.discover.response.client.ClientSubjectPageResponse;

/**
 * @author: huangzhong
 * @Date: 2019/10/7
 * @Description: 客户端话题服务
 */
public interface ClientSubjectService {

    /**
     * 话题列表分页
     * @param cache 定时任务使用，true为缓存较多数据
     * @return
     */
    CommonResponse<CommonPageResult<ClientSubjectPageResponse>> querySubjectPage(ClientSubjectPageRequest request, boolean cache);

    /**
     *  话题详情
     * @param cache 定时任务使用，true为缓存较多数据
     * @return
     */
    CommonResponse<ClientSubjectInfoResponse> querySubjectInfo(ClientSubjectInfoRequest request, boolean cache);

    /**
     * 话题内文章列表
     */
    CommonResponse<CommonPageResult<ClientArticleResponse>> querySubjectArticlePage(ClientSubjectArticlePageRequest request);

    /**
     * 话题内文章列表,兼容老版本
     */
    CommonResponse<CommonPageResult<ClientArticleResponse>> querySubjectArticlePageOld(ClientSubjectArticlePageRequest request, boolean cache);
}
