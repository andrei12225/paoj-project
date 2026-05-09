package shop.models;

import shop.enums.AmpTechnology;

public class Amplifier extends Product {
    private String brand;
    private int wattage;
    private AmpTechnology technology;
    private double speakerSize;


    public Amplifier(int id, String name, double price, int stockQuantity, String brand, int wattage, AmpTechnology technology, double speakerSize) {
        super(id, name, price, stockQuantity);
        this.brand = brand;
        this.wattage = wattage;
        this.technology = technology;
        this.speakerSize = speakerSize;
    }

    @Override
    public String toString() {
        return "Amplifier {" +
            " id='" + getDisplayId() + "'" +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stockQuantity='" + getStockQuantity() + "'" +
            " brand='" + getBrand() + "'" +
            ", wattage='" + getWattage() + "'" +
            ", technology='" + getTechnology() + "'" +
            ", speakerSize='" + getSpeakerSize() + "'" +
            " }";
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWattage() {
        return this.wattage;
    }

    public void setWattage(int wattage) {
        this.wattage = wattage;
    }

    public AmpTechnology getTechnology() {
        return this.technology;
    }

    public void setTechnology(AmpTechnology technology) {
        this.technology = technology;
    }

    public double getSpeakerSize() {
        return this.speakerSize;
    }

    public void setSpeakerSize(double speakerSize) {
        this.speakerSize = speakerSize;
    }
}
