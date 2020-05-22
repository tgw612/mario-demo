package com.mall.discover.task.jobhandler.article;

import com.doubo.common.model.response.CommonResponse;
import com.doubo.json.util.JsonUtil;
import com.mall.common.exception.BusinessException;
import com.mall.discover.service.ArticleService;
import com.mall.discover.task.common.constants.DubboConstants;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * 定时更新未发布的文章为发布状态
 */
@JobHandler(value = "publishArticleHandler")
@Service
@Slf4j
public class PublishArticleHandler extends IJobHandler {

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    ArticleService articleService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        CommonResponse<Boolean> response = articleService.publishArticle();
        if (!response.isSuccess()) {
            XxlJobLogger.log("调用[articleService][publishArticle]dubbo 接口出错,request:{},response:{}",
                    JsonUtil.toJson(System.currentTimeMillis()), JsonUtil.toJson(response));
            throw new BusinessException(response.getErrorMsg());
        }
        return SUCCESS;
    }
}
