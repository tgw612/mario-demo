package com.mario.persistence.support.algorithm.complex;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:复合订单分库算法 Author: 陈二伟 Date:2018/09/25
 */
public abstract class ComplexTbShardingAlgorithm implements ComplexKeysShardingAlgorithm {

  @Override
  public Collection<String> doSharding(Collection<String> availableTargetNames,
      Collection<ShardingValue> shardingValues) {
    Set<String> tbIndex = new HashSet<>();
    for (ShardingValue s : shardingValues) {
      ListShardingValue shardingValue = (ListShardingValue) s;
      String logicTableName = shardingValue.getLogicTableName();
      if (CollectionUtils.isNotEmpty(shardingValue.getValues())) {
        for (Iterator<String> i = shardingValue.getValues().iterator(); i.hasNext(); ) {
          Object value = i.next();
          tbIndex.add(logicTableName + "_" + this
              .getIndex(shardingValue.getColumnName(), value.toString()));
        }
      }
    }

    return tbIndex;
  }

  protected abstract Integer getIndex(String columnName, String value);
}
