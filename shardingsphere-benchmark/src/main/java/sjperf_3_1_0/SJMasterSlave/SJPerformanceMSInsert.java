package sjperf_3_1_0.SJMasterSlave;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import service.api.service.SJPerfService;
import service.util.config.sjperf.SJPerfDataSourceUtil;
import service.util.config.sjperf_3_1_0.SJPerfDataSourceOp_3_1_0;
import sjperf_3_1_0.SQLStatement;

import java.sql.SQLException;

/**
 * sharding jdbc performance for master slave insert
 */
public class SJPerformanceMSInsert extends AbstractJavaSamplerClient {
    public static final String INSERT_SQL_MASTER_SLAVE = SQLStatement.INSERT_SQL_MASTER_SLAVE.getValue();
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
        results.setSampleLabel("SJPerformanceMSInsert");
        results.sampleStart();
        try {
            SJPerfDataSourceUtil.insert(INSERT_SQL_MASTER_SLAVE,sjPerfService.dataSource);
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
