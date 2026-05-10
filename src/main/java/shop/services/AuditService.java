package shop.services;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import shop.config.AppLogger;

public final class AuditService {
    private static final Logger logger = AppLogger.getLogger(AuditService.class);

    private static final AuditService instance = new AuditService();

    private AuditService() {
    }
    
    public static AuditService getInstance() {
        return instance;
    }

    public void log(String actionName) {
        try (FileWriter writer = new FileWriter("audit.csv", true)) {
            writer.write(actionName + "," + LocalDateTime.now() + System.lineSeparator());
        } catch (Exception e) {
            logger.severe("Failed to write to audit log: " + e.getMessage());
        }
    }
}
