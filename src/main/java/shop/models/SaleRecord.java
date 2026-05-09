package shop.models;

import java.time.LocalDateTime;

public class SaleRecord {
    private int transactionID;
    private Product soldProduct;
    private Partner buyer;
    private int quantity;
    private double finalPricePerUnit;
    private LocalDateTime saleTimestamp;

    public SaleRecord(int transactionID, Product soldProduct, Partner buyer, int quantity) {
        this.transactionID = transactionID;
        this.soldProduct = soldProduct;
        this.buyer = buyer;
        this.quantity = quantity;
        
        this.finalPricePerUnit = soldProduct.getPrice() * (1 - buyer.getDiscountRate());
        this.saleTimestamp = LocalDateTime.now();
    }

    public SaleRecord(int transactionID, Product soldProduct, Partner buyer, int quantity, double finalPricePerUnit, LocalDateTime saleTimestamp) {
        this.transactionID = transactionID;
        this.soldProduct = soldProduct;
        this.buyer = buyer;
        this.quantity = quantity;
        this.finalPricePerUnit = finalPricePerUnit;
        this.saleTimestamp = saleTimestamp;
    }

    public double getTotalSaleAmount() {
        return this.finalPricePerUnit * this.quantity;
    }

    public String getDisplayId() {
        return "TXN-" + String.format("%06d", transactionID);
    }

    @Override
    public String toString() {
        return "SaleRecord {" +
            " transactionID='" + getDisplayId() + "'" +
            ", soldProduct='" + getSoldProduct() + "'" +
            ", buyer='" + getBuyer() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", finalPricePerUnit='" + getFinalPricePerUnit() + "'" +
            ", saleTimestamp='" + getSaleTimestamp() + "'" +
            " }";
    }

    public int getTransactionID() {
        return this.transactionID;
    }

    public void setTransactionID(int transactionID) {
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
