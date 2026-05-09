package shop.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import shop.config.DatabaseConnection;
import shop.data.StoreInventory;
import shop.models.SalePeriod;
import shop.models.SaleRecord;

public class SaleRecordRepository {
    public void create(SaleRecord saleRecord, SalePeriod salePeriod) {
        String sql = "INSERT INTO sale_records (transaction_id, period_id, product_id, partner_id, quantity, final_price_per_unit, sale_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, saleRecord.getTransactionID());
            stmt.setString(2, salePeriod.getPeriodID());
            stmt.setString(3, saleRecord.getSoldProduct().getId());
            stmt.setString(4, saleRecord.getBuyer().getPartnerID());
            stmt.setInt(5, saleRecord.getQuantity());
            stmt.setDouble(6, saleRecord.getFinalPricePerUnit());
            stmt.setObject(7, saleRecord.getSaleTimestamp());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create sale record", exception);
        }
    }

    public List<SaleRecord> findByPeriod(SalePeriod salePeriod) {
        String sql = "SELECT * FROM sale_records WHERE period_id = ?";
        List<SaleRecord> records = new ArrayList<>();
        StoreInventory inventory = StoreInventory.getInstance();

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, salePeriod.getPeriodID());
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                SaleRecord record = new SaleRecord(
                    resultSet.getString("transaction_id"),
                    inventory.getProductById(resultSet.getString("product_id")),
                    inventory.getPartnerById(resultSet.getString("partner_id")),
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
