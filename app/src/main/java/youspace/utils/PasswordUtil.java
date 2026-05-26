package youspace.utils;

public class PasswordUtil {

    private PasswordUtil() {
    }

    public static boolean checkPassword(String inputPassword, String storedPassword) {
        return inputPassword != null && inputPassword.equals(storedPassword);
    }
}