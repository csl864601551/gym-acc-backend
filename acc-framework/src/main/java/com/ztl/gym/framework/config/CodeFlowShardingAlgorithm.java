package com.ztl.gym.framework.config;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CodeFlowShardingAlgorithm implements ComplexKeysShardingAlgorithm {

    @Override
    public Collection<String> doSharding(Collection actualTableNames, ComplexKeysShardingValue complexKeysShardingValue) {
        // 返回真实表名集合
        List<String> tableNameList = new ArrayList<>();

        // 分片键的值
        Collection<Long> conpanyIds = (Collection<Long>) complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("company_id");

        // 获取真实表名
        for (Long companyId : conpanyIds) {
            for (String tableName : (Collection<String>) actualTableNames) {
                if (tableName.endsWith(String.valueOf(companyId))) {
                    tableNameList.add(tableName);
                }
            }
        }
        return tableNameList;
    }
}
