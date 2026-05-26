package youspace.service;

import youspace.dao.BookingDAO;
import youspace.dao.VenueDAO;
import youspace.enums.BookingStatus;
import youspace.models.Booking;
import youspace.models.Venue;
import youspace.utils.ValidationUtil;

import java.util.List;

public class BookingService {

    private final BookingDAO bookingDAO;
    private final VenueDAO venueDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.venueDAO = new VenueDAO();
    }

    public boolean createBooking(int userId, int venueId, String eventName, int guestCount,
                                 String bookingDate, String startTime, String endTime, String note) {
        if (ValidationUtil.isEmpty(eventName)) {
            throw new IllegalArgumentException("Nama acara wajib diisi.");
        }

        if (!ValidationUtil.isPositiveNumber(guestCount)) {
            throw new IllegalArgumentException("Jumlah tamu harus lebih dari 0.");
        }

        if (ValidationUtil.isEmpty(bookingDate)) {
            throw new IllegalArgumentException("Tanggal booking wajib diisi.");
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

        if (bookingDAO.isVenueBookedAtDate(venueId, bookingDate)) {
            throw new IllegalArgumentException("Venue sudah dibooking pada tanggal tersebut.");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setVenueId(venueId);
        booking.setEventName(eventName);
        booking.setGuestCount(guestCount);
        booking.setBookingDate(bookingDate);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setTotalPrice(venue.getPricePerDay());
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