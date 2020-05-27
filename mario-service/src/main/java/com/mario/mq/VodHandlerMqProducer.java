package com.mario.mq;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.doubo.ali.mq.model.MqMessageBody;
import com.doubo.ali.mq.model.RockMqMessage;
import com.doubo.ali.mq.producer.RocketMqProducerBean;
import com.doubo.json.util.JsonUtil;
import com.mall.discover.common.enums.VodTopicEnum;
import com.mario.mq.request.ArticleVodUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VodHandlerMqProducer {

  @Autowired
  private RocketMqProducerBean defaultMqProducer;

  public void sendVodHandlerTask(ArticleVodUpdateRequest videoTranscodeRequest) {
    RockMqMessage<ArticleVodUpdateRequest> message = new RockMqMessage<>(
        VodTopicEnum.VOD_HANDLER_TOPIC,
        MqMessageBody
            .getInstance(videoTranscodeRequest, videoTranscodeRequest.getVodLook().getFileId()));
    boolean sendResult = defaultMqProducer
        .send(message, t -> JsonUtil.toJsonBytes(t, SerializerFeature.EMPTY));
    log.info("短视频处理任务消息发送成功" + JsonUtil.toJson(message));
    if (!sendResult) {
      log.error("短视频处理任务发送消息失败" + JsonUtil.toJson(message));
    }
  }
}
