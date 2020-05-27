package com.mario.persistence.support.algorithm.precise;

import com.doubo.common.interfaces.StringHashCoding;
import com.mall.common.algorithms.ShardingDBAlgorithms;
import com.mario.persistence.util.SpringUtil;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiujingwang
 * @version 1.0
 * @date 2018/09/18 下午7:47
 * @Description: 数据库分片算法
 */
@Slf4j
public class DataBaseShardingAlgorithmForInteger implements PreciseShardingAlgorithm<Long> {

  private static StringHashCoding dataBaseHashCoding;

  @Override
  public String doSharding(Collection<String> availableTargetNames,
      PreciseShardingValue<Long> shardingValue) {
      /*  StringHashCoding dataBaseHashCoding = getDataBaseHashCoding();
        return "ds" + dataBaseHashCoding.hashFor(Integer.toString(shardingValue.getValue()));*/
    //TODO 后期改为64个库
    int dbIndex = (ShardingDBAlgorithms.getRealNode(Long.toString(shardingValue.getValue())) % 64);
    log.info("---------dbIndex:" + dbIndex);
    return "ds" + dbIndex;
  }

  private StringHashCoding getDataBaseHashCoding() {
    if (dataBaseHashCoding == null) {
      synchronized (DataBaseShardingAlgorithmForInteger.class) {
        if (dataBaseHashCoding == null) {
          dataBaseHashCoding = SpringUtil.getBean("dataBaseHashCoding");
        }
      }
    }
    return dataBaseHashCoding;
  }
}