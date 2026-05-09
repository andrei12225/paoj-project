package shop.models;

import shop.enums.AccessoryCategory;

public class Accessory extends Product {
    private AccessoryCategory category;
    private String targetInstrument;
    private int packQuantity;

    public Accessory(int id, String name, double price, int stockQuantity, AccessoryCategory category, String targetInstrument, int packQuantity) {
        super(id, name, price, stockQuantity);
        this.category = category;
        this.targetInstrument = targetInstrument;
        this.packQuantity = packQuantity;
    }

    @Override
    public String toString() {
        return "Accessory {" +
            " id='" + getDisplayId() + "'" +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stockQuantity='" + getStockQuantity() + "'" +
            " category='" + getCategory() + "'" +
            ", targetInstrument='" + getTargetInstrument() + "'" +
            ", packQuantity='" + getPackQuantity() + "'" +
            " }";
    }

    public AccessoryCategory getCategory() {
        return this.category;
    }

    public void setCategory(AccessoryCategory category) {
        this.category = category;
    }

    public String getTargetInstrument() {
        return this.targetInstrument;
    }

    public void setTargetInstrument(String targetInstrument) {
        this.targetInstrument = targetInstrument;
    }

    public int getPackQuantity() {
        return this.packQuantity;
    }

    public void setPackQuantity(int packQuantity) {
        this.packQuantity = packQuantity;
    }
}
