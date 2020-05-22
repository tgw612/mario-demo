package com.mall.discover.common.enums;

import com.doubo.common.topic.MqTopic;
import com.doubo.common.topic.env.ITopicEnv;
import com.doubo.common.topic.env.TopicEnv;
import com.doubo.common.util.EnumUtil;

public enum VodTopicEnum implements MqTopic {

    VOD_HANDLER_TOPIC("topic_discover_vod_handler", "视频处理任务队列"),

    ;

    private String code;

    private String desc;

    VodTopicEnum(String code, String desc) {
        this(code, false, desc);
    }

    VodTopicEnum(String code, boolean isBroadcast, String desc) {
        this.code = code;
        this.desc = desc;
        this.isBroadcast = isBroadcast;
    }

    public static VodTopicEnum resolve(String value) {
        return EnumUtil.fromEnumValue(VodTopicEnum.class, "code", value);
    }

    private static final String DEFAULT_TAG = "*";

    /**
     * Tag，即消息标签、消息类型，用来区分某个MQ的Topic下的消息分类。MQ允许消费者按照Tag对消息进行过滤，确保消费者最终只消费到他关心的消息类型。
     */
    private String tag = DEFAULT_TAG;

    private boolean isBroadcast;//是否为广播模式主题（默认值为false）

    /**
     * 默认不需要通过后缀来区分环境
     *
     * @see TopicEnv
     */
    public static ITopicEnv env = TopicEnv.EMPTY;

    @Override
    public String getCode() {
        return env.getCode(code);
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    public static void setEnv(TopicEnv env) {
        VodTopicEnum.env = env;
    }

    public void env(TopicEnv env) {
        VodTopicEnum.env = env;
    }
}
