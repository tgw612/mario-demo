package com.mario.mq.processor;

import com.doubo.ali.mq.consumer.RocketMqConsumerBean;
import com.doubo.ali.mq.consumer.api.RocketMqMessageListener;
import com.tdn.commons.lang.utils.FieldUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 生成MQ消息监听器代理
 *
 * @author zhangzehao
 * @date 2019/8/13 12:16
 */
@Component
@Slf4j
public class ConsumerListenerProxyProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    try {
      if (bean instanceof RocketMqConsumerBean) {
        Field mapfield = FieldUtils
            .getDeclaredField(RocketMqConsumerBean.class, "topicTagListenerMap", true);
        Map<String, Map<String, RocketMqMessageListener>> topicMap = (Map<String, Map<String, RocketMqMessageListener>>) FieldUtils
            .readField(mapfield, bean, true);
        for (Map<String, RocketMqMessageListener> tagMap : topicMap.values()) {
          for (String key : tagMap.keySet()) {
            //原生listener
            RocketMqMessageListener listener = tagMap.get(key);
            //代理listener
            RocketMqMessageListener listenerProxy = (RocketMqMessageListener) new ListenerMethodInterceptor()
                .getInstance(listener);

            Collection<Field> fields = FieldUtils.getAllDeclaredFields(listener.getClass());
            for (Field field : fields) {
              Object fieldResult = null;
              if (Modifier.isFinal(field.getModifiers()) || Modifier
                  .isStatic(field.getModifiers())) {
                continue;
              } else {
                fieldResult = FieldUtils.readField(field, listener, true);
                FieldUtils.writeField(field, listenerProxy, fieldResult, true);
              }
            }

            tagMap.put(key, listenerProxy);
          }
        }
      }
    } catch (Throwable e) {
      log.error("生成MQ消息监听器代理失败", e);
    }
    return bean;
  }
}
