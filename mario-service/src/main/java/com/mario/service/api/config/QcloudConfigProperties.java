package com.mario.service.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "vod.qcloud")
@Component
@Data
public class QcloudConfigProperties {

  /**
   * 密钥ID
   */
  private String secretId;
  /**
   * 密钥Key
   */
  private String secretKey;
  /**
   * 区域 eg： gz:广州; sh:上海; hk:香港; ca:北美;等。
   */
  private String region;
  /**
   * 输入桶
   */
  private String inputBucket;
  /**
   * 输出桶
   */
  private String outputBucket;
  /**
   * 输出目录
   */
  private String outputDir;
  /**
   * 转码模板ID
   */
  private Integer definition;
  /**
   * 水印模板ID
   */
  private Integer watermarkDefinition;
  /**
   * 视频审核模板
   */
  private Long pornCheckDefinition;

  /**
   * 视频审核模板
   */
  private Long sampleSnapshotDefinition;

  /**
   * CDN地址:eg https://server-video.weilaijishi.cn/
   */
  private String cdn;

  /**
   * cos 地址 eg:https://server-video-1259014512.cos.ap-guangzhou.myqcloud.com
   */
  private String cos;

  /**
   * 文件分隔标识 eg：test-vod
   */
  private String spilt;
  /**
   * 视频后续任务处理操作，即完成视频上传后，可自动发起任务流操作。参数值为任务流模板名，云点播支持 创建任务流模板 并为模板命名。
   */
  private String procedure;
  /**
   * 子应用 ID，如果不填写、填写0或填写开发者的腾讯云 AppId，则操作的子应用为“主应用”。
   */
  private Integer vodSubAppId;
  /**
   * 任务流状态变更通知模式（仅当指定了 procedure 时才有效）。 Finish：只有当任务流全部执行完毕时，才发起一次事件通知。
   * Change：只要任务流中每个子任务的状态发生变化，都进行事件通知。 None：不接受该任务流回调。 默认为 Finish。
   */
  private String taskNotifyMode;
}
