package com.mall.discover.task.jobhandler.client;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.json.util.JsonUtil;
import com.mall.common.exception.BusinessException;
import com.mall.discover.request.client.ClientArticlePageRequest;
import com.mall.discover.response.client.ClientDiscoverResponse;
import com.mall.discover.service.ClientArticleService;
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
 * @Description: 缓存文章列表
 */
@JobHandler(value = "clientCacheArticleOldPageJobHandler")
@Service
public class ClientCacheArticleOldPageJobHandler extends IJobHandler {

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientArticleService clientArticleService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        ClientArticlePageRequest request = new ClientArticlePageRequest();
        request.setCurrentPage(1);
        CommonResponse<CommonPageResult<ClientDiscoverResponse>> response = clientArticleService.queryArticlePageOld(request, true);
        if (!response.isSuccess()) {
            XxlJobLogger.log("调用[ClientArticleService][queryArticlePageOld]dubbo 接口出错,request:{},response:{}",
                    JsonUtil.toJson(request), JsonUtil.toJson(response));
            throw new BusinessException(response.getErrorMsg());
        }
        return SUCCESS;
    }
}
