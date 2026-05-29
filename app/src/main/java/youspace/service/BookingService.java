package youspace.service;

import java.util.List;

import youspace.dao.BookingDAO;
import youspace.dao.VenueDAO;
import youspace.enums.BookingStatus;
import youspace.models.Booking;
import youspace.models.Venue;
import youspace.utils.ValidationUtil;

public class BookingService {

    private final BookingDAO bookingDAO;
    private final VenueDAO venueDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.venueDAO = new VenueDAO();
    }

    public boolean createBooking(int userId, int venueId, String eventName, int guestCount,
                                 String startDate, String endDate, String note) {

        if (ValidationUtil.isEmpty(eventName)) {
            throw new IllegalArgumentException("Nama acara wajib diisi.");
        }

        if (!ValidationUtil.isPositiveNumber(guestCount)) {
            throw new IllegalArgumentException("Jumlah tamu harus lebih dari 0.");
        }

        if (ValidationUtil.isEmpty(startDate) || ValidationUtil.isEmpty(endDate)) {
            throw new IllegalArgumentException("Tanggal mulai dan tanggal selesai wajib diisi.");
        }

        if (startDate.compareTo(endDate) > 0) {
            throw new IllegalArgumentException("Tanggal mulai tidak boleh setelah tanggal selesai.");
        }

        Venue venue = venueDAO.findById(venueId);

        if (venue == null) {
            throw new IllegalArgumentException("Venue tidak ditemukan.");
        }

        if (!venue.isAvailable()) {
            throw new IllegalArgumentException("Venue sedang tidak tersedia.");
        }

        if (!venue.canAccommodate(guestCount)) {
            throw new IllegalArgumentException("Jumlah tamu melebihi kapasitas venue.");
        }

        if (bookingDAO.isVenueBookedInRange(venueId, startDate, endDate)) {
            throw new IllegalArgumentException("Venue sudah dibooking pada rentang tanggal tersebut.");
        }

        double totalPrice = venue.getPricePerDay();

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setVenueId(venueId);
        booking.setEventName(eventName);
        booking.setGuestCount(guestCount);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.WAITING_PAYMENT);
        booking.setNote(note);

        return bookingDAO.createBooking(booking);
    }

    public List<Booking> getBookingsByUser(int userId) {
        return bookingDAO.getBookingsByUser(userId);
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    public Booking getBookingById(int bookingId) {
        return bookingDAO.findById(bookingId);
    }

    public boolean approveBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, BookingStatus.APPROVED);
    }

    public boolean rejectBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, BookingStatus.REJECTED);
    }

    public boolean completeBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, BookingStatus.COMPLETED);
    }
}