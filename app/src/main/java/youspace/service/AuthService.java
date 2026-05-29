package youspace.service;

import youspace.dao.UserDAO;
import youspace.enums.UserStatus;
import youspace.models.AppUser;
import youspace.models.Customer;
import youspace.utils.PasswordUtil;
import youspace.utils.SessionManager;
import youspace.utils.ValidationUtil;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean register(String name, String email, String password, String phone) {
        if (
            ValidationUtil.isEmpty(name) ||
            ValidationUtil.isEmpty(email) ||
            ValidationUtil.isEmpty(password) ||
            ValidationUtil.isEmpty(phone)
        ) {
            throw new IllegalArgumentException("Nama, email, password, dan nomor HP wajib diisi.");
        }

        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Format email tidak valid.");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter.");
        }

        AppUser existingUser = userDAO.findByEmail(email);

        if (existingUser != null) {
            throw new IllegalArgumentException("Email sudah terdaftar.");
        }

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setPhone(phone);
        customer.setStatus(UserStatus.ACTIVE);

        return userDAO.register(customer);
    }

    public AppUser login(String email, String password) {
        if (ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(password)) {
            throw new IllegalArgumentException("Email dan password wajib diisi.");
        }

        AppUser user = userDAO.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("Email tidak ditemukan.");
        }

        if (!user.canLogin()) {
            throw new IllegalArgumentException("Akun sedang disuspend.");
        }

        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Password salah.");
        }

        SessionManager.login(user);
        return user;
    }

    public void logout() {
        SessionManager.logout();
    }
}