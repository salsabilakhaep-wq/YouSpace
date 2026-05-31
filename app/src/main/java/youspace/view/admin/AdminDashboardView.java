package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminDashboardView extends HBox {

    public AdminDashboardView() {
        // --- 1. SET LAYOUT UTAMA ---
        this.setStyle("-fx-background-color: #F8FAFC;"); // Background abu-abu terang netral figma
        this.setPrefSize(1050, 650);

        // --- 2. PASANG SIDEBAR ADMIN ---
        SidebarAdmin sidebar = new SidebarAdmin("Beranda");
        this.getChildren().add(sidebar);

        // --- 3. CONTAINER KONTEN UTAMA (Kanan) ---
        VBox contentArea = new VBox();
        contentArea.setPadding(new Insets(40, 40, 30, 40));
        contentArea.setSpacing(24);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // Judul Dashboard
        Label titleLabel = new Label("Beranda");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1E293B"));
        contentArea.getChildren().add(titleLabel);

        // --- 4. BARIS KARTU STATISTIK (TOP CARDS) ---
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        
        // Membuat 3 kartu ringkasan sesuai data figma
        VBox cardVenue = createStatCard("12", "Total Venue");
        VBox cardBooking = createStatCard("3", "Booking Aktif");
        VBox cardPemesanan = createStatCard("100", "Total Pemesanan");

        statsGrid.add(cardVenue, 0, 0);
        statsGrid.add(cardBooking, 1, 0);
        statsGrid.add(cardPemesanan, 2, 0);
        contentArea.getChildren().add(statsGrid);

        // --- 5. KARTU TOTAL PENDAPATAN (REVENUE CARD) ---
        VBox revenueCard = new VBox();
        revenueCard.setPadding(new Insets(25));
        revenueCard.setAlignment(Pos.CENTER);
        revenueCard.setSpacing(6);
        // Memakai warna soft-blue muda melengkung serasi figma
        revenueCard.setStyle("-fx-background-color: #E0F2FE; -fx-background-radius: 14;");
        
        Label revenueValue = new Label("Rp3.000.000.000");
        revenueValue.setFont(Font.font("System", FontWeight.BOLD, 28));
        revenueValue.setTextFill(Color.web("#1B365D"));

        Label revenueLabel = new Label("Total Pendapatan");
        revenueLabel.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        revenueLabel.setTextFill(Color.web("#64748B"));

        revenueCard.getChildren().addAll(revenueValue, revenueLabel);
        contentArea.getChildren().add(revenueCard);

        // --- 6. BARIS KONTROL FILTER TABEL ---
        HBox filterRow = new HBox(12);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        Button btnDefault = new Button("📅  Default");
        btnDefault.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 16; -fx-text-fill: #334155; -fx-font-weight: bold;");
        
        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("Filter", "Hari Ini", "Bulan Ini", "Tahun Ini");
        filterCombo.setValue("Filter");
        filterCombo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 5 12;");
        filterCombo.setPrefWidth(130);

        filterRow.getChildren().addAll(btnDefault, filterCombo);
        contentArea.getChildren().add(filterRow);

        // --- 7. TABEL TRANSAKSI TERBARU ---
        TableView<Object> transactionTable = new TableView<>();
        transactionTable.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2E8F0; -fx-border-radius: 12;");
        VBox.setVgrow(transactionTable, Priority.ALWAYS);

        // Setup kolom tabel sesuai struktur mockup figma
        TableColumn<Object, String> colNo = new TableColumn<>("No.");
        colNo.setPrefWidth(50);
        colNo.setStyle("-fx-alignment: CENTER;");

        TableColumn<Object, String> colPemesan = new TableColumn<>("Pemesan");
        colPemesan.setPrefWidth(160);

        TableColumn<Object, String> colRuangan = new TableColumn<>("Ruangan");
        colRuangan.setPrefWidth(180);

        TableColumn<Object, String> colTipe = new TableColumn<>("Tipe Ruangan");
        colTipe.setPrefWidth(140);

        TableColumn<Object, String> colJumlah = new TableColumn<>("Jumlah Transaksi");
        colJumlah.setPrefWidth(160);

        transactionTable.getColumns().addAll(colNo, colPemesan, colRuangan, colTipe, colJumlah);
        
        // Membungkus tabel dengan ScrollPane pelindung agar responsif saat ditarik
        ScrollPane tableScroll = new ScrollPane(transactionTable);
        tableScroll.setFitToWidth(true);
        tableScroll.setFitToHeight(true);
        tableScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(tableScroll, Priority.ALWAYS);

        contentArea.getChildren().add(tableScroll);
        this.getChildren().add(contentArea);
    }

    // Helper Method Praktis untuk Membuat Kartu Ringkasan Atas
    private VBox createStatCard(String value, String title) {
        VBox card = new VBox();
        card.setPadding(new Insets(25, 30, 25, 30));
        card.setSpacing(8);
        card.setPrefWidth(225);
        // Memakai warna navy pekat melengkung halus
        card.setStyle("-fx-background-color: #1B365D; -fx-background-radius: 14;");

        Label valLabel = new Label(value);
        valLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        valLabel.setTextFill(Color.WHITE);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.MEDIUM, 13));
        titleLabel.setTextFill(Color.web("#94A3B8")); // Abu-abu pudar kontras

        card.getChildren().addAll(valLabel, titleLabel);
        return card;
    }
}