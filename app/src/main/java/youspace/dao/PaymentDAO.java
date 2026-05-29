package youspace.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import youspace.config.DatabaseConfig;
import youspace.enums.PaymentMethod;
import youspace.enums.PaymentStatus;
import youspace.models.Payment;

public class PaymentDAO {

    public boolean createPayment(Payment payment) {
        String sql = """
            INSERT INTO payments (booking_id, method, payment_status, paid_at)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP);
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, payment.getBookingId());
            stmt.setString(2, payment.getMethod().name());
            stmt.setString(3, payment.getPaymentStatus().name());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Gagal membuat pembayaran: " + e.getMessage());
            return false;
        }
    }

    public Payment findByBookingId(int bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, bookingId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }

        } catch (SQLException e) {
            System.out.println("Gagal mencari pembayaran: " + e.getMessage());
        }

        return null;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY id DESC;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil pembayaran: " + e.getMessage());
        }

        return payments;
    }

    public double getTotalPaidIncome() {
        String sql = """
            SELECT SUM(b.total_price) AS total_income
            FROM bookings b
            JOIN payments p ON b.id = p.booking_id
            WHERE p.payment_status = 'PAID';
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            if (rs.next()) {
                return rs.getDouble("total_income");
            }

        } catch (SQLException e) {
            System.out.println("Gagal menghitung pendapatan: " + e.getMessage());
        }

        return 0;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("id"),
            rs.getInt("booking_id"),
            PaymentMethod.valueOf(rs.getString("method")),
            null,
            PaymentStatus.valueOf(rs.getString("payment_status")),
            rs.getString("paid_at")
        );
    }
}