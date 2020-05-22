package com.mall.discover.persistence.support.algorithm.precise;

import com.doubo.common.interfaces.StringHashCoding;
import com.mall.discover.persistence.util.SpringUtil;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author qiujingwang
 * @version 1.0
 * @date 2018/09/18 下午7:47
 */
@Slf4j
public class TableShardingAlgorithmForString implements PreciseShardingAlgorithm<String> {

    private static StringHashCoding tableHashCoding;

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        StringHashCoding tableHashCoding = getTableHashCoding();
        int tableShardingValue = tableHashCoding.hashFor(shardingValue.getValue());
        String tableName = shardingValue.getLogicTableName() + "_" + (tableShardingValue % 8);
        log.info("---------tableName:" + tableName);
        return tableName;
    }

    private StringHashCoding getTableHashCoding() {
        if (tableHashCoding == null) {
            synchronized (TableShardingAlgorithmForString.class) {
                if (tableHashCoding == null) {
                    tableHashCoding = SpringUtil.getBean("tableHashCoding");
                }
            }
        }
        return tableHashCoding;
    }
}
