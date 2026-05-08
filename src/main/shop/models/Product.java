package shop.models;

public class Product {
    private String id;
    private String name;
    private double price;
    private int stockQuantity;

    public Product(String id, String name, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void reduceStock(int quantity) {
        if (quantity > this.stockQuantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }
        this.stockQuantity -= quantity;
    }

    @Override
    public String toString() {
        return "Product {" +
            " id='" + id + "'" +
            " name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stockQuantity='" + getStockQuantity() + "'" +
            " }";
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }
}
