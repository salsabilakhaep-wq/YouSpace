package youspace.service;

import java.util.List;

import youspace.dao.BookingDAO;
import youspace.dao.PaymentDAO;
import youspace.enums.BookingStatus;
import youspace.enums.PaymentMethod;
import youspace.enums.PaymentStatus;
import youspace.models.Payment;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final BookingDAO bookingDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.bookingDAO = new BookingDAO();
    }

    public boolean payBooking(int bookingId, PaymentMethod method) {
        if (bookingId <= 0) {
            throw new IllegalArgumentException("Booking ID tidak valid.");
        }

        if (method == null) {
            throw new IllegalArgumentException("Metode pembayaran wajib dipilih.");
        }

        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setMethod(method);
        payment.setPaymentStatus(PaymentStatus.PAID);

        boolean paymentCreated = paymentDAO.createPayment(payment);

        if (paymentCreated) {
            bookingDAO.updateStatus(bookingId, BookingStatus.APPROVED);
        }

        return paymentCreated;
    }

    public Payment getPaymentByBookingId(int bookingId) {
        return paymentDAO.findByBookingId(bookingId);
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }
}