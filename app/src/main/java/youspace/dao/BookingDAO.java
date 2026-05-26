package youspace.dao;

import youspace.config.DatabaseConfig;
import youspace.enums.BookingStatus;
import youspace.models.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean createBooking(Booking booking) {
        String sql = """
            INSERT INTO bookings
            (user_id, venue_id, event_name, guest_count, booking_date, start_time, end_time, total_price, status, note)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getVenueId());
            stmt.setString(3, booking.getEventName());
            stmt.setInt(4, booking.getGuestCount());
            stmt.setString(5, booking.getBookingDate());
            stmt.setString(6, booking.getStartTime());
            stmt.setString(7, booking.getEndTime());
            stmt.setDouble(8, booking.getTotalPrice());
            stmt.setString(9, booking.getStatus().name());
            stmt.setString(10, booking.getNote());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Gagal membuat booking: " + e.getMessage());
            return false;
        }
    }

    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY id DESC;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil booking user: " + e.getMessage());
        }

        return bookings;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY id DESC;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil semua booking: " + e.getMessage());
        }

        return bookings;
    }

    public Booking findById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ?;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, bookingId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            System.out.println("Gagal mencari booking: " + e.getMessage());
        }

        return null;
    }

    public boolean updateStatus(int bookingId, BookingStatus status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, status.name());
            stmt.setInt(2, bookingId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Gagal update status booking: " + e.getMessage());
            return false;
        }
    }

    public boolean isVenueBookedAtDate(int venueId, String bookingDate) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM bookings
            WHERE venue_id = ?
              AND booking_date = ?
              AND status IN ('PENDING', 'WAITING_PAYMENT', 'WAITING_CONFIRMATION', 'APPROVED');
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, venueId);
            stmt.setString(2, bookingDate);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Gagal cek jadwal venue: " + e.getMessage());
        }

        return false;
    }

    public int countActiveBookings() {
        String sql = """
            SELECT COUNT(*) AS total
            FROM bookings
            WHERE status IN ('PENDING', 'WAITING_PAYMENT', 'WAITING_CONFIRMATION', 'APPROVED');
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menghitung booking aktif: " + e.getMessage());
        }

        return 0;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        return new Booking(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("venue_id"),
            rs.getString("event_name"),
            rs.getInt("guest_count"),
            rs.getString("booking_date"),
            rs.getString("start_time"),
            rs.getString("end_time"),
            rs.getDouble("total_price"),
            BookingStatus.valueOf(rs.getString("status")),
            rs.getString("note")
        );
    }
}