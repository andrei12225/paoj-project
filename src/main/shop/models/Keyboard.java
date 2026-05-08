package shop.models;

import shop.enums.KeyAction;

public class Keyboard extends Product {
    private int keyCount;
    private boolean isDigital;
    private KeyAction keyAction;

    public Keyboard(String id, String name, double price, int stockQuantity, int keyCount, boolean isDigital, KeyAction keyAction) {
        super(id, name, price, stockQuantity);
        this.keyCount = keyCount;
        this.isDigital = isDigital;
        this.keyAction = keyAction;
    }

    @Override
    public String toString() {
        return "Keyboard {" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stockQuantity='" + getStockQuantity() + "'" +
            " keyCount='" + getKeyCount() + "'" +
            ", isDigital='" + isIsDigital() + "'" +
            ", keyAction='" + getKeyAction() + "'" +
            " }";
    }

    public int getKeyCount() {
        return this.keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }

    public boolean isIsDigital() {
        return this.isDigital;
    }

    public boolean getIsDigital() {
        return this.isDigital;
    }

    public void setIsDigital(boolean isDigital) {
        this.isDigital = isDigital;
    }

    public KeyAction getKeyAction() {
        return this.keyAction;
    }

    public void setKeyAction(KeyAction keyAction) {
        this.keyAction = keyAction;
    }
}
