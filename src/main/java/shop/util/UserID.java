package shop.util;

public class UserID {
    private static long counter = 0;

    public static String generateID() {
        counter++;
        return "USR-" + String.format("%06d", counter);
    }
}
