package shop.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shop.config.DatabaseConnection;
import shop.models.Partner;

public class PartnerRepository {
    public void create(Partner partner) {
        String sql = "INSERT INTO partners (partner_id, company_name, email, discount_rate, total_spent) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, partner.getPartnerID());
            stmt.setString(2, partner.getCompanyName());
            stmt.setString(3, partner.getEmail());
            stmt.setDouble(4, partner.getDiscountRate());
            stmt.setDouble(5, partner.getTotalSpent());

            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create partner", exception);
        }
    }

    public void delete(Partner partner) {
        String sql = "DELETE FROM partners WHERE partner_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, partner.getPartnerID());

            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete partner", exception);
        }
    }

    public Partner findById(String partnerID) {
        String sql = "SELECT * FROM partners WHERE partner_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, partnerID);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return new Partner(
                    resultSet.getString("partner_id"),
                    resultSet.getString("company_name"),
                    resultSet.getString("email"),
                    resultSet.getDouble("discount_rate")
                );
            } else {
                return null;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find partner by ID", exception);
        }
    }

    public List<Partner> findAll() {
        String sql = "SELECT * FROM partners";
        List<Partner> partners = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Partner partner = new Partner(
                    resultSet.getString("partner_id"),
                    resultSet.getString("company_name"),
                    resultSet.getString("email"),
                    resultSet.getDouble("discount_rate")
                );
                partner.addPurchase(resultSet.getDouble("total_spent"));
                partners.add(partner);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all partners", exception);
        }

        return partners;
    }

    public void update(Partner partner) {
        String sql = "UPDATE partners SET company_name = ?, email = ?, discount_rate = ?, total_spent = ? WHERE partner_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, partner.getCompanyName());
            stmt.setString(2, partner.getEmail());
            stmt.setDouble(3, partner.getDiscountRate());
            stmt.setDouble(4, partner.getTotalSpent());
            stmt.setString(5, partner.getPartnerID());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update partner", exception);
        }
    }
}
