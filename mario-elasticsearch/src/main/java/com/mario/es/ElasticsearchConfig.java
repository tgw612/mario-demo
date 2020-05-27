import java.net.InetAddress;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConfig.class);

  /**
   * elk集群地址
   */
  @Value("${es.ip}")
  private String ip;

  /**
   * 端口
   */
  @Value("${es.port}")
  private String port;

  /**
   * 集群名称
   */
  @Value("${es.cluster.name}")
  private String clusterName;

  /**
   * 连接池
   */
  @Value("${es.pool}")
  private String pool;

  /**
   * Bean name default  函数名字
   *
   * @return
   */
  @Bean(name = "transportClient")
  public TransportClient transportClient() {

    TransportClient transportClient = null;
    try {
      // 配置信息
      Settings esSetting = Settings.builder()
          .put("cluster.name", clusterName) //集群名字
          .put("client.transport.sniff", true)//增加嗅探机制，找到ES集群
          .put("thread_pool.search.size", Integer.parseInt(pool))//增加线程池个数，暂时设为5
          .build();
      //配置信息Settings自定义
      transportClient = new PreBuiltTransportClient(esSetting);
      TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(ip),
          Integer.valueOf(port));
      transportClient.addTransportAddresses(transportAddress);
    } catch (Exception e) {
      LOGGER.error("elasticsearch TransportClient create error!!", e);
    }
    return transportClient;
  }
}
————————————————
    版权声明：本文为CSDN博主「不愿秃头的阳某」的原创文章，遵循CC4.0BY-SA版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/qq_43517653/java/article/details/104858592