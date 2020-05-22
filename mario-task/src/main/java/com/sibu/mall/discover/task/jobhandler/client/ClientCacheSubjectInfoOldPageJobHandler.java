package com.mall.discover.task.jobhandler.client;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.json.util.JsonUtil;
import com.mall.common.exception.BusinessException;
import com.mall.discover.request.client.ClientSubjectArticlePageRequest;
import com.mall.discover.request.client.ClientSubjectPageRequest;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientSubjectPageResponse;
import com.mall.discover.service.ClientSubjectService;
import com.mall.discover.task.common.constants.DubboConstants;
import com.mall.discover.task.config.properties.DiscoverProperties;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: huangzhong
 * @Date: 2019/10/15
 * @Description: 缓存话题列表
 */
@JobHandler(value = "clientCacheSubjectInfoOldPageJobHandler")
@Service
public class ClientCacheSubjectInfoOldPageJobHandler extends IJobHandler {
    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientSubjectService clientSubjectService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        ClientSubjectArticlePageRequest request = new ClientSubjectArticlePageRequest();
        request.setSubjectId(40L);
        CommonResponse<CommonPageResult<ClientArticleResponse>> response = clientSubjectService.querySubjectArticlePageOld(request, true);
        if (!response.isSuccess()) {
            XxlJobLogger.log("调用[ClientSubjectService][querySubjectArticlePageOld]dubbo 接口出错,request:{},response:{}",
                    JsonUtil.toJson(request), JsonUtil.toJson(response));
            throw new BusinessException(response.getErrorMsg());
        }
        return SUCCESS;
    }
}
