package youspace.service;

import youspace.dao.BookingDAO;
import youspace.dao.PaymentDAO;
import youspace.enums.BookingStatus;
import youspace.enums.PaymentMethod;
import youspace.enums.PaymentStatus;
import youspace.models.Payment;

import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final BookingDAO bookingDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.bookingDAO = new BookingDAO();
    }

    public boolean choosePaymentMethod(int bookingId, PaymentMethod method) {
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setMethod(method);
        payment.setPaymentStatus(PaymentStatus.WAITING_PAYMENT);

        return paymentDAO.createPayment(payment);
    }

    public boolean uploadPaymentProof(int bookingId, String proofPath) {
        boolean uploaded = paymentDAO.uploadProof(bookingId, proofPath);

        if (uploaded) {
            bookingDAO.updateStatus(bookingId, BookingStatus.WAITING_CONFIRMATION);
        }

        return uploaded;
    }

    public boolean confirmPayment(int bookingId) {
        boolean updated = paymentDAO.updatePaymentStatus(bookingId, PaymentStatus.PAID);

        if (updated) {
            bookingDAO.updateStatus(bookingId, BookingStatus.APPROVED);
        }

        return updated;
    }

    public boolean rejectPayment(int bookingId) {
        boolean updated = paymentDAO.updatePaymentStatus(bookingId, PaymentStatus.REJECTED);

        if (updated) {
            bookingDAO.updateStatus(bookingId, BookingStatus.REJECTED);
        }

        return updated;
    }

    public Payment getPaymentByBookingId(int bookingId) {
        return paymentDAO.findByBookingId(bookingId);
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }
}