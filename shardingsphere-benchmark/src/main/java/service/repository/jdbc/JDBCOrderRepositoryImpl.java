package service.repository.jdbc;

import service.api.entity.Order;
import service.api.repository.OrderRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class JDBCOrderRepositoryImpl implements OrderRepository {

    private final DataSource dataSource;

    public JDBCOrderRepositoryImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS t_order (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id))";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (final SQLException ignored) {
        }
    }

    @Override
    public void dropTable() {
        String sql = "DROP TABLE t_order";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (final SQLException ignored) {
        }
    }

    @Override
    public void truncateTable() {
        String sql = "TRUNCATE TABLE t_order";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (final SQLException ignored) {
        }
    }

    @Override
    public Long insert(final Order order) {
        String sql = "INSERT INTO t_order (user_id, status) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setString(2, order.getStatus());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    order.setOrderId(resultSet.getLong(1));
                }
            }
        } catch (final SQLException ignored) {
        }
        return order.getOrderId();
    }

    @Override
    public void delete(final Long orderId) {
        String sql = "DELETE FROM t_order WHERE order_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.executeUpdate();
        } catch (final SQLException ignored) {
        }
    }

    @Override
    public List<Order> selectAll(String sql) throws SQLException {
        return getOrders(sql);
    }

    @Override
    public List<Order> selectRange() throws SQLException {
        String sql = "SELECT * FROM t_order WHERE order_id BETWEEN 1 AND 10";
        return getOrders(sql);
    }

    private List<Order> getOrders(final String sql) throws SQLException {
        List<Order> result = new LinkedList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getLong(1));
                order.setUserId(resultSet.getInt(2));
                order.setStatus(resultSet.getString(3));
                result.add(order);
            }
        }
        return result;
    }
}
