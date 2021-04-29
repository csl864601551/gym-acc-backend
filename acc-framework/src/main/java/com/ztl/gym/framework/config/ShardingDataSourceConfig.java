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

        //t_code 表规则配置
        TableRuleConfiguration codeTableRuleConfig = new TableRuleConfiguration("t_code", "master.t_code_$->{0..1000}");
        codeTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("company_id", new CodeShardingAlgorithm()));

        //t_code_flow 表规则配置
        TableRuleConfiguration codeFlowTableRuleConfig1 = new TableRuleConfiguration("t_in_code_flow", "master.t_in_code_flow_$->{0..1000}");
        codeFlowTableRuleConfig1.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("company_id", new CodeFlowShardingAlgorithm()));

        TableRuleConfiguration codeFlowTableRuleConfig2 = new TableRuleConfiguration("t_out_code_flow", "master.t_out_code_flow_$->{0..1000}");
        codeFlowTableRuleConfig2.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("company_id", new CodeFlowShardingAlgorithm()));

        TableRuleConfiguration codeFlowTableRuleConfig3 = new TableRuleConfiguration("t_back_code_flow", "master.t_back_code_flow_$->{0..1000}");
        codeFlowTableRuleConfig3.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("company_id", new CodeFlowShardingAlgorithm()));

        TableRuleConfiguration codeFlowTableRuleConfig4 = new TableRuleConfiguration("t_transfer_code_flow", "master.t_transfer_code_flow_$->{0..1000}");
        codeFlowTableRuleConfig4.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("company_id", new CodeFlowShardingAlgorithm()));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(codeTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(codeFlowTableRuleConfig1);
        shardingRuleConfig.getTableRuleConfigs().add(codeFlowTableRuleConfig2);
        shardingRuleConfig.getTableRuleConfigs().add(codeFlowTableRuleConfig3);
        shardingRuleConfig.getTableRuleConfigs().add(codeFlowTableRuleConfig4);

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
