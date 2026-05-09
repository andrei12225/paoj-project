package shop.services;

import shop.data.StoreInventory;
import shop.enums.UserPermission;
import shop.enums.UserRole;
import shop.models.Partner;
import shop.models.Product;
import shop.models.User;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ConsoleUi {
    private TreeMap<Integer, String> options;
    private StoreInventory inventory;
    private Scanner scanner;

    public ConsoleUi(StoreInventory inventory, Scanner scanner) {
        this.inventory = inventory;
        this.scanner = scanner;
        this.options = new TreeMap<>(Map.ofEntries(
            Map.entry(1, "View Products"),
            Map.entry(2, "View Partners"),
            Map.entry(3, "Start New Sale Period"),
            Map.entry(4, "Close Current Sale Period"),
            Map.entry(5, "View Current Sale Period"), 
            Map.entry(6, "View Sale Period History"),
            Map.entry(7, "Sell a Product"),
            Map.entry(8, "Filter Products"),
            Map.entry(9, "Generate Sales Report"),
            Map.entry(10, "Manage users"),
            Map.entry(11, "Logout"),
            Map.entry(0, "Exit")
        ));
    }

    public void start() {
        handleLogin();

        Integer option = -1;

        try {
            while (options.get(option) != "Exit") {
                showMainMenu();
                option = Integer.parseInt(scanner.nextLine());
                String optionStr = options.get(option);

                switch (optionStr) {
                    case "View Products":
                        handleViewProducts();
                        break;

                    case "View Partners":
                        handleViewPartners();
                        break;

                    case "Start New Sale Period":
                        handleStartNewSalePeriod();
                        break;

                    case "Close Current Sale Period":
                        handleCloseCurrentSalePeriod();
                        break;

                    case "View Current Sale Period":
                        handleViewCurrentSalePeriod();
                        break;

                    case "View Sale Period History":
                        handleViewSalePeriodHistory();
                        break;

                    case "Sell a Product":
                        handleSellProduct();
                        break;

                    case "Filter Products":
                        handleFilterProducts();
                        break;

                    case "Generate Sales Report":
                        System.out.println("Generating sales report...");
                        CSVFileService.exportProductsToCSV();
                        break;

                    case "Manage users":
                        handleManageUsers();
                        break;

                    case "Logout":
                        System.out.println("Logging out...");
                        handleLogin();
                        break;

                    case "Exit":
                        System.out.println("Exiting the system. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number corresponding to the menu options.");
        } finally {
            scanner.close();
        }
    }

    private void handleViewProducts() {
        System.out.println("Products:");
        for (var product : inventory.getProducts()) {
            System.out.println(product);
        }
    }

    private void handleViewPartners() {
        System.out.println("Partners:");
        for (var p : inventory.getPartners()) {
            System.out.println(p);
        }
    }

    private void handleStartNewSalePeriod() {
        System.out.println("Starting new sale period...");
        System.out.println("Enter period name: ");
        String periodName = scanner.nextLine();
        inventory.startNewPeriod(periodName);
    }

    private void handleCloseCurrentSalePeriod() {
        System.out.println("Closing current sale period...");
        inventory.endCurrentPeriod();
    }

    private void handleViewCurrentSalePeriod() {
        System.out.println("Current Sale Period:");
        if (inventory.getCurrentPeriod() != null) {
            System.out.println(inventory.getCurrentPeriod());
        } else {
            System.out.println("No active sale period.");
        }
    }

    private void handleViewSalePeriodHistory() {
        System.out.println("Sale Period History:");
        for (int i = 0; i < inventory.getSalesHistory().size(); i++) {
            System.out.println((i + 1) + ". " + inventory.getSalesHistory().get(i));
        }
    }

    private void handleSellProduct() {
        System.out.println("Choose a product to sell:");
        for (int i = 0; i < inventory.getProducts().size(); i++) {
            System.out.println((i + 1) + ". " + inventory.getProducts().get(i));
        }
        int productIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (productIndex < 0 || productIndex >= inventory.getProducts().size()) {
            System.out.println("Invalid product selection.");   
        } else {
            Product selectedProduct = inventory.getProducts().get(productIndex);

            System.out.println("Choose a partner to sell to:");
            for (int i = 0; i < inventory.getPartners().size(); i++) {
                System.out.println((i + 1) + ". " + inventory.getPartners().get(i));
            }
            int partnerIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (partnerIndex < 0 || partnerIndex >= inventory.getPartners().size()) {
                System.out.println("Invalid partner selection.");
            } else {
                Partner selectedPartner = inventory.getPartners().get(partnerIndex);

                System.out.println("Enter quantity to sell: ");
                int quantity = Integer.parseInt(scanner.nextLine());

                try {
                    inventory.sellProduct(selectedProduct, selectedPartner, quantity);
                    System.out.println("Product sold successfully!");
                } catch (Exception e) {
                    System.out.println("Error selling product: " + e.getMessage());
                }
            }
        }
    }

    private void filterProductsByCategory() {
        System.out.println("Enter category to filter by:");
        String category = scanner.nextLine();
        int tolerance = 2;
        List<Product> filteredProductsCategory = inventory.getProductsByCategory(category, tolerance);
        if (filteredProductsCategory.isEmpty()) {
            System.out.println("No products found in category: " + category);
        } else {
            System.out.println("Products in category '" + category + "':");
            for (Product p : filteredProductsCategory) {
                System.out.println(p);
            }
        }
    }

    private void filterProductsByPriceRange() {
        System.out.println("Enter minimum price:");
        double minPrice = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter maximum price:");
        double maxPrice = Double.parseDouble(scanner.nextLine());
        List<Product> filteredProductsPrice = inventory.getProductsByPriceRange(minPrice, maxPrice);
        if (filteredProductsPrice.isEmpty()) {
            System.out.println("No products found in the specified price range.");
        } else {
            System.out.println("Products in the specified price range:");
            for (Product p : filteredProductsPrice) {
                System.out.println(p);
            }
        }
    }

    private void handleFilterProducts() {
        System.out.println("Choose a filter option:");
        System.out.println("1. Filter by category");
        System.out.println("2. Filter by price range");
        int filterOption = Integer.parseInt(scanner.nextLine());
        switch (filterOption) {
            case 1:
                filterProductsByCategory();
                break;
            case 2:
                filterProductsByPriceRange();
                break;
            default:
                System.out.println("Invalid filter option.");
        }
    }

    private void handleLogin() {
        while (true) {
            System.out.println("Would you like to log in or register? (1 for login, 2 for register)");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    while (true) {
                        System.out.println("Username: ");
                        String username = scanner.nextLine();
                        System.out.println("Password: ");
                        String password = scanner.nextLine();

                        try {
                            inventory.login(username, password);
                            return;
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                case "2":
                    System.out.println("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.println("Enter password: ");
                    String password = scanner.nextLine();

                    try {
                        inventory.register(username, password);
                        System.out.println("Registration successful! You can now log in.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }
    
    private void showRegisteredUsers() {
        System.out.println("Registered Users:");
        for (User user : inventory.getRegisteredUsers()) {
            System.out.println(user);
        }
    }

    private void handleDeleteUser() {
        System.out.println("Enter username of the user to delete:");
        String usernameToDelete = scanner.nextLine();
        try {
            inventory.deleteUser(usernameToDelete);
            System.out.println("User deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleManageUserPermissions() {
        System.out.println("Enter username of the user to manage permissions:");
        String usernameToManage = scanner.nextLine();

        User userToManage = inventory.getUserByUsername(usernameToManage, 2);
        if (userToManage == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Current permissions for " + userToManage.getUsername() + ":");
        System.out.println(userToManage.getPermissions());
        System.out.println("Available permissions:");
        for (UserPermission perm : UserPermission.values()) {
            System.out.println("- " + perm);
        }
        System.out.println("Enter permission to toggle (or 'done' to finish):");

        while (true) {
            String permInput = scanner.nextLine();
            if (permInput.equalsIgnoreCase("done")) {
                break;
            }
            try {
                UserPermission perm = UserPermission.valueOf(permInput.toUpperCase());
                if (userToManage.hasPermission(perm)) {
                    inventory.removeUserPermission(userToManage.getUsername(), perm);
                    System.out.println("Permission " + perm + " removed.");
                } else {
                    inventory.addUserPermission(userToManage.getUsername(), perm);
                    System.out.println("Permission " + perm + " added.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid permission. Please try again.");
            }
        }
    }

    private void handleManageUsers() {
        if (inventory.getActiveUser().getRole() != UserRole.ADMIN) {
            System.out.println("Access denied. Only admins can manage users.");
            return;
        }
        System.out.println("User Management:");
        System.out.println("1. View registered users");
        System.out.println("2. Delete a user");
        System.out.println("3. Manage user permissions");

        int userOption = Integer.parseInt(scanner.nextLine());
        switch (userOption) {
            case 1:
                showRegisteredUsers();
                break;

            case 2:
                handleDeleteUser();
                break;

            case 3:
                handleManageUserPermissions();
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

    public void showMainMenu() {
        System.out.println("Main Menu:");
        for (Map.Entry<Integer, String> entry : options.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
    }
}
