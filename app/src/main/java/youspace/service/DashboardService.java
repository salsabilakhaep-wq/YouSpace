package youspace.service;

import youspace.dao.BookingDAO;
import youspace.dao.PaymentDAO;
import youspace.dao.UserDAO;
import youspace.dao.VenueDAO;
import youspace.models.AppUser;

public class DashboardService {

    private final VenueDAO venueDAO;
    private final BookingDAO bookingDAO;
    private final PaymentDAO paymentDAO;
    private final UserDAO userDAO;

    public DashboardService() {
        this.venueDAO = new VenueDAO();
        this.bookingDAO = new BookingDAO();
        this.paymentDAO = new PaymentDAO();
        this.userDAO = new UserDAO();
    }

    public int getTotalVenue() {
        return venueDAO.countVenues();
    }

    public int getActiveBooking() {
        return bookingDAO.countActiveBookings();
    }

    public double getTotalIncome() {
        return paymentDAO.getTotalPaidIncome();
    }

    public int getTotalUser() {
        int total = 0;

        for (AppUser user : userDAO.getAllUsers()) {
            if (user.getRole().name().equals("USER")) {
                total++;
            }
        }

        return total;
    }
}