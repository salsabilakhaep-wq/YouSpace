package youspace.utils;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isPositiveNumber(int number) {
        return number > 0;
    }

    public static boolean isPositivePrice(double price) {
        return price >= 0;
    }
}