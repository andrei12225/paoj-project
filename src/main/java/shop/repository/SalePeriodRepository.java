package shop.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import shop.config.DatabaseConnection;
import shop.models.SalePeriod;
import shop.models.SaleRecord;

public class SalePeriodRepository {
    public void create(SalePeriod salePeriod) {
        String sql = "INSERT INTO sale_periods (period_id, name, start_time, end_time, is_open) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, salePeriod.getPeriodID());
            stmt.setString(2, salePeriod.getName());
            stmt.setObject(3, salePeriod.getStartTime());
            stmt.setObject(4, salePeriod.getEndTime());
            stmt.setBoolean(5, salePeriod.getIsOpen());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create sale period", exception);
        }
    }

    public void update(SalePeriod salePeriod) {
        String sql = "UPDATE sale_periods SET name = ?, start_time = ?, end_time = ?, is_open = ? WHERE period_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, salePeriod.getName());
            stmt.setObject(2, salePeriod.getStartTime());
            stmt.setObject(3, salePeriod.getEndTime());
            stmt.setBoolean(4, salePeriod.getIsOpen());
            stmt.setString(5, salePeriod.getPeriodID());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update sale period", exception);
        }
    }

    public List<SalePeriod> findAll() {
        String sql = "SELECT * FROM sale_periods";
        List<SalePeriod> periods = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                SalePeriod period = new SalePeriod(
                    resultSet.getString("period_id"),
                    resultSet.getString("name"),
                    resultSet.getObject("start_time", LocalDateTime.class),
                    resultSet.getObject("end_time", LocalDateTime.class),
                    resultSet.getBoolean("is_open")
                );
                periods.add(period);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all sale periods", exception);
        }

        return periods;
    }
}
