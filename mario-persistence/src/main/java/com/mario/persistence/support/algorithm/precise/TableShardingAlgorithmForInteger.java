package com.mario.persistence.support.algorithm.precise;

import com.doubo.common.interfaces.StringHashCoding;
import com.mario.persistence.util.SpringUtil;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiujingwang
 * @version 1.0
 * @date 2018/09/18 下午7:47
 */
@Slf4j
public class TableShardingAlgorithmForInteger implements PreciseShardingAlgorithm<Long> {

  private static StringHashCoding tableHashCoding;

  @Override
  public String doSharding(Collection<String> availableTargetNames,
      PreciseShardingValue<Long> shardingValue) {
    StringHashCoding tableHashCoding = getTableHashCoding();
    long tableShardingValue = tableHashCoding.hashFor(shardingValue.getValue().toString());
    String tableName = shardingValue.getLogicTableName() + "_" + (tableShardingValue % 8);
    log.info("---------tableName:" + tableName);
    return tableName;
  }

  private StringHashCoding getTableHashCoding() {
    if (tableHashCoding == null) {
      synchronized (TableShardingAlgorithmForInteger.class) {
        if (tableHashCoding == null) {
          tableHashCoding = SpringUtil.getBean("tableHashCoding");
        }
      }
    }
    return tableHashCoding;
  }
}
