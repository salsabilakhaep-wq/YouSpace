package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import youspace.models.Venue;
import youspace.service.BookingService;
import youspace.utils.SessionManager;

import java.time.format.DateTimeFormatter;

public class BookingFormDialog extends Stage {

    private final BookingService bookingService;
    private final Venue venue;

    private TextField txtEventName, txtGuestCount;
    private DatePicker pickerStart, pickerEnd;
    private TextArea txtNote;

    public BookingFormDialog(Venue venue, Runnable onBookingSuccess) {
        this.venue = venue;
        this.bookingService = new BookingService();

        // Konfigurasi agar dialog ini fokus (tidak bisa klik window utama sebelum ditutup)
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Formulir Sewa Gedung");
        this.setResizable(false);

        VBox container = new VBox();
        container.setPadding(new Insets(25));
        container.setSpacing(15);
        container.setStyle("-fx-background-color: #FFFFFF;");

        // Header Dialog
        Label lblHeader = new Label("Sewa: " + venue.getName());
        lblHeader.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblHeader.setTextFill(Color.web("#1B365D"));

        // Field 1: Nama Acara
        Label lblEvent = new Label("Nama Acara / Kegiatan");
        lblEvent.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        txtEventName = new TextField();
        txtEventName.setPromptText("Contoh: Seminar Nasional Teknologi");

        // Field 2: Jumlah Tamu
        Label lblGuests = new Label("Jumlah Tamu / Pax (Maksimal: " + venue.getCapacity() + ")");
        lblGuests.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        txtGuestCount = new TextField();
        txtGuestCount.setPromptText("Masukkan total pax");

        // Field 3 & 4: Rentang Tanggal (HBox)
        HBox dateBox = new HBox();
        dateBox.setSpacing(15);

        VBox startBox = new VBox();
        startBox.setSpacing(5);
        Label lblStart = new Label("Tanggal Mulai");
        lblStart.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        pickerStart = new DatePicker();
        startBox.getChildren().addAll(lblStart, pickerStart);

        VBox endBox = new VBox();
        endBox.setSpacing(5);
        Label lblEnd = new Label("Tanggal Selesai");
        lblEnd.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        pickerEnd = new DatePicker();
        endBox.getChildren().addAll(lblEnd, pickerEnd);

        dateBox.getChildren().addAll(startBox, endBox);

        // Field 5: Catatan Tambahan
        Label lblNote = new Label("Catatan Tambahan (Opsional)");
        lblNote.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        txtNote = new TextArea();
        txtNote.setPrefHeight(70);
        txtNote.setPromptText("Tulis kebutuhan khusus panggung, tata suara, dll...");

        // Row Tombol Aksi
        HBox actionRow = new HBox();
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.setSpacing(10);

        Button btnBatal = new Button("Batal");
        btnBatal.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #475569; -fx-background-radius: 6; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnBatal.setOnAction(e -> this.close());

        Button btnKirim = new Button("Ajukan Pemesanan");
        btnKirim.setStyle("-fx-background-color: #F27442; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnKirim.setOnAction(e -> prosesPengajuanBooking(onBookingSuccess));

        actionRow.getChildren().addAll(btnBatal, btnKirim);

        // Styling input field seragam
        styleFields();

        container.getChildren().addAll(lblHeader, lblEvent, txtEventName, lblGuests, txtGuestCount, dateBox, lblNote, txtNote, actionRow);
        
        Scene scene = new Scene(container, 450, 520);
        this.setScene(scene);
    }

    private void prosesPengajuanBooking(Runnable onBookingSuccess) {
        try {
            int currentUserId = SessionManager.getCurrentUser().getId();
            
            // Konversi nilai input teks dan tanggal
            String eventName = txtEventName.getText();
            int guestCount = txtGuestCount.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtGuestCount.getText().trim());
            
            // Format LocalDate JavaFX menjadi string "yyyy-MM-dd" agar pas dengan parameter BookingService kamu
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDate = (pickerStart.getValue() != null) ? pickerStart.getValue().format(formatter) : "";
            String endDate = (pickerEnd.getValue() != null) ? pickerEnd.getValue().format(formatter) : "";
            String note = txtNote.getText();

            // Panggil backend via BookingService
            boolean berhasil = bookingService.createBooking(
                    currentUserId, venue.getId(), eventName, guestCount, startDate, endDate, note
            );

            if (berhasil) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Booking Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Pesanan berhasil dikirim! Silakan lakukan pembayaran di menu Riwayat.");
                alert.showAndWait();
                
                onBookingSuccess.run(); // Callback untuk merefresh halaman katalog
                this.close();
            }
        } catch (NumberFormatException nfe) {
            showErrorAlert("Jumlah tamu harus diisi dengan angka valid.");
        } catch (IllegalArgumentException iae) {
            // Menangkap semua lemparan throw dari aturan validasi BookingService-mu
            showErrorAlert(iae.getMessage());
        }
    }

    private void styleFields() {
        txtEventName.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-padding: 8;");
        txtGuestCount.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-padding: 8;");
        pickerStart.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-padding: 2;");
        pickerEnd.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-padding: 2;");
        txtNote.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-padding: 8;");
    }

    private void showErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan Validasi");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}