package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.service.DashboardService;
import youspace.utils.SessionManager;
import youspace.view.SidebarApp;

public class UserDashboardView extends HBox {

    private final DashboardService dashboardService;
    private Label lblTotalVenue, lblActiveBooking;

    public UserDashboardView() {
        // Menginisialisasi DashboardService untuk menarik data dari DAO/Database
        this.dashboardService = new DashboardService();
        
        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);
        
        // Panggil komponen Sidebar navigasi kiri dengan menu "Beranda" aktif
        SidebarApp sidebar = new SidebarApp("Beranda");

        // Container Konten Utama (Kanan)
        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setSpacing(25);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        // ==================== 1. TOP HEADER BAR ====================
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        // Sapa user yang sedang aktif login berdasarkan SessionManager
        String userName = (SessionManager.getCurrentUser() != null) ? SessionManager.getCurrentUser().getName() : "User";
        
        VBox welcomeBox = new VBox();
        Label titleLabel = new Label("Halo, " + userName + " 👋");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1B365D"));
        
        Label subtitleLabel = new Label("Temukan space terbaik untuk acaramu hari ini.");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
        subtitleLabel.setTextFill(Color.web("#64748B"));
        welcomeBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari gedung atau aula...");
        searchField.setPrefWidth(250);
        searchField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 15 8 15;");
        
        HBox topSpacer = new HBox();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(welcomeBox, topSpacer, searchField);

        // ==================== 2. SUMMARY CARDS (INTEGRASI SERVICE) ====================
        HBox summaryCardsBox = new HBox();
        summaryCardsBox.setSpacing(20);
        
        lblTotalVenue = new Label("0");
        lblActiveBooking = new Label("0");
        
        // Membuat card informasi rangkuman data
        summaryCardsBox.getChildren().addAll(
            createInfoCard(lblTotalVenue, "Total Venue", "#1B365D"),
            createInfoCard(lblActiveBooking, "Booking Aktif", "#F27442"),
            createInfoCard(new Label("0"), "Venue Disimpan", "#1B365D")
        );

        // ==================== 3. SECTION BEST VENUES (PROMO/HIGHLIGHT) ====================
        Label sectionLabel = new Label("Rekomendasi Populer");
        sectionLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionLabel.setTextFill(Color.web("#1B365D"));

        VBox venueListBox = new VBox();
        venueListBox.setSpacing(15);
        
        // Contoh tampilan card highlight statis sebelum masuk ke menu katalog penuh
        venueListBox.getChildren().addAll(
            createHighlightCard("Grand Celestia Ballroom", "BALLROOM", "Rp 80.000.000 / Hari", "Kapasitas 1000 Pax"),
            createHighlightCard("Velora Creative Studio", "STUDIO", "Rp 5.000.000 / Hari", "Kapasitas 50 Pax")
        );

        // Satukan semua komponen ke panel utama
        mainContent.getChildren().addAll(headerBox, summaryCardsBox, sectionLabel, venueListBox);
        this.getChildren().addAll(sidebar, mainContent);

        // Eksekusi penarikan data riil dari database
        refreshDashboardData();
    }

    private VBox createInfoCard(Label numLabel, String title, String hexColor) {
        VBox card = new VBox();
        card.setPrefWidth(180);
        card.setPadding(new Insets(20));
        card.setSpacing(5);
        card.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 14; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        numLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        numLabel.setTextFill(Color.WHITE);

        Label descLabel = new Label(title);
        descLabel.setFont(Font.font("System", FontWeight.MEDIUM, 12));
        descLabel.setTextFill(Color.web("#E2E8F0"));

        card.getChildren().addAll(numLabel, descLabel);
        return card;
    }

    private HBox createHighlightCard(String name, String type, String price, String capacity) {
        HBox card = new HBox();
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 8, 0, 0, 4); -fx-border-color: #F1F5F9; -fx-border-radius: 12;");
        card.setPadding(new Insets(15));
        card.setSpacing(15);
        card.setAlignment(Pos.CENTER_LEFT);

        // Kotak representasi gambar gedung
        VBox imgPlaceholder = new VBox();
        imgPlaceholder.setPrefSize(100, 70);
        imgPlaceholder.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 8;"); 

        VBox infoBox = new VBox();
        infoBox.setSpacing(4);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.web("#1B365D"));

        HBox tagsBox = new HBox();
        tagsBox.setSpacing(8);
        
        Label typeTag = new Label(type);
        typeTag.setFont(Font.font("System", FontWeight.BOLD, 9));
        typeTag.setTextFill(Color.web("#1B365D"));
        typeTag.setStyle("-fx-background-color: #E2E8F0; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

        Label capTag = new Label(capacity);
        capTag.setFont(Font.font("System", FontWeight.NORMAL, 11));
        capTag.setTextFill(Color.web("#64748B"));
        tagsBox.getChildren().addAll(typeTag, capTag);

        Label priceLabel = new Label(price);
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        priceLabel.setTextFill(Color.web("#F27442"));

        infoBox.getChildren().addAll(nameLabel, tagsBox, priceLabel);

        Button btnLihat = new Button("Lihat Detail");
        btnLihat.setStyle("-fx-background-color: transparent; -fx-text-fill: #1B365D; -fx-border-color: #1B365D; -fx-border-radius: 6; -fx-font-weight: bold; -fx-padding: 6 12 6 12; -fx-cursor: hand;");
        
        // Ketika tombol dipasang aksi klik, dia akan melompat langsung ke halaman Grid Katalog
        btnLihat.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(new Scene(new VenueCardGrid(), 1050, 650));
        });

        card.getChildren().addAll(imgPlaceholder, infoBox, btnLihat);
        return card;
    }

    /**
     * Mengambil data asli via DashboardService dan memperbarui teks angka di UI
     */
    public void refreshDashboardData() {
        try {
            int totalVenue = dashboardService.getTotalVenue();
            int activeBooking = dashboardService.getActiveBooking();

            lblTotalVenue.setText(String.valueOf(totalVenue));
            lblActiveBooking.setText(String.valueOf(activeBooking));
        } catch (Exception e) {
            System.err.println("Gagal memuat data rangkuman dashboard: " + e.getMessage());
        }
    }
}