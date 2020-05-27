package com.mario.service.api.util;

import com.doubo.common.exception.ServiceException;
import com.mall.discover.common.exception.code.BussinessErrCodeEnum;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.Data;
import sun.misc.BASE64Encoder;

@Data
public class SignatureUtil {

  private static final String HMAC_ALGORITHM = "HmacSHA1";
  private static final String CONTENT_CHARSET = "UTF-8";
  private String secretId;
  private String secretKey;
  private long currentTime;
  private int random;
  private int signValidDuration;
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
  /**
   * 指定存储地域，可以在控制台上自助添加存储地域，详细请参见 上传存储设置，该字段填写为存储地域的 英文简称。
   */
  private String storageRegion;

  public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
    byte[] byte3 = new byte[byte1.length + byte2.length];
    System.arraycopy(byte1, 0, byte3, 0, byte1.length);
    System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
    return byte3;
  }

  public String getUploadSignature() {
    String strSign = "";
    try {
      String contextStr = "";
      long endTime = (currentTime + signValidDuration);
      contextStr += "secretId=" + java.net.URLEncoder.encode(secretId, "utf8");
      contextStr += "&currentTimeStamp=" + currentTime;
      contextStr += "&expireTime=" + endTime;
      contextStr += "&random=" + random;
      contextStr += "&procedure=" + procedure;
      contextStr += "&vodSubAppId=" + vodSubAppId;
      contextStr += "&taskNotifyMode=" + taskNotifyMode;

      Mac mac = Mac.getInstance(HMAC_ALGORITHM);
      SecretKeySpec secretKey = new SecretKeySpec(this.secretKey.getBytes(CONTENT_CHARSET),
          mac.getAlgorithm());
      mac.init(secretKey);

      byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
      byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
      strSign = base64Encode(sigBuf);
      strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
    } catch (Exception e) {
      throw new ServiceException(BussinessErrCodeEnum.SIGNATURE_ERR);
    }
    return strSign;
  }

  private String base64Encode(byte[] buffer) {
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(buffer);
  }
}
