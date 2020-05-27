package com.mario.service.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.exception.BusinessException;
import com.mall.discover.common.PictureLook;
import com.mall.discover.common.VodLook;
import com.mall.discover.common.article.ArticleLook;
import com.mall.discover.common.enums.ArticleLookTypeEnum;
import com.mall.discover.common.enums.BizTypeEnum;
import com.mall.discover.common.enums.RelationTypeEnum;
import com.mall.discover.common.subject.SubjectLook;
import com.mall.discover.persistence.bo.ArticleBO;
import com.mall.discover.persistence.bo.SubjectBO;
import com.mall.discover.persistence.util.SpringUtil;
import com.mall.discover.persistence.vo.ArticleVo;
import com.mall.discover.persistence.vo.ProductVo;
import com.mall.discover.persistence.vo.SubjectVo;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientSubjectResponse;
import com.mario.service.api.service.impl.biz.ClientArticleBiz;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * @author: huangzhong
 * @Date: 2019/10/9
 * @Description: 发现页公共提取方法
 */
public class DiscoverUtil {

  private static ObjectMapper objectMapper;
  private static ClientArticleBiz clientArticleBiz;

  static {
    objectMapper = SpringUtil.getBean(ObjectMapper.class);
    clientArticleBiz = SpringUtil.getBean(ClientArticleBiz.class);
  }


  /**
   * 转换
   */
  public static <T> T convertResponse(Object resource, Class<T> target) {
    T t;
    try {
      if (resource == null) {
        return target.newInstance();
      }
      String value = objectMapper.writeValueAsString(resource);
      t = objectMapper.readValue(value, target);
    } catch (Exception e) {
      throw new BusinessException("返回值转换失败");
    }
    return t;
  }

  /**
   * 转换字符串
   */
  public static <T> T convertStringResponse(String resource, Class<T> target) {
    T t;
    try {
      if (resource == null) {
        return target.newInstance();
      }
      t = objectMapper.readValue(resource, target);
    } catch (Exception e) {
      throw new BusinessException("返回值转换失败");
    }
    return t;
  }

  /**
   * 计算图片宽高比
   */
  public static void computerWhRatio(PictureLook pictureLook) {
    if (pictureLook == null) {
      return;
    }
    Integer height = pictureLook.getHeight();
    Integer width = pictureLook.getWidth();
    if (height == null || width == null || height <= 0 || width <= 0) {
      return;
    }
    BigDecimal divide = new BigDecimal(width.toString())
        .divide(new BigDecimal(height), 2, BigDecimal.ROUND_HALF_UP);
    pictureLook.setWhRatio(divide);
  }

  /**
   * 计算佣金比例
   */
  public static String computerCommissionRatio(BigDecimal price, BigDecimal commission) {
    BigDecimal zero = new BigDecimal(0);
    if (price == null || commission == null || price.compareTo(zero) <= 0) {
      return "0.00%";
    }
    BigDecimal divide = commission.multiply(new BigDecimal(100))
        .divide(price, 2, BigDecimal.ROUND_HALF_UP);
    return divide.stripTrailingZeros().toPlainString() + "%";
  }

  /**
   * 获取文章第一张图片
   */
  public static PictureLook getArticleFirstPicture(String articleLookStr) {
    //获取第一张图片信息
    ArticleLook articleLook = convertStringResponse(articleLookStr, ArticleLook.class);
    if (articleLook.getPictureLookList() != null) {
      PictureLook pictureLook = articleLook.getPictureLookList().stream()
          .min(Comparator.comparing(PictureLook::getSort))
          .orElseGet(PictureLook::new);
      computerWhRatio(pictureLook);
      return pictureLook;
    }
    return new PictureLook();
  }

  /**
   * 获取第一张图片，老版本兼容
   *
   * @return
   */
  public static PictureLook getArticleFirstPictureOld(String articleLookStr) {
    ArticleLook articleLook = DiscoverUtil.convertStringResponse(articleLookStr, ArticleLook.class);

    //老版APP前端有个bug，需要规避掉
    if (ObjectUtils.isEmpty(articleLook.getPictureLookList())) {
      return null;
    }
    PictureLook pictureLook = articleLook.getPictureLookList().stream()
        .min(Comparator.comparing(PictureLook::getSort))
        .orElseGet(PictureLook::new);
    DiscoverUtil.computerWhRatio(pictureLook);
    return pictureLook;
  }


  public static VodLook getVodLook(String articleLookStr) {
    //获取第一张图片信息
    ArticleLook articleLook = convertStringResponse(articleLookStr, ArticleLook.class);
    if (articleLook.getVodLookList() != null && !CollectionUtils
        .isEmpty(articleLook.getVodLookList())) {
      return articleLook.getVodLookList().get(0);
    }
    return new VodLook();
  }

  /**
   * 获取话题第一张图片URL
   *
   * @param subject
   * @return
   */
  public static String getSubjectPictureUrl(SubjectBO subject) {
    if (subject == null) {
      return "";
    }
    SubjectLook subjectLook = DiscoverUtil
        .convertStringResponse(subject.getSubjectLook(), SubjectLook.class);
    List<PictureLook> pictureLookList = subjectLook.getPictureLookList();
    if (pictureLookList != null && pictureLookList.size() > 0) {
      //话题显示一张图片
      PictureLook pictureLook = pictureLookList.stream()
          .min(Comparator.comparing(PictureLook::getSort)).orElseGet(PictureLook::new);
      return pictureLook.getPictureUrl();
    }
    return "";
  }

