package com.mall.discover.service;

import com.doubo.common.model.CommonPageResult;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.product.*;
import com.mall.discover.response.product.ProductArticleResponse;
import com.mall.discover.response.product.ProductInfoResponse;

public interface ProductService {

    /**
     * 新增or更新商品（内部接口）
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> addOrEditProduct(ProductAddOrEditRequestDto request);

    /**
     * 商品列表
     *
     * @param request
     * @return
     */
    CommonResponse<CommonPageResult<ProductInfoResponse>> listProduct(ProductListRequestDto request);

    /**
     * 商品详情
     *
     * @return
     */
    CommonResponse<ProductInfoResponse> getProductInfo(Long productId);

    /**
     * 商品内文章列表分页
     */
    CommonResponse<CommonPageResult<ProductArticleResponse>> queryProductArticlePage(ProductArticlePageRequestDto request);

    /**
     * 修改商品排序
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> productSortEdit(ProductSortEditRequestDto request);

    /**
     * 修改爆款状态、高佣金状态
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> productEdit(ProductEditRequestDto request);

    /**
     * 删除商品内文章
     *
     * @param request
     * @return
     */
    CommonResponse<Boolean> deleteProductArticle(ProductDeleteArticleRequestDto request);

    /**
     * 更新商品表 价格/佣金/销量/图片/名称
     *
     * @return
     */
    CommonResponse<Boolean> scheduleUpdateProductEntity();

    /**
     * 定时任务调用更新ReadShareCount
     *
     * @return
     */
    CommonResponse<Boolean> updateReadShareCount();

    /**
     * 未关联文章/未设置爆款和高佣金的商品隐藏显示  is_deleted=1
     *
     * @return
     */
    CommonResponse<Boolean> scheduleDeleteProductEntity();

}
