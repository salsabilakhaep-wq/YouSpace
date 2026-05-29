package youspace.models;

import youspace.enums.BookingStatus;

public class Booking {

    private int id;
    private int userId;
    private int venueId;
    private String eventName;
    private int guestCount;
    private String startDate;
    private String endDate;
    private double totalPrice;
    private BookingStatus status;
    private String note;

    public Booking() {
    }

    public Booking(int id, int userId, int venueId, String eventName, int guestCount,
                   String startDate, String endDate,
                   double totalPrice, BookingStatus status, String note) {
        this.id = id;
        this.userId = userId;
        this.venueId = venueId;
        this.eventName = eventName;
        this.guestCount = guestCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.note = note;
    }

    public boolean isPending() {
        return status == BookingStatus.PENDING;
    }

    public boolean isApproved() {
        return status == BookingStatus.APPROVED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID booking tidak boleh negatif.");
        }
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID tidak valid.");
        }
        this.userId = userId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        if (venueId <= 0) {
            throw new IllegalArgumentException("Venue ID tidak valid.");
        }
        this.venueId = venueId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        if (eventName == null || eventName.isBlank()) {
            throw new IllegalArgumentException("Nama acara tidak boleh kosong.");
        }
        this.eventName = eventName;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        if (guestCount <= 0) {
            throw new IllegalArgumentException("Jumlah tamu harus lebih dari 0.");
        }
        this.guestCount = guestCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        if (startDate == null || startDate.isBlank()) {
            throw new IllegalArgumentException("Tanggal mulai tidak boleh kosong.");
        }
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        if (endDate == null || endDate.isBlank()) {
            throw new IllegalArgumentException("Tanggal selesai tidak boleh kosong.");
        }
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total harga tidak boleh negatif.");
        }
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}