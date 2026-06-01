package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.service.DashboardService;

import java.text.NumberFormat;
import java.util.Locale;

public class AdminDashboardView {

    private final Stage stage;
    private final DashboardService dashboardService;

    public AdminDashboardView(Stage stage) {
        this.stage = stage;
        this.dashboardService = new DashboardService();
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8F9FA;");

        // ================= BERKAS SIDEBAR DI-REFACTOR KE SINI =================
        // Kita panggil class komponen terpisah dan tandai "Beranda" sebagai menu yang aktif
        SidebarAdmin sidebar = new SidebarAdmin(stage, "Beranda");
        root.setLeft(sidebar);

        // ================= KONTEN UTAMA =================
        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(35, 40, 35, 40));
        mainContent.setAlignment(Pos.TOP_LEFT);

        Label txtHeader = new Label("Beranda");
        txtHeader.setFont(Font.font("System", FontWeight.BOLD, 26));
        txtHeader.setStyle("-fx-text-fill: #1A202C;");
        mainContent.getChildren().add(txtHeader);

        // Baris Kartu Statistik
        HBox cardRow = new HBox(20);
        cardRow.setMaxWidth(Double.MAX_VALUE);

        VBox cardVenue = createStatCard("12", "Total Venue"); //
        VBox cardBooking = createStatCard("3", "Booking Aktif"); //
        VBox cardPemesan = createStatCard("100", "Total Pemesan"); //

        HBox.setHgrow(cardVenue, Priority.ALWAYS);
        HBox.setHgrow(cardBooking, Priority.ALWAYS);
        HBox.setHgrow(cardPemesan, Priority.ALWAYS);
        cardRow.getChildren().addAll(cardVenue, cardBooking, cardPemesan);
        mainContent.getChildren().add(cardRow);

        // Banner Pendapatan Besar
        VBox incomeBanner = new VBox(8);
        incomeBanner.setPadding(new Insets(25));
        incomeBanner.setAlignment(Pos.CENTER);
        incomeBanner.setStyle("-fx-background-color: #EBF8FF; -fx-background-radius: 15; -fx-border-color: #BEE3F8; -fx-border-radius: 15;");

        double totalPendapatan = dashboardService.getTotalIncome();
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedIncome = rupiahFormat.format(totalPendapatan).replace(",00", "");

        Label txtIncomeValue = new Label(formattedIncome.equals("Rp0") ? "Rp3.000.000.000" : formattedIncome); //
        txtIncomeValue.setFont(Font.font("System", FontWeight.BOLD, 28));
        txtIncomeValue.setStyle("-fx-text-fill: #2B6CB0;");

        Label txtIncomeLabel = new Label("Total Pendapatan");
        txtIncomeLabel.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        txtIncomeLabel.setStyle("-fx-text-fill: #4A5568;");

        incomeBanner.getChildren().addAll(txtIncomeValue, txtIncomeLabel);
        mainContent.getChildren().add(incomeBanner);

        // Bagian Filter & Tabel Mockup
        HBox filterRow = new HBox(15);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        Button btnDefaultFilter = new Button("📅  Default");
        btnDefaultFilter.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        ComboBox<String> comboFilter = new ComboBox<>();
        comboFilter.setPromptText("Filter");
        comboFilter.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E0; -fx-background-radius: 8; -fx-border-radius: 8;");
        filterRow.getChildren().addAll(btnDefaultFilter, comboFilter);
        mainContent.getChildren().add(filterRow);

        TableView<Object> miniTable = new TableView<>();
        miniTable.setPrefHeight(200);
        miniTable.setStyle("-fx-background-radius: 10; -fx-background-color: white;");
        TableColumn<Object, String> colNo = new TableColumn<>("No.");
        TableColumn<Object, String> colPemesan = new TableColumn<>("Nama Pemesan");
        TableColumn<Object, String> colVenue = new TableColumn<>("Nama Venue");
        TableColumn<Object, String> colTanggal = new TableColumn<>("Tanggal");
        TableColumn<Object, String> colStatus = new TableColumn<>("Status");
        miniTable.getColumns().addAll(colNo, colPemesan, colVenue, colTanggal, colStatus);
        mainContent.getChildren().add(miniTable);

        root.setCenter(mainContent);
        return new Scene(root, 960, 600);
    }

    private VBox createStatCard(String value, String labelTitle) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #1A365D; -fx-background-radius: 12;"); //

        Label numLabel = new Label(value);
        numLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        numLabel.setStyle("-fx-text-fill: white;");

        Label titleLabel = new Label(labelTitle);
        titleLabel.setFont(Font.font("System", FontWeight.LIGHT, 13));
        titleLabel.setStyle("-fx-text-fill: #CBD5E0;");

        card.getChildren().addAll(numLabel, titleLabel);
        return card;
    }
}