package shop.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import shop.config.DatabaseConnection;
import shop.enums.UserPermission;
import shop.models.User;

public class UserPermissionsRepository {
    public void create(User user, UserPermission permission) {
        String sql = "INSERT INTO user_permissions (user_id, permission) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getUserID());
            stmt.setString(2, permission.name());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create user permission", exception);
        }
    }

    public void delete(User user, UserPermission permission) {
        String sql = "DELETE FROM user_permissions WHERE user_id = ? AND permission = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getUserID());
            stmt.setString(2, permission.name());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete user permission", exception);
        }
    }

    public Map<UserPermission, Boolean> findByUser(User user) {
        String sql = "SELECT permission FROM user_permissions WHERE user_id = ?";
        Map<UserPermission, Boolean> permissions = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getUserID());
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                UserPermission perm = UserPermission.valueOf(resultSet.getString("permission"));
                permissions.put(perm, true);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find permissions for user", exception);
        }

        return permissions;
    }
}
