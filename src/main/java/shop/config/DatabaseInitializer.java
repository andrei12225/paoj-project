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
            String[] individualQueries = sql.split(";");

            try (var stmt = connection.createStatement()) {
                for (String query : individualQueries) {
                    if (!query.trim().isEmpty()) {
                        stmt.execute(query.trim());
                    }
                }

                System.out.println("Database tables initialized successfully!");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
