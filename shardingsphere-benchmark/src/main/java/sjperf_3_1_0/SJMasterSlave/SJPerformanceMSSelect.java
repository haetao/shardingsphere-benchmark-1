package sjperf_3_1_0.SJMasterSlave;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import service.api.service.CommonService;
import service.api.service.SJPerfService;
import service.config.MasterSlaveConfiguration;
import service.repository.jdbc.JDBCOrderItemRepositoryImpl;
import service.repository.jdbc.JDBCOrderRepositoryImpl;
import service.repository.service.RawPojoService;
import service.util.config.DataSourceUtil;
import service.util.config.sjperf.SJPerfDataSourceUtil;
import service.util.config.sjperf_3_1_0.SJPerfDataSourceOp_3_1_0;
import sjperf_3_1_0.SQLStatement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * sharding jdbc performance for master slave select
 */
public class SJPerformanceMSSelect extends AbstractJavaSamplerClient {
    public static final String SELECT_SQL_MASTER_SLAVE = SQLStatement.SELECT_SQL_MASTER_SLAVE.getValue();
    public static SJPerfService sjPerfService;

    static {
        try {
            sjPerfService = new SJPerfService(SJPerfDataSourceOp_3_1_0.CreateMSDataSource());
        } catch (final SQLException ignore) {
        }
    }
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult results = new SampleResult();
        results.setSampleLabel("SJPerformanceMSSelect");
        results.sampleStart();
        try {
            SJPerfDataSourceUtil.getSelect(SELECT_SQL_MASTER_SLAVE,sjPerfService.dataSource);
        } catch (SQLException ex) {
            results.setSuccessful(false);
            return results;
        } finally {
            results.sampleEnd();
        }
        results.setSuccessful(true);
        return results;
    }
}
