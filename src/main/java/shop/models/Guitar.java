package shop.models;

import shop.enums.GuitarPickupType;
import shop.interfaces.Tunable;

public class Guitar extends Product implements Tunable {
    private String brand;
    private String model;
    private int numberOfStrings;
    private GuitarPickupType pickupType;

    public Guitar(String id, String name, double price, int stockQuantity, String brand, String model, int numberOfStrings, GuitarPickupType pickupType) {
        super(id, name, price, stockQuantity);
        this.brand = brand;
        this.model = model;
        this.numberOfStrings = numberOfStrings;
        this.pickupType = pickupType;
    }

    @Override
    public void tune() {
        System.out.println("Tuning the guitar to standard EADGBE tuning.");
    }

    @Override
    public double getStandardTuningFrequency() {
        return 440.0;
    }

    @Override
    public String toString() {
        return "Guitar {" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stockQuantity='" + getStockQuantity() + "'" +
            " brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", numberOfStrings='" + getNumberOfStrings() + "'" +
            ", pickupType='" + getPickupType() + "'" +
            " }";
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getNumberOfStrings() {
        return this.numberOfStrings;
    }

    public void setNumberOfStrings(int numberOfStrings) {
        this.numberOfStrings = numberOfStrings;
    }

    public GuitarPickupType getPickupType() {
        return this.pickupType;
    }

    public void setPickupType(GuitarPickupType pickupType) {
        this.pickupType = pickupType;
    }
}
