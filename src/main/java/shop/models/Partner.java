package shop.models;

public class Partner {
    private String partnerID;
    private String companyName;
    private String email;
    private double discountRate; 
    private double totalSpent;

    public Partner(String partnerID, String companyName, String email, double discountRate, double totalSpent) {
        this.partnerID = partnerID;
        this.companyName = companyName;
        this.email = email;
        this.discountRate = discountRate;
        this.totalSpent = totalSpent;
    }

    public Partner(String partnerID, String companyName, String email, double discountRate) {
        this.partnerID = partnerID;
        this.companyName = companyName;
        this.email = email;
        this.discountRate = discountRate;
        this.totalSpent = 0.0;
    }

    public void addPurchase(double amount) {
        this.totalSpent += amount;
    }

    @Override
    public String toString() {
        return "Partner {" +
            " partnerID='" + getPartnerID() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", email='" + getEmail() + "'" +
            ", discountRate='" + getDiscountRate() + "'" +
            ", totalSpent='" + getTotalSpent() + "'" +
            " }";
    }

    public String getPartnerID() {
        return this.partnerID;
    }

    public void setPartnerID(String partnerID) {
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
