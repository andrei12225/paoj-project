package shop.models;

import java.time.LocalDateTime;

public class SaleRecord {
    private String transactionID;
    private Product soldProduct;
    private Partner buyer;
    private int quantity;
    private double finalPricePerUnit;
    private LocalDateTime saleTimestamp;

    public SaleRecord(String transactionID, Product soldProduct, Partner buyer, int quantity) {
        this.transactionID = transactionID;
        this.soldProduct = soldProduct;
        this.buyer = buyer;
        this.quantity = quantity;
        
        this.finalPricePerUnit = soldProduct.getPrice() * (1 - buyer.getDiscountRate());
        this.saleTimestamp = LocalDateTime.now();
    }

    public double getTotalSaleAmount() {
        return this.finalPricePerUnit * this.quantity;
    }

    @Override
    public String toString() {
        return "SaleRecord {" +
            " transactionID='" + getTransactionID() + "'" +
            ", soldProduct='" + getSoldProduct() + "'" +
            ", buyer='" + getBuyer() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", finalPricePerUnit='" + getFinalPricePerUnit() + "'" +
            ", saleTimestamp='" + getSaleTimestamp() + "'" +
            " }";
    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public Product getSoldProduct() {
        return this.soldProduct;
    }

    public void setSoldProduct(Product soldProduct) {
        this.soldProduct = soldProduct;
    }

    public Partner getBuyer() {
        return this.buyer;
    }

    public void setBuyer(Partner buyer) {
        this.buyer = buyer;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getFinalPricePerUnit() {
        return this.finalPricePerUnit;
    }

    public void setFinalPricePerUnit(double finalPricePerUnit) {
        this.finalPricePerUnit = finalPricePerUnit;
    }

    public LocalDateTime getSaleTimestamp() {
        return this.saleTimestamp;
    }

    public void setSaleTimestamp(LocalDateTime saleTimestamp) {
        this.saleTimestamp = saleTimestamp;
    }
}
