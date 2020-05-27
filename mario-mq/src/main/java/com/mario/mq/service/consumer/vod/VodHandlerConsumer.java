package com.mario.mq.service.consumer.vod;

import com.alibaba.fastjson.TypeReference;
import com.aliyun.openservices.ons.api.Action;
import com.doubo.ali.mq.autoconfigure.MqConsumer;
import com.doubo.ali.mq.consumer.api.RocketMqMessageListener;
import com.doubo.ali.mq.exception.SerializationException;
import com.doubo.ali.mq.model.MessageContext;
import com.doubo.ali.mq.model.MqBaseMessageBody;
import com.doubo.ali.mq.model.MqMessageBody;
import com.doubo.common.topic.MqTopic;
import com.doubo.common.util.StringUtil;
import com.doubo.json.util.JsonUtil;
import com.mall.discover.common.VodLook;
import com.mall.discover.common.enums.VodTopicEnum;
import com.mall.discover.service.ArticleService;
import com.mario.mq.constant.DubboConstants;
import com.mario.mq.service.consumer.entity.ArticleVodUpdateDTO;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;

/**
 * @author xuxu
 * @date 2019/9/29 15:15
 * @description 直播商品活动信息变更
 */
@Slf4j
@MqConsumer(group = "discoverVodHandlerGroup")
public class VodHandlerConsumer implements RocketMqMessageListener {

  @Reference(consumer = DubboConstants.DISCOVER_CONSUMER)
  ArticleService articleService;
  private Type TYPE = new TypeReference<MqMessageBody<String>>() {
  }.getType();

  @Override
  public Action call(MqBaseMessageBody mqBaseMessageBody, MessageContext messageContext) {
    try {
      ArticleVodUpdateDTO dto = JsonUtil.parseObject(mqBaseMessageBody.getContent().toString(),
          new TypeReference<ArticleVodUpdateDTO>() {
          });
      VodLook vodLook = dto.getVodLook();
      Long articleId = dto.getArticleId();
      if (vodLook != null && StringUtil.isNotBlank(vodLook.getCoverUrl()) && StringUtil
          .isNotBlank(vodLook.getVodUrl())
          && StringUtil.isNotBlank(vodLook.getWebpUrl())) {
        articleService.updateArticleLookVod(vodLook, articleId);
        log.info("consume message success:===" + JsonUtil.toJson(mqBaseMessageBody));
        return Action.CommitMessage;
      }
      return Action.ReconsumeLater;
    } catch (Exception e) {
      log.error("consume message error:===" + JsonUtil.toJson(mqBaseMessageBody) + e.getMessage());
      return Action.ReconsumeLater;
    }
  }

  @Override
  public MqTopic subscriTopic() {
    return VodTopicEnum.VOD_HANDLER_TOPIC;
  }

  @Override
  public MqBaseMessageBody deserialize(byte[] bytes, MessageContext messageContext)
      throws SerializationException {
    return JsonUtil.fromJson(bytes, TYPE);
  }
}
