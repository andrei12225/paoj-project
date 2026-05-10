package shop.data; 

import org.mindrot.jbcrypt.BCrypt;
import shop.enums.UserPermission;
import shop.enums.UserRole;
import shop.interfaces.Tunable;
import shop.models.Partner;
import shop.models.Product;
import shop.models.SalePeriod;
import shop.models.SaleRecord;
import shop.models.User;
import shop.repository.PartnerRepository;
import shop.repository.ProductRepository;
import shop.repository.SalePeriodRepository;
import shop.repository.SaleRecordRepository;
import shop.repository.UserPermissionsRepository;
import shop.repository.UserRepository;
import shop.services.AuditService;
import shop.util.SearchUtils;

import java.util.List;

public class StoreInventory {   
private static StoreInventory instance;

    private ProductRepository productRepository;
    private PartnerRepository partnerRepository;
    private UserRepository userRepository;
    private SalePeriodRepository salePeriodRepository;
    private SaleRecordRepository saleRecordRepository;
    private UserPermissionsRepository userPermissionsRepository;

    private List<Product> products;
    private List<Partner> partners;
    private List<User> registeredUsers;
    
    private SalePeriod currentPeriod;
    private User activeUser;

    public static List<String> productCategories = List.of("Guitar", "Drumset", "Bass", "Amplifier", "Accessory");

    private StoreInventory() {
        this.productRepository = new ProductRepository();
        this.partnerRepository = new PartnerRepository();
        this.userRepository = new UserRepository();
        this.salePeriodRepository = new SalePeriodRepository();
        this.saleRecordRepository = new SaleRecordRepository();
        this.userPermissionsRepository = new UserPermissionsRepository();

        refreshData();

        if (registeredUsers.isEmpty()) {
            User admin = new User(0, "admin", BCrypt.hashpw("1234", BCrypt.gensalt()), UserRole.ADMIN);
            userRepository.create(admin);
            for (UserPermission permission : UserPermission.values()) {
                userPermissionsRepository.create(admin, permission);
            }
        }

        refreshData();
    }

    public void refreshData() {
        this.products = productRepository.findAll();
        this.partners = partnerRepository.findAll();
        this.registeredUsers = userRepository.findAll();
        for (User user : registeredUsers) {
            user.getPermissions().putAll(userPermissionsRepository.findByUser(user));
        }

        List<SalePeriod> allPeriods = salePeriodRepository.findAll();
        this.currentPeriod = allPeriods.stream()
            .filter(SalePeriod::getIsOpen)
            .findFirst()
            .orElse(null);

        if (this.currentPeriod != null) {
            this.currentPeriod.setRecords(saleRecordRepository.findByPeriod(currentPeriod, this));
        }
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
        if (getUserByUsername(username, 0) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }
        User newUser = new User(0, username, BCrypt.hashpw(password, BCrypt.gensalt()), UserRole.EMPLOYEE);
        userRepository.create(newUser);
        registeredUsers.add(newUser);
    }

    public void login(String username, String password) {
        User user = getUserByUsername(username, 0);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        activeUser = user;
        System.out.println("Login successful! Welcome, " + activeUser.getUsername());
        System.out.println("Last login: " + activeUser.getLastLogin());
        activeUser.updateLastLogin();
        userRepository.update(activeUser);
    }

