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
import shop.enums.UserRole;
import shop.models.User;

public class UserRepository {
    public void create(User user) {
        String sql = "INSERT INTO users (username, password, role, last_login) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());
            stmt.setObject(4, user.getLastLogin());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setUserID(keys.getInt(1));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create user", exception);
        }
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    UserRole.valueOf(resultSet.getString("role"))
                );
                user.setLastLogin(resultSet.getObject("last_login", LocalDateTime.class));
                users.add(user);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all users", exception);
        }

        return users;
    }

    public void deleteByUsername(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete user", exception);
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, last_login = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());
            stmt.setObject(4, user.getLastLogin());
            stmt.setInt(5, user.getUserID());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update user", exception);
        }
    }
}
