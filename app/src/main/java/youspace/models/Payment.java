package youspace.models;

import youspace.enums.PaymentMethod;
import youspace.enums.PaymentStatus;

public class Payment {

    private int id;
    private int bookingId;
    private PaymentMethod method;
    private String proofPath;
    private PaymentStatus paymentStatus;
    private String paidAt;

    public Payment() {
    }

    public Payment(int id, int bookingId, PaymentMethod method, String proofPath,
                   PaymentStatus paymentStatus, String paidAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.method = method;
        this.proofPath = proofPath;
        this.paymentStatus = paymentStatus;
        this.paidAt = paidAt;
    }

    public boolean isPaid() {
        return paymentStatus == PaymentStatus.PAID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID payment tidak boleh negatif.");
        }
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        if (bookingId <= 0) {
            throw new IllegalArgumentException("Booking ID tidak valid.");
        }
        this.bookingId = bookingId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getProofPath() {
        return proofPath;
    }

    public void setProofPath(String proofPath) {
        this.proofPath = proofPath;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }
}