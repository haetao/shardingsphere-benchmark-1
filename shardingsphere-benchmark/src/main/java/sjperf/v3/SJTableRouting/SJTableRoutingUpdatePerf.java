package sjperf.v3.SJTableRouting;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import service.api.service.SJPerfService;
import service.util.config.sjperf.v3.SJPerfDataSourceOp;
import service.util.config.sjperf.SJPerfDataSourceUtil;
import sjperf.v3.SQLStatement;

import java.sql.SQLException;

/**
 * for sharding jdbc table routing update performance
 */
public class SJTableRoutingUpdatePerf extends AbstractJavaSamplerClient {

    public static final String UPDATE_SQL_TABLE_ROUTING = SQLStatement.UPDATE_SQL_TABLE_ROUTING.getValue();
    public static SJPerfService sjPerfService;

    static {
        try {
            sjPerfService = new SJPerfService(SJPerfDataSourceOp.CreateDataSource());
        } catch (final SQLException ignore) {
        }
    }
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult results = new SampleResult();
        results.setSampleLabel("SJTableRoutingUpdatePerf");
        results.sampleStart();
        try {
            SJPerfDataSourceUtil.updateStmt(UPDATE_SQL_TABLE_ROUTING,sjPerfService.dataSource);
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
