package shop.services;

import shop.models.Partner;

public class PartnerFactory {
    public static Partner createPartner(String name, String contactInfo, double discountRate) {
        return new Partner(0, name, contactInfo, discountRate);
    }

    public static Partner createPartner(int id, String name, String contactInfo, double discountRate) {
        return new Partner(id, name, contactInfo, discountRate);
    }
}
