package service.util.config.sjperf_3_1_0;

import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import service.util.config.sjperf.SJPerfDataSourceUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * for different sjperf dataSource
 */
public class SJPerfDataSourceOp_3_1_0 {

    /**
     * create full routing
     * @return  data source
     * @throws SQLException
     */
    public static DataSource CreateDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("db0", SJPerfDataSourceUtil.createDataSource("db0", "####", 3306, "####"));
        dataSourceMap.put("db1", SJPerfDataSourceUtil.createDataSource("db1", "####", 3306, "####"));
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
        tableRuleConfiguration.setLogicTable("t_order");
        tableRuleConfiguration.setActualDataNodes("db${0..1}.t_order${0..1}");
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration(
                "user_id",
                "db${user_id % 2}"));
        tableRuleConfiguration.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration(
                        "order_id",
                        "t_order${order_id % 2}"));

        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfiguration);

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(
                dataSourceMap,
                shardingRuleConfig,
                new ConcurrentHashMap(),
                new Properties()
        );
        return dataSource;
    }

    /**
     * create master slave datasource
     * @return master slave data source
     * @throws SQLException
     */
    public static DataSource CreateMSDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds_master", SJPerfDataSourceUtil.createDataSource("ds_master", "####", 3306, "###"));
        dataSourceMap.put("ds_master", SJPerfDataSourceUtil.createDataSource("ds_master", "####", 3306, "###"));
        dataSourceMap.put("ds_slave0", SJPerfDataSourceUtil.createDataSource("ds_slave0","####",3306,"###"));
        dataSourceMap.put("ds_slave1", SJPerfDataSourceUtil.createDataSource("ds_slave1","####",3306,"###"));

        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration();
        masterSlaveRuleConfig.setName("ds_master_slave");
        masterSlaveRuleConfig.setMasterDataSourceName("ds_master");
        masterSlaveRuleConfig.setSlaveDataSourceNames(Arrays.asList("ds_slave0", "ds_slave1"));

        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig,new ConcurrentHashMap(), new Properties());
        return dataSource;
    }

}
