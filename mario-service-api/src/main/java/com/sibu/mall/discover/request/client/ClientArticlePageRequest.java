package com.mall.discover.request.client;

import com.doubo.common.model.request.PageQueryRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: huangzhong
 * @Date: 2019/9/27
 * @Description:
 */
@ApiModel(value = "文章列表")
@Data
public class ClientArticlePageRequest extends PageQueryRequest {

}
