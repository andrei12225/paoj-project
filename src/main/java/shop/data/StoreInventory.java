package shop.data; 

import shop.enums.UserPermission;
import shop.enums.UserRole;
import shop.interfaces.Tunable;
import shop.models.Partner;
import shop.models.Product;
import shop.models.SalePeriod;
import shop.models.SaleRecord;
import shop.models.User;
import shop.util.SalePeriodID;
import shop.util.SearchUtils;
import shop.util.TransactionID;
import shop.util.UserID;
import java.util.ArrayList;
import java.util.List;

public class StoreInventory {   
    private static StoreInventory instance;

    private List<Product> products;
    private List<Partner> partners;
    private List<SalePeriod> salesHistory;
    private SalePeriod currentPeriod;
    private List<User> registeredUsers;
    private User activeUser;

    private StoreInventory() {
        this.products = new ArrayList<>();
        this.partners = new ArrayList<>();
        this.salesHistory = new ArrayList<>();
        this.registeredUsers = new ArrayList<>();

        registeredUsers.add(new User(UserID.generateID(), "admin", "1234", UserRole.ADMIN));
    }

    public static StoreInventory getInstance() {
        if (instance == null) {
            instance = new StoreInventory();
        }
        return instance;
    }

    public User getUserByUsername(String username, int tolerance) {
        return registeredUsers.stream()
            .filter(user -> SearchUtils.LevenshteinDistance(user.getUsername(), username) <= tolerance)
            .findFirst()
            .orElse(null);
    }

    public void register(String username, String password) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_USERS)) {
            System.out.println("Permission denied: You do not have permission to manage users.");
            return;
        }
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username)) {
                throw new IllegalArgumentException("Username already exists. Please choose a different username.");
            }
        }
        User newUser = new User(UserID.generateID(), username, password, UserRole.EMPLOYEE);
        registeredUsers.add(newUser);
    }

    public void login(String username, String password) {
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                activeUser = user;
                System.out.println("Login successful! Welcome, " + activeUser.getUsername());
                System.out.println("Last login: " + activeUser.getLastLogin());
                activeUser.updateLastLogin();
                return;
            }
        }

        throw new IllegalArgumentException("Invalid username or password.");
    }

    public void deleteUser(String username) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_USERS)) {
            System.out.println("Permission denied: You do not have permission to manage users.");
            return;
        }
        registeredUsers.removeIf(user -> user.getUsername().equals(username));
    }

    public void addProduct(Product p) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        products.add(p);
    }

    public void removeProduct(Product p) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        products.remove(p);
    }

    public void addPartner(Partner p) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PARTNERS)) {
            System.out.println("Permission denied: You do not have permission to manage partners.");
            return;
        }
        partners.add(p);
    }

    public void removePartner(Partner p) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PARTNERS)) {
            System.out.println("Permission denied: You do not have permission to manage partners.");
            return;
        }
        partners.remove(p);
    }

    public void sellProduct(Product p, Partner buyer, int quantity) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        if (currentPeriod == null || !currentPeriod.getIsOpen()) {
            throw new IllegalStateException("No active sale period. Please start a new period before selling.");
        }
        if (!products.contains(p)) {
            throw new IllegalArgumentException("Product not found in inventory.");
        }
        if (!partners.contains(buyer)) {
            throw new IllegalArgumentException("Buyer is not a registered partner.");
        }
        if (quantity > p.getStockQuantity()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        p.reduceStock(quantity);
        
        SaleRecord record = new SaleRecord(TransactionID.generateID(), p, buyer, quantity);
        currentPeriod.getRecords().add(record);
        buyer.addPurchase(record.getTotalSaleAmount());
    }

    public void updateProduct(Product p, String name, double price, int stockQuantity) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        if (!products.contains(p)) {
            throw new IllegalArgumentException("Product not found in inventory.");
        }
        p.setName(name);
        p.setPrice(price);
        p.reduceStock(p.getStockQuantity() - stockQuantity);
    }

    public void applyDiscountToCategory(String category, double discount) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        for (Product p : products) {
            if (p.getClass().getSimpleName().equalsIgnoreCase(category)) {
                p.setPrice(p.getPrice() * (1 - discount));
            }
        }
    }

    public List<Product> searchProductByPriceRange(double minPrice, double maxPrice) {
        return products.stream()
            .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            .toList();
    }

    public List<Product> getLowStockItems(int threshold) {
        return products.stream()
            .filter(p -> p.getStockQuantity() < threshold)
            .toList();
    }

    public double calculateTotalValue() {
        return products.stream()
            .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
            .sum();
    }

    public void startNewPeriod(String name) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        if (currentPeriod != null) {
            endCurrentPeriod();
        }
        this.currentPeriod = new SalePeriod(SalePeriodID.generateID(), name);
    }

    public List<Product> getProductsByCategory(String category, int tolerance) {
        return products.stream()
            .filter(p -> SearchUtils.LevenshteinDistance(category.toLowerCase(), p.getClass().getSimpleName().toLowerCase()) <= tolerance)
            .toList();
    }

    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return products.stream()
            .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            .toList();
    }

    public void endCurrentPeriod() {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        if (currentPeriod != null) {
            currentPeriod.closePeriod();
            salesHistory.add(currentPeriod);
            currentPeriod = null;
        }
    }

    public void tuneInstruments() {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        for (Product p : products) {
            if (p instanceof Tunable tunable) {
                tunable.tune();
            }
        }
    }

    @Override
    public String toString() {
        return "StoreInventory {" +
            " products='" + getProducts() + "'" +
            ", partners='" + getPartners() + "'" +
            ", salesHistory='" + getSalesHistory() + "'" +
            ", currentPeriod='" + getCurrentPeriod() + "'" +
            " }";
    }

    public List<SalePeriod> getSalesHistory() { 
        return salesHistory; 
    }

    public List<Product> getProducts() { 
        return products; 
    }

    public List<Partner> getPartners() { 
        return partners;
    }

    public SalePeriod getCurrentPeriod() { 
        return currentPeriod;
    }

    public List<User> getRegisteredUsers() {
        return registeredUsers;
    }

    public User getActiveUser() {
        return activeUser;
    }
}