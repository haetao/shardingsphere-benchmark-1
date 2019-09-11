package sjperf.v3.SJMasterSlave;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import service.api.service.SJPerfService;
import service.util.config.sjperf.SJPerfDataSourceUtil;
import service.util.config.sjperf.v3.SJPerfDataSourceOp;
import sjperf.v3.SQLStatement;

import java.sql.SQLException;

/**
 * sharding jdbc performance for master slave update
 */
public class SJPerformanceMSUpdate extends AbstractJavaSamplerClient {
    public static final String UPDATE_SQL_MASTER_SLAVE = SQLStatement.UPDATE_SQL_MASTER_SLAVE.getValue();
    public static SJPerfService sjPerfService;

    static {
        try {
            sjPerfService = new SJPerfService(SJPerfDataSourceOp.CreateMSDataSource());
        } catch (final SQLException ignore) {
        }
    }
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult results = new SampleResult();
        results.setSampleLabel("SJPerformanceMSUpdate");
        results.sampleStart();
        try {
            SJPerfDataSourceUtil.updateStmt(UPDATE_SQL_MASTER_SLAVE,sjPerfService.dataSource);
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
