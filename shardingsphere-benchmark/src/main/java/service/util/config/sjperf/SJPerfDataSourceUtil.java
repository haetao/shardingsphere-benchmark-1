package service.util.config.sjperf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import service.api.entity.Iou;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class SJPerfDataSourceUtil {
    private static final String USER_NAME = "####";
    private static final String DEFAULT_SCHEMA = "test";

    public static DataSource createDataSource(final String dataSourceName, final String host, final int port, final String password) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC", host, port, dataSourceName));
        config.setUsername(USER_NAME);
        config.setPassword(password);
        config.setMaximumPoolSize(200);
        config.addDataSourceProperty("useServerPrepStmts", Boolean.TRUE.toString());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useLocalSessionState", Boolean.TRUE.toString());
        config.addDataSourceProperty("rewriteBatchedStatements", Boolean.TRUE.toString());
        config.addDataSourceProperty("cacheResultSetMetadata", Boolean.TRUE.toString());
        config.addDataSourceProperty("cacheServerConfiguration", Boolean.TRUE.toString());
        config.addDataSourceProperty("elideSetAutoCommits", Boolean.TRUE.toString());
        config.addDataSourceProperty("maintainTimeStats", Boolean.FALSE.toString());
        config.addDataSourceProperty("netTimeoutForStreamingResults", 0);
        DataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }

    public void createSchema(final DataSource dataSource) {
        //TODO
    }

    public static void getSelect(final String sql, final DataSource dataSource) throws SQLException{
        List<Iou> result = new LinkedList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Iou iou = new Iou();
                //TODO add result iou.setK(resultSet.getInt(2));
                result.add(iou);
            }
        }
    }

    public static int updateStmt(final String sql, final DataSource datasource) throws SQLException {
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //TODO ADD preparedStatement.setString(1,"##-#####");
            return preparedStatement.executeUpdate();
        }
    }

    public static int delete(final String sql, final DataSource datasource) throws SQLException {
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //TODO ADD preparedStatement.setInt(1,1);
            return preparedStatement.executeUpdate();
        }
    }

    public static void insert(final String sql, final DataSource datasource) throws SQLException {
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //TODO ADD preparedStatement.setInt(1, 1);
            preparedStatement.execute();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

}
