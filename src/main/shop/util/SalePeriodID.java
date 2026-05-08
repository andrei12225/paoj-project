package shop.util;

public class SalePeriodID {
    private static long counter = 0;

    public static String generateID() {
        counter++;
        return "SP-" + String.format("%06d", counter);
    }
}
