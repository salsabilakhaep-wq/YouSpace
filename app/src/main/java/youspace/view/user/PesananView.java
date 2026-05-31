package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import youspace.enums.BookingStatus;
import youspace.models.Booking;
import youspace.models.Venue;
import youspace.service.BookingService;
import youspace.service.VenueService;
import youspace.utils.SessionManager;
import youspace.view.SidebarApp;

import java.util.ArrayList;
import java.util.List;

public class PesananView extends HBox {

    private final BookingService bookingService;
    private final VenueService venueService;
    private final GridPane gridContainer;
    private final HBox filterBar;
    private List<Booking> allUserBookings;

    public PesananView() {
        this.bookingService = new BookingService();
        this.venueService = new VenueService();
        this.allUserBookings = new ArrayList<>();

        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);

        // Sidebar kiri dengan menu "Pesanan" aktif
        SidebarApp sidebar = new SidebarApp("Pesanan");

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setSpacing(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        Label titleLabel = new Label("Pesanan");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1B365D"));

        // Barisan Filter Tab sesuai Desain Figma (Semua, Selesai, Dalam Proses)
        filterBar = new HBox();
        filterBar.setSpacing(12);
        setupFilterTabs();

        // Main Scroll Container
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F8FAFC; -fx-background-color: transparent; -fx-border-color: transparent;");

        gridContainer = new GridPane();
        gridContainer.setHgap(25);
        gridContainer.setVgap(25);
        scrollPane.setContent(gridContainer);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Load data pertama kali
        fetchDataFromBackend();
        renderFilteredData("Semua");

        mainContent.getChildren().addAll(titleLabel, filterBar, scrollPane);
        this.getChildren().addAll(sidebar, mainContent);
    }

    private void setupFilterTabs() {
        String[] tabs = {"Semua", "Selesai", "Dalam Proses"};

        for (int i = 0; i < tabs.length; i++) {
            String tabName = tabs[i];
            Button btnTab = new Button(tabName);
            btnTab.setPadding(new Insets(8, 20, 8, 20));
            btnTab.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
            btnTab.setCursor(javafx.scene.Cursor.HAND);

            // Styling default (Tab pertama "Semua" dalam keadaan aktif)
            if (i == 0) {
                btnTab.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-background-radius: 8;");
            } else {
                btnTab.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #64748B; -fx-background-radius: 8;");
            }

            btnTab.setOnAction(e -> {
                // Reset semua warna tab ke default abu-abu figma
                filterBar.getChildren().forEach(node -> 
                    node.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #64748B; -fx-background-radius: 8;")
                );
                // Aktifkan tab terpilih (Navy Blue)
                btnTab.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-background-radius: 8;");
                
                // Saring data visual secara real-time
                renderFilteredData(tabName);
            });

            filterBar.getChildren().add(btnTab);
        }
    }

    private void fetchDataFromBackend() {
        try {
            int currentUserId = SessionManager.getCurrentUser().getId();
            allUserBookings = bookingService.getBookingsByUser(currentUserId);
        } catch (Exception e) {
            System.err.println("Gagal mengambil data pesanan: " + e.getMessage());
        }
    }

    private void renderFilteredData(String filterType) {
        gridContainer.getChildren().clear();

        if (allUserBookings == null || allUserBookings.isEmpty()) {
            Label lblEmpty = new Label("Belum ada riwayat pesanan.");
            lblEmpty.setFont(Font.font("System", 14));
            lblEmpty.setTextFill(Color.web("#94A3B8"));
            gridContainer.add(lblEmpty, 0, 0);
            return;
        }

        int column = 0, row = 0;

        for (Booking booking : allUserBookings) {
            boolean matches = false;

            // Logika mapping pencocokan filter tab figma terhadap Enum DB kamu
            if (filterType.equals("Semua")) {
                matches = true;
            } else if (filterType.equals("Selesai")) {
                matches = (booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.COMPLETED);
            } else if (filterType.equals("Dalam Proses")) {
                matches = (booking.getStatus() == BookingStatus.WAITING_PAYMENT);
            }

            if (matches) {
                VBox card = createPesananCard(booking);
                gridContainer.add(card, column, row);

                column++;
                if (column == 2) { // 2 Kolom menyamping persis seperti screenshot Figma kamu
                    column = 0;
                    row++;
                }
            }
        }
    }

    private VBox createPesananCard(Booking booking) {
        VBox card = new VBox();
        card.setPrefWidth(320);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.02), 12, 0, 0, 6); -fx-border-color: #F1F5F9; -fx-border-radius: 18;");
        card.setPadding(new Insets(0, 0, 16, 0));

        // 1. Placeholder Gambar Atas dengan Bookmark hiasan pasif
        VBox imgBox = new VBox();
        imgBox.setPrefHeight(150);
        imgBox.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 18 18 0 0;");
        imgBox.setPadding(new Insets(12));
        imgBox.setAlignment(Pos.TOP_RIGHT);
        
        Label lblBookmarkPasif = new Label("🔖"); // Sesuai icon figma di pojok kanan gambar pesanan
        lblBookmarkPasif.setStyle("-fx-font-size: 14; -fx-opacity: 0.7;");
        imgBox.getChildren().add(lblBookmarkPasif);

        // 2. Info Detail Box bawah gambar
        VBox infoBox = new VBox();
        infoBox.setPadding(new Insets(16, 16, 0, 16));
        infoBox.setSpacing(6);

        Label nameLabel = new Label(booking.getEventName()); // Atw ambil via venueService.getVenueById(booking.getVenueId()).getName()
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.web("#1B365D"));

        Label typeLabel = new Label("Kapasitas " + booking.getGuestCount() + " Pax");
        typeLabel.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #3B82F6; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
        
        HBox badgeContainer = new HBox(typeLabel);

        Label descLabel = new Label("Pemesanan space YouSpace untuk rentang tanggal " + booking.getStartDate() + " hingga " + booking.getEndDate());
        descLabel.setFont(Font.font("System", 12));
        descLabel.setTextFill(Color.web("#64748B"));
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(40);

        // 3. Row Harga dan Badge Status di bagian paling bawah card
        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        bottomRow.setPadding(new Insets(12, 0, 0, 0));

        String formattedPrice = String.format("Rp %,.0f/hari", booking.getTotalPrice());
        Label priceLabel = new Label(formattedPrice);
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        priceLabel.setTextFill(Color.web("#1B365D"));
        HBox.setHgrow(priceLabel, Priority.ALWAYS);

        // Tombol status penanda bergaya Figma (Biru Muda untuk Proses, Hijau Muda untuk Selesai)
        Button statusBadgeBtn = new Button();
        statusBadgeBtn.setFont(Font.font("System", FontWeight.BOLD, 10));
        statusBadgeBtn.setPadding(new Insets(5, 12, 5, 12));

        if (booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.COMPLETED) {
            statusBadgeBtn.setText("Selesai  →");
            statusBadgeBtn.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-background-radius: 6;");
        } else {
            statusBadgeBtn.setText("Dalam Proses  →");
            statusBadgeBtn.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1D4ED8; -fx-background-radius: 6;");
        }

        bottomRow.getChildren().addAll(priceLabel, statusBadgeBtn);
        infoBox.getChildren().addAll(nameLabel, badgeContainer, descLabel, bottomRow);
        card.getChildren().addAll(imgBox, infoBox);

        return card;
    }
}