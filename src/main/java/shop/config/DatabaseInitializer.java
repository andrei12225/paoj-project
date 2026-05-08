package shop.config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;

public final class DatabaseInitializer {
    private DatabaseInitializer () {
    }

    public static void initializeDatabase() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            var path = Paths.get("src/main/resources/schema.sql").toAbsolutePath().toString();
            var sql = new String(Files.readAllBytes(Paths.get(path)));
            var statement = connection.createStatement();
            statement.execute(sql);

            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
