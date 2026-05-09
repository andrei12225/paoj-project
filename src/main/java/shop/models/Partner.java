package shop.models;

public class Partner {
    private int partnerID;
    private String companyName;
    private String email;
    private double discountRate; 
    private double totalSpent;

    public Partner(int partnerID, String companyName, String email, double discountRate, double totalSpent) {
        this.partnerID = partnerID;
        this.companyName = companyName;
        this.email = email;
        this.discountRate = discountRate;
        this.totalSpent = totalSpent;
    }

    public Partner(int partnerID, String companyName, String email, double discountRate) {
        this.partnerID = partnerID;
        this.companyName = companyName;
        this.email = email;
        this.discountRate = discountRate;
        this.totalSpent = 0.0;
    }

    public void addPurchase(double amount) {
        this.totalSpent += amount;
    }

    public String getDisplayId() {
        return "PRT-" + String.format("%06d", partnerID);
    }

    @Override
    public String toString() {
        return "Partner {" +
            " partnerID='" + getDisplayId() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", email='" + getEmail() + "'" +
            ", discountRate='" + getDiscountRate() + "'" +
            ", totalSpent='" + getTotalSpent() + "'" +
            " }";
    }

    public int getPartnerID() {
        return this.partnerID;
    }

    public void setPartnerID(int partnerID) {
        this.partnerID = partnerID;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getDiscountRate() {
        return this.discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getTotalSpent() {
        return this.totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
}