    public void deleteUser(String username) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_USERS)) {
            System.out.println("Permission denied: You do not have permission to manage users.");
            return;
        }
        userRepository.deleteByUsername(username);
        registeredUsers.removeIf(user -> user.getUsername().equals(username));
    }

    public void addUserPermission(String username, UserPermission permission) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_USERS)) {
            System.out.println("Permission denied: You do not have permission to manage users.");
            return;
        }
        User user = getUserByUsername(username, 0);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        user.getPermissions().put(permission, true);
        userPermissionsRepository.create(user, permission);
    }

    public void removeUserPermission(String username, UserPermission permission) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_USERS)) {
            System.out.println("Permission denied: You do not have permission to manage users.");
            return;
        }
        User user = getUserByUsername(username, 0);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        user.getPermissions().put(permission, false);
        userPermissionsRepository.delete(user, permission);
    }

    public void addProduct(Product p) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        productRepository.create(p);
        products.add(p);
        AuditService.getInstance().log("Add product");
    }

    public void removeProduct(int productId) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        Product product = findProductById(productId);
        if (product != null) {
            productRepository.delete(product);
            products.remove(product);
            AuditService.getInstance().log("Remove product");
        }
    }

    public Product findProductById(int id) {
        return products.stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void addPartner(Partner p) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PARTNERS)) {
            System.out.println("Permission denied: You do not have permission to manage partners.");
            return;
        }
        partnerRepository.create(p);
        partners.add(p);
        AuditService.getInstance().log("Register partner");
    }

    public void removePartner(int partnerId) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PARTNERS)) {
            System.out.println("Permission denied: You do not have permission to manage partners.");
            return;
        }
        Partner partner = findPartnerById(partnerId);
        if (partner != null) {
            partnerRepository.delete(partner);
            partners.remove(partner);
            AuditService.getInstance().log("Remove partner");
        }
    }

    private Partner findPartnerById(int id) {
        return partners.stream()
            .filter(p -> p.getPartnerID() == id)
            .findFirst()
            .orElse(null);
    }

    public void sellProduct(Product p, Partner buyer, int quantity) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        if (currentPeriod == null || !currentPeriod.getIsOpen()) {
            throw new IllegalStateException("No active sale period. Please start a new period before selling.");
        }
        if (findProductById(p.getId()) == null) {
            throw new IllegalArgumentException("Product not found in inventory.");
        }
        if (partnerRepository.findById(buyer.getPartnerID()) == null) {
            throw new IllegalArgumentException("Buyer is not a registered partner.");
        }
        if (quantity > p.getStockQuantity()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        p.reduceStock(quantity);
        
        SaleRecord record = new SaleRecord(0, p, buyer, quantity);
        currentPeriod.getRecords().add(record);
        buyer.addPurchase(record.getTotalSaleAmount());
        productRepository.update(p);
        partnerRepository.update(buyer);
        salePeriodRepository.update(currentPeriod);
        saleRecordRepository.create(record, currentPeriod);
        AuditService.getInstance().log("Sell product");
    }

    public void updateProduct(Product p, String name, double price, int stockQuantity) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }
        if (findProductById(p.getId()) == null) {
            throw new IllegalArgumentException("Product not found in inventory.");
        }
        p.setName(name);
        p.setPrice(price);
        p.reduceStock(p.getStockQuantity() - stockQuantity);
        productRepository.update(p);
        AuditService.getInstance().log("Update product");
    }

    public void applyDiscountToCategory(String category, double discount) {
        if (activeUser != null && !activeUser.hasPermission(UserPermission.MANAGE_PRODUCTS)) {
            System.out.println("Permission denied: You do not have permission to manage products.");
            return;
        }

        for (Product p : products) {
            if (p.getClass().getSimpleName().equalsIgnoreCase(category)) {
                p.setPrice(p.getPrice() * (1 - discount));
                productRepository.update(p);
            }
        }
        AuditService.getInstance().log("Apply bulk discount");
    }

    public Product getProductById(int id) {
        return findProductById(id);
    }

    public Partner getPartnerById(int id) {
        return partnerRepository.findById(id);
    }

    public List<Product> searchProductByPriceRange(double minPrice, double maxPrice) {
        return products.stream()
            .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            .toList();
    }

    public List<Product> getLowStockItems(int threshold) {
        AuditService.getInstance().log("Generate low-stock report");
        return products.stream()
            .filter(p -> p.getStockQuantity() < threshold)
            .toList();
    }

    public double calculateTotalValue() {
        AuditService.getInstance().log("View inventory valuation");
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
        SalePeriod salePeriod = new SalePeriod(0, name);
        currentPeriod = salePeriod;
        salePeriodRepository.create(salePeriod);
        AuditService.getInstance().log("Start sale period");
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
            salePeriodRepository.update(currentPeriod);
            currentPeriod = null;
        }
        AuditService.getInstance().log("End sale period");
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
        List<SalePeriod> periods = salePeriodRepository.findAll();
        for (SalePeriod period : periods) {
            period.setRecords(saleRecordRepository.findByPeriod(period, this));
        }
        return periods;
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
