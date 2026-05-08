package shop.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import shop.data.StoreInventory;
import shop.enums.UserPermission;
import shop.models.SalePeriod;
import shop.models.SaleRecord;

public class CSVFileService {
    private static final String PRODUCTS_FILE = "products.csv";
    
    private CSVFileService() {
    }

    public static void exportProductsToCSV() {
        StoreInventory inventory = StoreInventory.getInstance();

        if (inventory.getActiveUser() != null && !inventory.getActiveUser().hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }

        SalePeriod currentPeriod = inventory.getCurrentPeriod();

        if (currentPeriod == null) {
            System.out.println("No active sale period. Cannot export products.");
            return;
        }
        
        if (currentPeriod.getRecords().isEmpty()) {
            System.out.println("No sales records to generate report.");
            return;
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("TransactionID,ProductName,BuyerName,Quantity,FinalPricePerUnit,SaleTimestamp\n");
        for (SaleRecord record : currentPeriod.getRecords()) {
            csvBuilder.append(record.getTransactionID()).append(",")
                      .append(record.getSoldProduct().getName()).append(",")
                      .append(record.getBuyer().getCompanyName()).append(",")
                      .append(record.getQuantity()).append(",")
                      .append(record.getFinalPricePerUnit()).append(",")
                      .append(record.getSaleTimestamp()).append("\n");  
        }

        try {
            Files.writeString(Paths.get(PRODUCTS_FILE), csvBuilder.toString(), java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.CREATE);
            System.out.println("Products exported to " + PRODUCTS_FILE);
        } catch (Exception e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
