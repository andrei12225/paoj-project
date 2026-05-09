package shop.models;

import shop.enums.ShellMaterial;

public class Drumset extends Product {
    private int numberOfPieces;
    private ShellMaterial shellMaterial;
    private boolean includesCymbals;


    public Drumset(int id, String name, double price, int stockQuantity, int numberOfPieces, ShellMaterial shellMaterial, boolean includesCymbals) {
        super(id, name, price, stockQuantity);
        this.numberOfPieces = numberOfPieces;
        this.shellMaterial = shellMaterial;
        this.includesCymbals = includesCymbals;
    }

    @Override
    public String toString() {
        return "Drumset {" +
            " id='" + getDisplayId() + "'" +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stockQuantity='" + getStockQuantity() + "'" +
            " numberOfPieces='" + getNumberOfPieces() + "'" +
            ", shellMaterial='" + getShellMaterial() + "'" +
            ", includesCymbals='" + getIncludesCymbals() + "'" +
            " }";
    }
    
    public int getNumberOfPieces() {
        return this.numberOfPieces;
    }

    public void setNumberOfPieces(int numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public ShellMaterial getShellMaterial() {
        return this.shellMaterial;
    }

    public void setShellMaterial(ShellMaterial shellMaterial) {
        this.shellMaterial = shellMaterial;
    }

    public boolean getIncludesCymbals() {
        return this.includesCymbals;
    }

    public void setIncludesCymbals(boolean includesCymbals) {
        this.includesCymbals = includesCymbals;
    }
}
