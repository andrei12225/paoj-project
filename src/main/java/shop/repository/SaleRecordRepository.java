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
import shop.data.StoreInventory;
import shop.models.SalePeriod;
import shop.models.SaleRecord;

public class SaleRecordRepository {
    public void create(SaleRecord saleRecord, SalePeriod salePeriod) {
        String sql = "INSERT INTO sale_records (period_id, product_id, partner_id, quantity, final_price_per_unit, sale_timestamp) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, salePeriod.getPeriodID());
            stmt.setInt(2, saleRecord.getSoldProduct().getId());
            stmt.setInt(3, saleRecord.getBuyer().getPartnerID());
            stmt.setInt(4, saleRecord.getQuantity());
            stmt.setDouble(5, saleRecord.getFinalPricePerUnit());
            stmt.setObject(6, saleRecord.getSaleTimestamp());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    saleRecord.setTransactionID(keys.getInt(1));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create sale record", exception);
        }
    }

    public List<SaleRecord> findByPeriod(SalePeriod salePeriod, StoreInventory inventory) {
        String sql = "SELECT * FROM sale_records WHERE period_id = ?";
        List<SaleRecord> records = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, salePeriod.getPeriodID());
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                SaleRecord record = new SaleRecord(
                    resultSet.getInt("transaction_id"),
                    inventory.getProductById(resultSet.getInt("product_id")),
                    inventory.getPartnerById(resultSet.getInt("partner_id")),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("final_price_per_unit"),
                    resultSet.getObject("sale_timestamp", LocalDateTime.class)
                );
                records.add(record);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find sale records by period", exception);
        }

        return records;
    }
}
