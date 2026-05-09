package shop.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import shop.config.DatabaseConnection;
import shop.models.SalePeriod;

public class SalePeriodRepository {
    public void create(SalePeriod salePeriod) {
        String sql = "INSERT INTO sale_periods (name, start_time, end_time, is_open) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, salePeriod.getName());
            stmt.setObject(2, salePeriod.getStartTime());
            stmt.setObject(3, salePeriod.getEndTime());
            stmt.setBoolean(4, salePeriod.getIsOpen());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    salePeriod.setPeriodID(keys.getInt(1));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create sale period", exception);
        }
    }

    public void update(SalePeriod salePeriod) {
        String sql = "UPDATE sale_periods SET name = ?, start_time = ?, end_time = ?, is_open = ? WHERE period_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, salePeriod.getName());
            stmt.setObject(2, salePeriod.getStartTime());
            stmt.setObject(3, salePeriod.getEndTime());
            stmt.setBoolean(4, salePeriod.getIsOpen());
            stmt.setInt(5, salePeriod.getPeriodID());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update sale period", exception);
        }
    }

    public List<SalePeriod> findAll() {
        String sql = "SELECT * FROM sale_periods";
        List<SalePeriod> periods = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                SalePeriod period = new SalePeriod(
                    resultSet.getInt("period_id"),
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
