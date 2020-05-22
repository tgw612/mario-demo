package com.mall.discover.task.jobhandler.client;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.json.util.JsonUtil;
import com.mall.common.exception.BusinessException;
import com.mall.discover.request.client.ClientProductPageRequest;
import com.mall.discover.response.client.ClientProductPageResponse;
import com.mall.discover.service.ClientProductService;
import com.mall.discover.task.common.constants.DubboConstants;
import com.mall.discover.task.config.properties.DiscoverProperties;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author: huangzhong
 * @Date: 2019/10/15
 * @Description: 缓存爆款商品列表
 */
@JobHandler(value = "clientCacheHotProductPageJobHandler")
@Service
public class ClientCacheHotProductPageJobHandler extends IJobHandler {

    @Autowired
    private DiscoverProperties discoverProperties;

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientProductService clientProductService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        ClientProductPageRequest request = new ClientProductPageRequest();
        Integer cacheMaxPage = discoverProperties.getCacheMaxPage();

        for (int i = 1; i <= cacheMaxPage; i++) {
            request.setCurrentPage(i);
            CommonResponse<CommonPageResult<ClientProductPageResponse>> response = clientProductService.queryHotProductPage(request, true);
            if (!response.isSuccess()) {
                XxlJobLogger.log("调用[ClientProductService][queryHotProductPage]dubbo 接口出错,request:{},response:{}",
                        JsonUtil.toJson(request), JsonUtil.toJson(response));
                throw new BusinessException(response.getErrorMsg());
            }
        }
        return SUCCESS;
    }
}
