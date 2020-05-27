package com.mario.service.api.service.impl;

import com.doubo.common.model.response.CommonResponse;
import com.mall.common.manage.ResponseManage;
import com.mall.discover.common.enums.RelationTypeEnum;
import com.mall.discover.service.ClientLikeService;
import com.mario.service.api.service.impl.biz.ClientLikeBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: huangzhong
 * @Date: 2019/10/8
 * @Description:
 */
@Slf4j
@Service(
    group = "${dubbo.provider.group}",
    version = "${dubbo.provider.version}",
    application = "${dubbo.application.id}",
    protocol = "${dubbo.protocol.id}",
    registry = "${dubbo.registry.id}"
)
public class ClientLikeServiceImpl implements ClientLikeService {

  @Autowired
  private ClientLikeBiz clientLikeBiz;

  @Override
  public CommonResponse<Boolean> updateUserLike(Integer relationEnumCode) {
    //文章点赞
    if (RelationTypeEnum.USER_LIKE_ARTICLE.getCode().equals(relationEnumCode)) {
      clientLikeBiz.updateUserLikeArticle();
    }
    return ResponseManage.success(true);
  }
}