  /**
   * 文章详情转换
   *
   * @param article
   * @return
   */
  public static ClientArticleResponse getClientArticleResponse(ArticleBO article) {
    ClientArticleResponse response = convertResponse(article, ClientArticleResponse.class);

    //获取第一张图片信息
    PictureLook pictureLook = getArticleFirstPicture(article.getArticleLook());
    response.setPictureLook(pictureLook);

    response.setVodLook(getVodLook(article.getArticleLook()));

    //阅读数转换
    String readCountString = getReadCountString(response.getReadCount());
    response.setViewCountString(readCountString);

    //获取相关话题（只要名称和id）
    List<ClientSubjectResponse> subjectList = getClientSubjectResponseList(article.getArticleId());
    response.setSubjectList(subjectList);
    return response;
  }

  /**
   * 文章详情转换  兼容老版本
   *
   * @param article
   * @return
   */
  public static ClientArticleResponse getClientArticleResponseOld(ArticleBO article) {
    ClientArticleResponse response = convertResponse(article, ClientArticleResponse.class);

    //获取第一张图片信息
    PictureLook pictureLook = DiscoverUtil.getArticleFirstPictureOld(article.getArticleLook());
    if (pictureLook == null) {
      return null;
    }
    response.setPictureLook(pictureLook);

    //阅读数转换
    String readCountString = getReadCountString(response.getReadCount());
    response.setViewCountString(readCountString);

    //获取相关话题（只要名称和id）
    List<ClientSubjectResponse> subjectList = getClientSubjectResponseList(article.getArticleId());
    response.setSubjectList(subjectList);
    return response;
  }

  /**
   * 阅读数转换
   */
  public static String getReadCountString(Long readCount) {
    if (readCount == null || readCount <= 0) {
      return "0";
    }
    int divide = 10000;
    if (readCount < divide) {
      return String.valueOf(readCount);
    }
    BigDecimal result = new BigDecimal(readCount)
        .divide(new BigDecimal(divide), 1, BigDecimal.ROUND_HALF_UP);
    return result.stripTrailingZeros().toPlainString() + "万";
  }

  /**
   * 通过文章id获取相关话题
   *
   * @param articleId
   * @return
   */
  public static List<ClientSubjectResponse> getClientSubjectResponseList(Long articleId) {
    List<SubjectBO> subjectBOList = clientArticleBiz.queryArticleSubject(articleId);
    List<ClientSubjectResponse> subjectList = new ArrayList<>();
    for (SubjectBO subjectBO : subjectBOList) {
      ClientSubjectResponse subjectResponse = new ClientSubjectResponse();
      subjectResponse.setSubjectId(subjectBO.getSubjectId());
      subjectResponse.setSubjectName(subjectBO.getSubjectName());
      subjectList.add(subjectResponse);
    }
    return subjectList;
  }

  /**
   * 生成话题查询实体类
   *
   * @return
   */
  public static SubjectVo getSubjectVo() {
    SubjectVo subjectVo = new SubjectVo();
    subjectVo.setBizType(BizTypeEnum.SUBJECT.getCode());
    subjectVo.setRelation(RelationTypeEnum.SUBJECT_ARTICLE.getCode());
    return subjectVo;
  }

  /**
   * 生成文章查询实体类
   *
   * @return
   */
  public static ArticleVo getArticleVo() {
    ArticleVo articleVo = new ArticleVo();
    articleVo.setBizType(BizTypeEnum.ARTICLE.getCode());
    return articleVo;
  }

  /**
   * 生成商品查询实体类
   *
   * @return
   */
  public static ProductVo getProductVo() {
    ProductVo productVo = new ProductVo();
    productVo.setBizType(BizTypeEnum.PRODUCT.getCode());
    productVo.setRelation(RelationTypeEnum.GOODS_ARTICLE.getCode());
    return productVo;
  }

  public static boolean checkTotalCount(Integer totalCount) {
    return totalCount != null && totalCount > 0;
  }

  /**
   * 生成ArticleLook类型
   */
  public static Integer getArticleLookType(ArticleLook articleLook) {
    if (ObjectUtils.isEmpty(articleLook)) {
      return ArticleLookTypeEnum.NONE.getCode();
    }

    //仅图片
    if (!ObjectUtils.isEmpty(articleLook.getPictureLookList()) && ObjectUtils
        .isEmpty(articleLook.getVodLookList())) {
      return ArticleLookTypeEnum.ONLY_PICTURE.getCode();
    }

    //仅视频
    if (!ObjectUtils.isEmpty(articleLook.getVodLookList()) && ObjectUtils
        .isEmpty(articleLook.getPictureLookList())) {
      return ArticleLookTypeEnum.ONLY_VIDEO.getCode();
    }

    //图片、视频
    if (!ObjectUtils.isEmpty(articleLook.getPictureLookList()) && !ObjectUtils
        .isEmpty(articleLook.getVodLookList())) {
      return ArticleLookTypeEnum.PICTURE_VIDEO.getCode();
    }

    return ArticleLookTypeEnum.NONE.getCode();
  }
}
