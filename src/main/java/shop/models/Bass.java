package shop.models;

import shop.enums.BassPickupType;

public class Bass extends Product {
    private int stringCount;
    private boolean isActive;
    private BassPickupType pickupType;


    public Bass(String id, String name, double price, int stockQuantity, int stringCount, boolean isActive, BassPickupType pickupType) {
        super(id, name, price, stockQuantity);
        this.stringCount = stringCount;
        this.isActive = isActive;
        this.pickupType = pickupType;
    }


    @Override
    public String toString() {
        return "Bass {" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stockQuantity='" + getStockQuantity() + "'" +
            " stringCount='" + getStringCount() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", pickupType='" + getPickupType() + "'" +
            " }";
    }

    public int getStringCount() {
        return this.stringCount;
    }

    public void setStringCount(int stringCount) {
        this.stringCount = stringCount;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public BassPickupType getPickupType() {
        return this.pickupType;
    }

    public void setPickupType(BassPickupType pickupType) {
        this.pickupType = pickupType;
    }
}
