package com.mall.discover.persistence.dao.mysql;

import com.mall.discover.persistence.bo.ArticleBO;
import com.mall.discover.persistence.bo.ProductBO;
import com.mall.discover.persistence.vo.ProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Repository
public interface ClientProductMapper {

    /**
     * 获取高佣金商品列表
     */
    List<ProductBO> queryHighProductPage(@Param("param") ProductVo productVo,
                                            @Param("startRecord") int startRecord,
                                            @Param("recordSize") int recordSize);

    /**
     * 获取高佣金商品列表数量
     */
    Integer queryHighProductPageCount(@Param("param") ProductVo productVo);

    /**
     * 获取爆款商品列表
     */
    List<ProductBO> queryHotProductPage(@Param("param") ProductVo productVo,
                                        @Param("startRecord") int startRecord,
                                        @Param("recordSize") int recordSize);

    /**
     * 获取爆款商品列表数量
     */
    Integer queryHotProductPageCount(@Param("param") ProductVo productVo);

    /**
     * 通过商品获取文章列表
     */
    List<ArticleBO> queryProductArticlePage(@Param("param") ProductVo productVo,
                                            @Param("startRecord") int startRecord,
                                            @Param("recordSize") int recordSize);

    /**
     * 通过商品获取文章列表count
     */
    Integer queryProductArticlePageCount(@Param("param") ProductVo productVo);

    /**
     * 通过二级类别获取相关商品
     */
    List<ProductBO> queryProductListByCategory(@Param("productCategory") Integer productCategory,
                                                @Param("startRecord") int startRecord,
                                                @Param("recordSize") int recordSize);

    /**
     * 获取商品详情
     */
    ProductBO queryProductInfo(@Param("param") ProductVo productVo);

    /**
     * 获取商品一级分类
     * @param productIds
     * @return
     */
    Set<Integer> queryCategoryIdFirstPage(@Param("list")List<Long> productIds);

    /**
     * 根据商品一级分类获取商品id
     * @param categoryIdList
     * @return
     */
    List<Long> queryProductPageByCategoryIdFirst(@Param("list") Set<Integer> categoryIdList);

    /**
     * 获取商品id编号对应关系
     * @return
     */
    List<ProductBO> queryProductIdNoList();
}
