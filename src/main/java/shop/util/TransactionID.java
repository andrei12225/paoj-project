package shop.util;

public class TransactionID {
    private static long counter = 0;

    public static String generateID() {
        counter++;
        return "TXN-" + String.format("%06d", counter);
    }
}
