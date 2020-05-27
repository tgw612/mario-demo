package com.mario.web.util;

import com.alicp.jetcache.Cache;
import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.response.client.ClientArticleResponse;
import com.mall.discover.response.client.ClientLikePageResponse;
import com.mall.discover.response.client.ClientLikeResponse;
import com.mall.discover.service.ClientProductService;
import com.mall.user.request.FindMembersByIdsRequest;
import com.mall.user.response.UserShowResponse;
import com.mall.user.service.UserService;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * @author: huangzhong
 * @Date: 2019/10/30
 * @Description:
 */
public class DiscoverUtil {

  /**
   * 从用户系统中获取用户信息并排序
   *
   * @param userIds
   */
  public static List<ClientLikePageResponse> getUserInfoList(UserService userService,
      List<Integer> userIds) {
    List<ClientLikePageResponse> result = new ArrayList<>();
    if (CollectionUtils.isEmpty(userIds)) {
      return result;
    }

    //从用户系统中获取
    FindMembersByIdsRequest userRequest = new FindMembersByIdsRequest();
    userRequest.setIds(userIds);
    CommonResponse<List<UserShowResponse>> userShowInfoByIds = userService
        .findUserShowInfoByIds(userRequest);
    if (userShowInfoByIds.isSuccess()) {
      List<UserShowResponse> userInfoResult = userShowInfoByIds.getResult();
      userInfoResult.forEach(user -> {
        ClientLikePageResponse response = new ClientLikePageResponse();
        response.setUserId(user.getId());
        response.setIconUrl(user.getHeadImg());
        response.setNickName(user.getNickName());
        result.add(response);
      });
    }
    //排序
    return sortUserInfoList(userIds, result);
  }

  public static List<ClientLikePageResponse> sortUserInfoList(List<Integer> userIds,
      List<ClientLikePageResponse> userInfoList) {
    List<ClientLikePageResponse> result = new ArrayList<>();
    if (!CollectionUtils.isEmpty(userIds) && !CollectionUtils.isEmpty(userInfoList)) {
      userIds.forEach(userId -> {
        ClientLikePageResponse response = userInfoList.stream()
            .filter(a -> userId.equals(a.getUserId()))
            .findFirst()
            .orElseGet(ClientLikePageResponse::new);
        if (!ObjectUtils.isEmpty(response.getUserId())) {
          result.add(response);
        }
      });
    }
    return result;
  }

  /**
   * 列表批量获取点赞信息
   *
   * @param result
   */
  public static void getUserLikePage(Cache<Long, LinkedHashSet<Integer>> articleLikeUserListCache,
      List<ClientArticleResponse> result) {
    if (ObjectUtils.isEmpty(result)) {
      return;
    }
    //获取用户id
    Integer userId = AccessTokenUtil.getCurrentUserId();
    Set<Long> articleIds = result.stream().map(ClientArticleResponse::getArticleId)
        .collect(Collectors.toSet());
    Map<Long, LinkedHashSet<Integer>> setMap = articleLikeUserListCache.getAll(articleIds);

    //遍历获取点赞信息
    result.forEach(article -> {
      Long articleId = article.getArticleId();
      LinkedHashSet<Integer> userIds = setMap.get(articleId);
      //封装用户点赞信息
      ClientLikeResponse clientLikeResponse = new ClientLikeResponse();
      if (!ObjectUtils.isEmpty(userIds)) {
        clientLikeResponse.setLikeCount(userIds.size());
        clientLikeResponse.setLikeCountString(DiscoverUtil.getReadCountString(userIds.size()));
        if (userId != null) {
          clientLikeResponse.setLikeStatus(userIds.contains(userId));
        }
      }
      article.setClientLikeResponse(clientLikeResponse);
    });
  }

  /**
   * 阅读数转换
   */
  public static String getReadCountString(Integer likeCount) {
    if (likeCount == null || likeCount <= 0) {
      return "0";
    }
    int divide = 10000;
    if (likeCount < divide) {
      return String.valueOf(likeCount);
    }
    BigDecimal result = new BigDecimal(likeCount)
        .divide(new BigDecimal(divide), 1, BigDecimal.ROUND_HALF_UP);
    return result.stripTrailingZeros().toPlainString() + "万";
  }


  /**
   * 商品编号转换为商品id
   *
   * @param productId
   * @param productNo
   * @return
   */
  public static Long getProductId(Long productId, String productNo,
      Cache<String, Long> productNoIdRelation, ClientProductService clientProductService) {
    if (productId == null || productId <= 0) {
      if (ObjectUtils.isEmpty(productNo)) {
        return null;
      }
      Long resultId = productNoIdRelation.get(productNo);
      if (ObjectUtils.isEmpty(resultId)) {
        CommonResponse<Long> longCommonResponse = clientProductService
            .queryProductNoIdRelation(productNo);
        if (!longCommonResponse.isSuccess()) {
          return null;
        }
        return longCommonResponse.getResult();
      }
      return resultId;
    }
    return productId;
  }
}
