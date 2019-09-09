package service.util.config.sjperf;

import org.apache.shardingsphere.api.config.encrypt.EncryptColumnRuleConfiguration;
import org.apache.shardingsphere.api.config.encrypt.EncryptRuleConfiguration;
import org.apache.shardingsphere.api.config.encrypt.EncryptTableRuleConfiguration;
import org.apache.shardingsphere.api.config.encrypt.EncryptorRuleConfiguration;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.EncryptDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
/**
 * for different sjperf dataSource
 */
public class SJPerfDataSourceOp {

    /**
     * create full routing
     * @return  data source
     * @throws SQLException
     */
    public static DataSource CreateDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("db0", SJPerfDataSourceUtil.createDataSource("db0", "####", 3306, "####"));
        dataSourceMap.put("db1", SJPerfDataSourceUtil.createDataSource("db1", "####", 3306, "####"));
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration("t_order", "db${0..1}.t_order${0..1}");
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
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("ds_master_slave", "ds_master", Arrays.asList("ds_slave0", "ds_slave1"));
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig, new Properties());
        return dataSource;
    }

    /**
     * create encrypt datasource
     * @return encrypt data source
     * @throws SQLException
     */
    public static DataSource CreateEncryptDataSource() throws SQLException {
        DataSource dataSource = SJPerfDataSourceUtil.createDataSource("ds_encrypt","####",3306,"####");
        Properties props = new Properties();
        props.setProperty("aes.key.value", "123456");
        EncryptorRuleConfiguration encryptorConfig = new EncryptorRuleConfiguration("AES", props);
        EncryptColumnRuleConfiguration columnConfig = new EncryptColumnRuleConfiguration("plain_pwd", "cipher_pwd", "", "aes");
        EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration(Collections.singletonMap("pwd", columnConfig));
        EncryptRuleConfiguration encryptRuleConfig = new EncryptRuleConfiguration();
        encryptRuleConfig.getEncryptors().put("aes", encryptorConfig);
        encryptRuleConfig.getTables().put("t_encrypt", tableConfig);
        DataSource encryptDatasource = EncryptDataSourceFactory.createDataSource(dataSource, encryptRuleConfig, new Properties());
        return encryptDatasource;
    }


}
