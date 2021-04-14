package com.ztl.gym.framework.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ztl.gym.framework.config.properties.DruidProperties;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShardingDataSourceConfig {
    @Bean(name = "shardingDataSource")
    public DataSource shardingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource) throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);

        // sys_order 表规则配置
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("sys_order", "master.sys_order_$->{0..10}");
        orderTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("order_id", new MyComplexShardingAlgorithm()));
        // 分布式主键
        orderTableRuleConfig.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "order_id"));

        //code 表规则配置
        TableRuleConfiguration codeTableRuleConfig = new TableRuleConfiguration("t_code", "master.t_code_$->{0..1000}");
        codeTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("company_id", new CodeShardingAlgorithm()));

        //code 表规则配置
        TableRuleConfiguration codeMoveTableRuleConfig = new TableRuleConfiguration("t_code_move", "master.t_code_move_$->{0..1000}");
        codeTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("company_id", new CodeShardingAlgorithm()));

        //code 表规则配置
        TableRuleConfiguration storageCodeTableRuleConfig = new TableRuleConfiguration("t_storage_code", "master.t_storage_code_$->{0..1000}");
        codeTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("company_id", new CodeShardingAlgorithm()));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(codeTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(codeMoveTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(storageCodeTableRuleConfig);
        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, getProperties());
        return dataSource;
    }

    /**
     * 系统参数配置
     */
    private Properties getProperties() {
        Properties shardingProperties = new Properties();
        shardingProperties.put("sql.show", true);
        return shardingProperties;
    }
}
