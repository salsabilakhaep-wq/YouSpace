package youspace.service;

import youspace.dao.UserDAO;
import youspace.models.AppUser;

import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<AppUser> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<AppUser> searchUsers(String keyword) {
        return userDAO.searchUsers(keyword);
    }

    public boolean updateProfile(AppUser user) {
        return userDAO.updateProfile(user);
    }

    public boolean suspendUser(int userId) {
        return userDAO.suspendUser(userId);
    }

    public boolean activateUser(int userId) {
        return userDAO.activateUser(userId);
    }

    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}