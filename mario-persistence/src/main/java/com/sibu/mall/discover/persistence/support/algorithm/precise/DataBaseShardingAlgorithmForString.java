package com.mall.discover.persistence.support.algorithm.precise;

import com.doubo.common.interfaces.StringHashCoding;
import com.mall.common.algorithms.ShardingDBAlgorithms;
import com.mall.discover.persistence.util.SpringUtil;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author qiujingwang
 * @version 1.0
 * @date 2018/09/18 下午7:47
 * @Description: 数据库分片算法
 */
@Slf4j
public class DataBaseShardingAlgorithmForString implements PreciseShardingAlgorithm<String> {

    private static StringHashCoding dataBaseHashCoding;

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
      /*  StringHashCoding dataBaseHashCoding = getDataBaseHashCoding();
        return "ds" + dataBaseHashCoding.hashFor(Integer.toString(shardingValue.getValue()));*/

        int dbIndex = (ShardingDBAlgorithms.getRealNode(shardingValue.getValue()) % 8);
        log.info("---------dbIndex:" + dbIndex);
        return "ds" + dbIndex;
    }

    private StringHashCoding getDataBaseHashCoding() {
        if (dataBaseHashCoding == null) {
            synchronized (DataBaseShardingAlgorithmForString.class) {
                if (dataBaseHashCoding == null) {
                    dataBaseHashCoding = SpringUtil.getBean("dataBaseHashCoding");
                }
            }
        }
        return dataBaseHashCoding;
    }
}