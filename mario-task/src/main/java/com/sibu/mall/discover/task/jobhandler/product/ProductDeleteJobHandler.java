package com.mall.discover.task.jobhandler.product;

import com.doubo.common.model.response.CommonResponse;
import com.doubo.json.util.JsonUtil;
import com.mall.common.exception.BusinessException;
import com.mall.discover.service.ProductService;
import com.mall.discover.task.common.constants.DubboConstants;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;


/**
 * 未关联文章/未设置爆款和高佣金的商品隐藏显示
 */
@JobHandler(value = "productDeleteJobHandler")
@Service
public class ProductDeleteJobHandler extends IJobHandler {

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    ProductService productService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        CommonResponse<Boolean> response = productService.scheduleDeleteProductEntity();
        if (!response.isSuccess()) {
            XxlJobLogger.log("调用[productService][scheduleDeleteProductEntity]dubbo 接口出错,request:{},response:{}",
                    JsonUtil.toJson(null), JsonUtil.toJson(response));
            throw new BusinessException(response.getErrorMsg());
        }
        return SUCCESS;
    }
}
