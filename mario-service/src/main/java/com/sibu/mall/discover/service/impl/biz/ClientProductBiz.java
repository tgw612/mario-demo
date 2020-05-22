package com.mall.discover.service.impl.biz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.discover.common.enums.BizTypeEnum;
import com.mall.discover.config.properties.DiscoverProperties;
import com.mall.discover.persistence.bo.ArticleBO;
import com.mall.discover.persistence.bo.ProductBO;
import com.mall.discover.persistence.dao.mysql.ClientProductMapper;
import com.mall.discover.persistence.vo.ProductVo;
import com.mall.discover.persistence.vo.SubjectVo;
import com.mall.discover.request.client.ClientProductInfoRequest;
import com.mall.discover.util.DiscoverUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author: huangzhong
 * @Date: 2019/9/30
 * @Description:
 */
@Slf4j
@Service
public class ClientProductBiz {

    @Autowired
    private ClientProductMapper clientProductMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiscoverProperties discoverProperties;

    /**
     * 获取高佣金商品列表
     * @param currentPage
     * @param highCommissionStatus
     * @return
     */
    public List<ProductBO> queryHighProductPage(int currentPage, Integer highCommissionStatus) {
        //每页条数固定,不需要计算总条数和总页数
        int pageSize = discoverProperties.getClientPageSize();

        //获取商品列表
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setHighCommissionStatus(highCommissionStatus);
        return clientProductMapper.queryHighProductPage(productVo, (currentPage - 1) * pageSize, pageSize);
    }

    /**
     * 获取高佣金商品列表,指定长度
     * @param highCommissionStatus
     * @return
     */
    public List<ProductBO> queryHighProductPage(Integer highCommissionStatus, int startRecord, int recordSize) {
        //获取商品列表
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setHighCommissionStatus(highCommissionStatus);
        return clientProductMapper.queryHighProductPage(productVo, startRecord, recordSize);
    }

    /**
     * 获取高佣金商品列表数量
     * @param highCommissionStatus
     * @return
     */
    public Integer queryHighProductPageCount(Integer highCommissionStatus) {
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setHighCommissionStatus(highCommissionStatus);
        Integer count = clientProductMapper.queryHighProductPageCount(productVo);
        return count == null ? 0 : count;
    }

    /**
     * 获取爆款商品列表
     * @param currentPage
     * @param hotProductStatus
     * @return
     */
    public List<ProductBO> queryHotProductPage(int currentPage, Integer hotProductStatus) {
        //每页条数固定,不需要计算总条数和总页数
        int pageSize = discoverProperties.getClientPageSize();

        //获取商品列表
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setHotProductStatus(hotProductStatus);
        return clientProductMapper.queryHotProductPage(productVo, (currentPage - 1) * pageSize, pageSize);
    }

    /**
     * 获取爆款商品列表,指定长度
     * @param hotProductStatus
     * @return
     */
    public List<ProductBO> queryHotProductPage(Integer hotProductStatus, int startRecord, int recordSize) {
        //获取商品列表
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setHotProductStatus(hotProductStatus);
        return clientProductMapper.queryHotProductPage(productVo, startRecord, recordSize);
    }

    /**
     * 获取爆款商品列表数量
     * @param hotProductStatus
     * @return
     */
    public Integer queryHotProductPageCount( Integer hotProductStatus) {
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setHotProductStatus(hotProductStatus);
        Integer count = clientProductMapper.queryHotProductPageCount(productVo);
        return count == null ? 0 : count;
    }

    /**
     * 获取商品详情
     * @return
     */
    public ProductBO queryProductInfo(ClientProductInfoRequest request) {
        //获取话题列表
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setProductId(request.getProductId());
        productVo.setProductNo(request.getProductNo());
        return clientProductMapper.queryProductInfo(productVo);
    }

    /**
     * 通过商品获取文章列表
     */
    public List<ArticleBO> queryProductArticlePage(Long productId, int startRecord, int recordSize) {
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setProductId(productId);
        return clientProductMapper.queryProductArticlePage(productVo, startRecord, recordSize);
    }

    /**
     * 通过商品获取文章列表count
     */
    public Integer queryProductArticlePageCount(Long productId) {
        ProductVo productVo = DiscoverUtil.getProductVo();
        productVo.setProductId(productId);
        Integer count = clientProductMapper.queryProductArticlePageCount(productVo);
        return count == null ? 0 : count;
    }

    /**
     * 通过二级类别获取相关商品
     */
    public List<ProductBO> queryProductListByCategory(Integer productCategory, int startRecord, int recordSize) {
        return clientProductMapper.queryProductListByCategory(productCategory, startRecord, recordSize);
    }

    /**
     * 查询相关一级商品分类
     */
    public Set<Integer> queryCategoryIdFirstPage(List<Long> productIds) {
        return clientProductMapper.queryCategoryIdFirstPage(productIds);
    }

    /**
     * 根据一级分类查询相关商品
     */
    public List<Long> queryProductPageByCategoryIdFirst(Set<Integer> CategoryIdList) {
        return clientProductMapper.queryProductPageByCategoryIdFirst(CategoryIdList);
    }

    /**
     * 获取商品id编号对应关系
     * @return
     */
    public List<ProductBO> queryProductIdNoList() {
        return clientProductMapper.queryProductIdNoList();
    }
}
