package shop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            ConfigLoader.get("db.url"),
            ConfigLoader.get("db.username"),
            ConfigLoader.get("db.password")
        );
    }
}
