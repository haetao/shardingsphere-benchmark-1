package sjperf_3_1_0.SJDatabaseRouting;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import service.api.service.SJPerfService;
import service.util.config.sjperf_3_1_0.SJPerfDataSourceOp_3_1_0;
import service.util.config.sjperf.SJPerfDataSourceUtil;
import sjperf_3_1_0.SQLStatement;

import java.sql.SQLException;

/**
 * for sharding jdbc database routing update performance
 */
public class SJDatabaseRoutingUpdatePerf extends AbstractJavaSamplerClient {

    public static final String UPDATE_SQL_DB_ROUTING = SQLStatement.UPDATE_SQL_DB_ROUTING.getValue();
    public static SJPerfService sjPerfService;

    static {
        try {
            sjPerfService = new SJPerfService(SJPerfDataSourceOp_3_1_0.CreateDataSource());
        } catch (final SQLException ignore) {
        }
    }
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult results = new SampleResult();
        results.setSampleLabel("SJDatabaseRoutingUpdatePerf");
        results.sampleStart();
        try {
            SJPerfDataSourceUtil.updateStmt(UPDATE_SQL_DB_ROUTING,sjPerfService.dataSource);
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