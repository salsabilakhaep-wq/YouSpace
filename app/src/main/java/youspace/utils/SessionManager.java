package youspace.utils;

import youspace.models.AppUser;

public class SessionManager {

    private static AppUser currentUser;

    private SessionManager() {
    }

    public static void login(AppUser user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static AppUser getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole().name().equals("ADMIN");
    }

    public static boolean isUser() {
        return currentUser != null && currentUser.getRole().name().equals("USER");
    }
}