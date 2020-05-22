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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 任务Handler示例（Bean模式）
 *	商品信息定时更新
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 */
@JobHandler(value = "productUpdateJobHandler")
@Service
public class ProductUpdateJobHandler extends IJobHandler {

    @Reference(consumer = DubboConstants.SIBU_MALL_DISCOVER_CONSUMER)
    ProductService productService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        CommonResponse<Boolean> response = productService.scheduleUpdateProductEntity();
        if (!response.isSuccess()) {
            XxlJobLogger.log("调用[productService][scheduleUpdateProductEntity]dubbo 接口出错,request:{},response:{}",
                    JsonUtil.toJson(null), JsonUtil.toJson(response));
            throw new BusinessException(response.getErrorMsg());
        }
        return SUCCESS;
    }
}
