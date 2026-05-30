package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.w3c.dom.Text;
import youspace.enums.BookingStatus;
import youspace.enums.PaymentMethod;
import youspace.models.Booking;
import youspace.service.BookingService;
import youspace.service.PaymentService;
import youspace.utils.SessionManager;
import youspace.view.SidebarApp;

import java.util.List;

public class BookingHistoryView extends HBox {

    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final VBox listContainer;

    public BookingHistoryView() {
        this.bookingService = new BookingService();
        this.paymentService = new PaymentService();

        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);

        // Sidebar kiri dengan menu "Riwayat" aktif
        SidebarApp sidebar = new SidebarApp("Riwayat");

        // Konten Utama
        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setSpacing(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        Label titleLabel = new Label("Riwayat Pemesanan");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1B365D"));

        // ScrollPane untuk menampung daftar card booking agar bisa di-scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F8FAFC; -fx-background-color: transparent; -fx-border-color: transparent;");

        listContainer = new VBox();
        listContainer.setSpacing(15);
        scrollPane.setContent(listContainer);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Panggil fungsi untuk mengisi data dari database
        loadHistoryData();

        mainContent.getChildren().addAll(titleLabel, scrollPane);
        this.getChildren().addAll(sidebar, mainContent);
    }

    private void loadHistoryData() {
        listContainer.getChildren().clear();

        try {
            int currentUserId = SessionManager.getCurrentUser().getId();
            
            // Ambil data riil pemesanan milik user yang sedang aktif login
            List<Booking> bookings = bookingService.getBookingsByUser(currentUserId);

            if (bookings.isEmpty()) {
                Label lblEmpty = new Label("Belum ada riwayat pemesanan space.");
                lblEmpty.setFont(Font.font("System", 14));
                lblEmpty.setTextFill(Color.web("#94A3B8"));
                listContainer.getChildren().add(lblEmpty);
                return;
            }

            for (Booking b : bookings) {
                listContainer.getChildren().add(createHistoryCard(b));
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat riwayat transaksi: " + e.getMessage());
        }
    }

    private HBox createHistoryCard(Booking booking) {
        HBox card = new HBox();
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.02), 8, 0, 0, 4); -fx-border-color: #F1F5F9; -fx-border-radius: 12;");
        card.setPadding(new Insets(20));
        card.setSpacing(20);
        card.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox();
        infoBox.setSpacing(6);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label lblEventName = new Label(booking.getEventName());
        lblEventName.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblEventName.setTextFill(Color.web("#1B365D"));

        Label lblDetails = new Label("📅 " + booking.getStartDate() + " s/d " + booking.getEndDate() + "  •  👥 " + booking.getGuestCount() + " Pax");
        lblDetails.setFont(Font.font("System", 12));
        lblDetails.setTextFill(Color.web("#64748B"));

        String formattedPrice = String.format("Total: Rp %,.0f", booking.getTotalPrice());
        Label lblPrice = new Label(formattedPrice);
        lblPrice.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblPrice.setTextFill(Color.web("#1B365D"));

        infoBox.getChildren().addAll(lblEventName, lblDetails, lblPrice);

        // Handle Badge Status Kelolaan Enum
        Label lblStatus = new Label(booking.getStatus().name());
        lblStatus.setFont(Font.font("System", FontWeight.BOLD, 10));
        lblStatus.setPadding(new Insets(5, 10, 5, 10));
        styleStatusBadge(lblStatus, booking.getStatus());

        VBox actionBox = new VBox(lblStatus);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setSpacing(10);

        // Jika statusnya WAITING_PAYMENT, munculkan tombol Bayar secara dinamis
        if (booking.getStatus() == BookingStatus.WAITING_PAYMENT) {
            Button btnBayar = new Button("Bayar Sekarang");
            btnBayar.setStyle("-fx-background-color: #F27442; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
            btnBayar.setOnAction(e -> bukaPilihanPembayaran(booking.getId()));
            actionBox.getChildren().add(btnBayar);
        }

        card.getChildren().addAll(infoBox, actionBox);
        return card;
    }

    private void bukaPilihanPembayaran(int bookingId) {
        // Dialog sederhana untuk memilih metode pembayaran enum
        ChoiceDialog<PaymentMethod> dialog = new ChoiceDialog<>(PaymentMethod.values()[0], PaymentMethod.values());
        dialog.setTitle("Pilih Metode Pembayaran");
        dialog.setHeaderText("Silakan pilih opsi pembayaran untuk Booking ID: #" + bookingId);
        dialog.setContentText("Metode:");

        dialog.showAndWait().ifPresent(method -> {
            try {
                // Eksekusi bayar ke backend via PaymentService
                boolean sukses = paymentService.payBooking(bookingId, method);
                if (sukses) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pembayaran Berhasil diproses!");
                    alert.showAndWait();
                    loadHistoryData(); // Refresh list riwayat langsung
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }
        });
    }

    private void styleStatusBadge(Label label, BookingStatus status) {
        switch (status) {
            case WAITING_PAYMENT -> label.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #D97706; -fx-background-radius: 4;");
            case APPROVED, COMPLETED -> label.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-background-radius: 4;");
            case REJECTED -> label.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-background-radius: 4;");
        }
    }
}