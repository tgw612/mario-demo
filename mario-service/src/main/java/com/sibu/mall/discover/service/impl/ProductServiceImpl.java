package com.mall.discover.service.impl;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.product.*;
import com.mall.discover.response.product.ProductArticleResponse;
import com.mall.discover.response.product.ProductInfoResponse;
import com.mall.discover.service.ProductService;
import com.mall.discover.service.impl.biz.ProductServiceBiz;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(
        group = "${dubbo.provider.group}",
        version = "${dubbo.provider.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductServiceBiz productServiceBiz;

    @Override
    public CommonResponse<Boolean> addOrEditProduct(ProductAddOrEditRequestDto request) {
        return productServiceBiz.addOrEditProduct(request);
    }

    @Override
    public CommonResponse<CommonPageResult<ProductInfoResponse>> listProduct(ProductListRequestDto request) {
        return productServiceBiz.listProduct(request);
    }

    @Override
    public CommonResponse<ProductInfoResponse> getProductInfo(Long productId) {
        return productServiceBiz.getProductInfo(productId);
    }

    @Override
    public CommonResponse<CommonPageResult<ProductArticleResponse>> queryProductArticlePage(ProductArticlePageRequestDto request) {
        return productServiceBiz.queryProductArticlePage(request);
    }

    @Override
    public CommonResponse<Boolean> productSortEdit(ProductSortEditRequestDto request) {
        return productServiceBiz.productSortEdit(request);
    }

    @Override
    public CommonResponse<Boolean> productEdit(ProductEditRequestDto request) {
        return productServiceBiz.productEdit(request);
    }

    @Override
    public CommonResponse<Boolean> deleteProductArticle(ProductDeleteArticleRequestDto request) {
        return productServiceBiz.deleteProductArticle(request);
    }

    @Override
    public CommonResponse<Boolean> scheduleUpdateProductEntity() {
        return productServiceBiz.scheduleUpdateProductEntityV2();
    }

    @Override
    public CommonResponse<Boolean> updateReadShareCount() {
        return productServiceBiz.updateReadShareCount();
    }

    @Override
    public CommonResponse<Boolean> scheduleDeleteProductEntity() {
        return productServiceBiz.scheduleDeleteProductEntity();
    }
}
