package com.mall.discover.persistence.dao.mysql;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.discover.persistence.bo.*;
import com.mall.discover.persistence.entity.product.ProductEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productDao")
public interface ProductDao extends BaseMapper<ProductEntity> {
    /**
     * dp.product_id,dp.product_no,dp.product_name,dp.share_count,dp.mall_count,dp.commission,dp.hot_product_status," +
     * "dp.high_commission_status,dp.mast_img
     *
     * @param productId
     * @return
     */
    @Select("select dp.*,dc.read_count,dc.share_count,dc.sort,count(dr.id) as articleNums from discover_product as dp " +
            "LEFT JOIN discover_count dc ON dp.product_id = dc.bizId " +
            "LEFT JOIN discover_relation dr ON dp.product_id=dr.from_id " +
            "where dc.biztype=2 and dr.biztype=1 and dp.product_id=#{id}")
    ProductInfoBO getProductInfo(@Param("id") Long productId);


    void productEdit(ProductEditBO request);

    /**
     * 获取商品内文章列表
     */
    List<ProductArticleBO> queryProductArticlePage(@Param("param") ProductArticlePageRequestBO bo,
                                                   @Param("startRecord") int startRecord,
                                                   @Param("recordSize") int recordSize);

    /**
     * 获取商品内文章数量
     */
    int queryProductArticlePageCount(@Param("param") ProductArticlePageRequestBO bo);

    /**
     * 商品列表查询
     *
     * @param bo
     * @param startRecord
     * @param recordSize
     * @return
     */
    List<ProductInfoBO> listProduct(@Param("param") ProductListRequestBO bo,
                                    @Param("startRecord") int startRecord,
                                    @Param("recordSize") int recordSize);

    //    @Options(useGeneratedKeys = true, keyProperty = "productId", keyColumn = "product_id")
    @Insert("insert into discover_product(product_id,product_name,product_no,mall_count,commission,price,hot_product_status,product_category," +
            "high_commission_status,master_img,is_deleted,create_user_id,update_time,create_time) " +
            "values(#{productId},#{productName},#{productNo},#{mallCount},#{commission},#{price},#{hotProductStatus},#{productCategory}," +
            "#{highCommissionStatus},#{masterImg},#{status},#{createUser},#{updateTime},#{createTime})")
    void save(ProductEntity productEntity);

    @Select("select product_id from discover_product where product_no = #{productNo}")
    String selectIdByCode(@Param("productNo") String productNo);

    @Select("select * from discover_product where product_no =#{param}")
    ProductEntity selectByProductNo(@Param("param") String productNo);

    int listProductCount(@Param("param") ProductListRequestBO bo);

    @Select("select count(product_id) from discover_product where is_deleted=0")
    int countEntity();

    List<ProductEntity> queryProductPage(@Param("startRecord") int startRecord,
                                         @Param("recordSize") int recordSize);


    @Select("select product_no from discover_product where product_id=#{id}")
    String searchNoByID(@Param("id") Long id);

    void batchUpate(@Param("param") List<CountReadShareUpdateBO> bo);

    @Select("select product_id from discover_product where high_commission_status=0 and hot_product_status=0")
    List<Integer> selectColdProduct();

    @Update("update discover_product set is_deleted=1 where product_id=#{id}")
    void deleteProduct(@Param("id") Integer productId);
}
