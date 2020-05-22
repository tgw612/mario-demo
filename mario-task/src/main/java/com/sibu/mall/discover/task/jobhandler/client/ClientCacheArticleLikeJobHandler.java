package com.mall.discover.task.jobhandler.client;

import com.doubo.common.model.response.CommonResponse;
import com.doubo.json.util.JsonUtil;
import com.mall.common.exception.BusinessException;
import com.mall.discover.common.enums.RelationTypeEnum;
import com.mall.discover.service.ClientLikeService;
import com.mall.discover.task.common.constants.DubboConstants;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @author: huangzhong
 * @Date: 2019/10/15
 * @Description: 缓存文章列表
 */
@JobHandler(value = "clientCacheArticleLikeJobHandler")
@Service
public class ClientCacheArticleLikeJobHandler extends IJobHandler {

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    private ClientLikeService clientLikeService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        CommonResponse<Boolean> booleanCommonResponse = clientLikeService.updateUserLike(RelationTypeEnum.USER_LIKE_ARTICLE.getCode());
        if (!booleanCommonResponse.isSuccess()) {
            XxlJobLogger.log("调用[ClientLikeService][updateUserLike]dubbo 接口出错,request:{},response:{}",
                    JsonUtil.toJson(RelationTypeEnum.USER_LIKE_ARTICLE.getCode()), JsonUtil.toJson(booleanCommonResponse));
            throw new BusinessException(booleanCommonResponse.getErrorMsg());
        }
        return SUCCESS;
    }
}
