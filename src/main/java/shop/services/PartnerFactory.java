package shop.services;

import shop.models.Partner;

public class PartnerFactory {
    private static int idCounter = 1;

    private static String generateId() {
        return "PRT-" + String.format("%06d", idCounter++);
    }

    public static Partner createPartner(String name, String contactInfo, double discountRate) {
        return new Partner(generateId(), name, contactInfo, discountRate);
    }
}
