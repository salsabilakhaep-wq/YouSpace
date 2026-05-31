package youspace.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminVenueView extends HBox {

    // Penampung data statis agar perubahan data (Tambah/Edit) tidak hilang saat pindah menu
    public static ObservableList<MockVenue> venueList = FXCollections.observableArrayList();

    static {
        // Data awal (Seed Data) sesuai contoh mockup figma
        if (venueList.isEmpty()) {
            venueList.add(new MockVenue(1, "Celestia Ballroom", "Ballroom", "Baik", "Tersedia", "80.000.000", "Ballroom mewah dengan kapasitas hingga 1000 orang."));
            venueList.add(new MockVenue(2, "Aurora Room", "Meeting Room", "Baik", "Sedang Digunakan", "15.000.000", "Ruang rapat modern dengan fasilitas lengkap videoconference."));
            venueList.add(new MockVenue(3, "Titan Hall", "Aula", "Perbaikan", "Tersedia", "35.000.000", "Aula serbaguna, sangat cocok untuk seminar besar dan wisuda."));
        }
    }

    public AdminVenueView() {
        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);

        // Sidebar Navigasi Admin
        SidebarAdmin sidebar = new SidebarAdmin("Venue");
        this.getChildren().add(sidebar);

        VBox contentArea = new VBox();
        contentArea.setPadding(new Insets(40, 40, 30, 40));
        contentArea.setSpacing(24);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // Header: Judul + Tombol Tambah Venue
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Kelola Venue");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1E293B"));

        VBox spacerHeader = new VBox();
        HBox.setHgrow(spacerHeader, Priority.ALWAYS);

        Button btnTambah = new Button("+ Tambah Venue");
        btnTambah.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
        btnTambah.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(new Scene(new AdminVenueForm(false, null), 1050, 650));
        });

        headerRow.getChildren().addAll(titleLabel, spacerHeader, btnTambah);
        contentArea.getChildren().add(headerRow);

        // Filter Pills Categories
        HBox categoryRow = new HBox(10);
        categoryRow.setAlignment(Pos.CENTER_LEFT);
        String[] categories = {"Semua", "Aula", "Ballroom", "Meeting Room", "Studio"};
        for (String cat : categories) {
            Button btnCat = new Button(cat);
            if (cat.equals("Semua")) {
                btnCat.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 6 16; -fx-font-weight: bold;");
            } else {
                btnCat.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #475569; -fx-background-radius: 12; -fx-padding: 6 16; -fx-font-weight: bold; -fx-cursor: hand;");
            }
            categoryRow.getChildren().add(btnCat);
        }
        contentArea.getChildren().add(categoryRow);

        // TableView Data Venue
        TableView<MockVenue> venueTable = new TableView<>();
        venueTable.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2E8F0; -fx-border-radius: 12;");
        
        TableColumn<MockVenue, Integer> colNo = new TableColumn<>("No.");
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colNo.setPrefWidth(50);
        colNo.setStyle("-fx-alignment: CENTER;");

        TableColumn<MockVenue, String> colNama = new TableColumn<>("Nama Ruangan");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colNama.setPrefWidth(200);

        TableColumn<MockVenue, String> colTipe = new TableColumn<>("Tipe Ruangan");
        colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        colTipe.setPrefWidth(130);

        TableColumn<MockVenue, String> colKondisi = new TableColumn<>("Kondisi Ruangan");
        colKondisi.setCellValueFactory(new PropertyValueFactory<>("kondisi"));
        colKondisi.setPrefWidth(130);

        TableColumn<MockVenue, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(140);

        venueTable.getColumns().addAll(colNo, colNama, colTipe, colKondisi, colStatus);
        venueTable.setItems(venueList);

        // Double click baris untuk memicu dialog detail figma
        venueTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && venueTable.getSelectionModel().getSelectedItem() != null) {
                MockVenue selectedVenue = venueTable.getSelectionModel().getSelectedItem();
                Stage currentStage = (Stage) this.getScene().getWindow();
                AdminVenueDetailDialog detailDialog = new AdminVenueDetailDialog(currentStage, selectedVenue);
                detailDialog.showAndWait();
            }
        });

        ScrollPane scrollPane = new ScrollPane(venueTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        contentArea.getChildren().add(scrollPane);
        this.getChildren().add(contentArea);
    }

    // --- INNER CLASS MODEL UNTUK MOCKING DATA DATA VENUE ---
    public static class MockVenue {
        private int no;
        private String nama;
        private String tipe;
        private String kondisi;
        private String status;
        private String harga;
        private String deskripsi;

        public MockVenue(int no, String nama, String tipe, String kondisi, String status, String harga, String deskripsi) {
            this.no = no;
            this.nama = nama;
            this.tipe = tipe;
            this.kondisi = kondisi;
            this.status = status;
            this.harga = harga;
            this.deskripsi = deskripsi;
        }

        public int getNo() { return no; }
        public void setNo(int no) { this.no = no; }
        public String getNama() { return nama; }
        public void setNama(String nama) { this.nama = nama; }
        public String getTipe() { return tipe; }
        public void setTipe(String tipe) { this.tipe = tipe; }
        public String getKondisi() { return kondisi; }
        public void setKondisi(String kondisi) { this.kondisi = kondisi; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getHarga() { return harga; }
        public void setHarga(String harga) { this.harga = harga; }
        public String getDeskripsi() { return deskripsi; }
        public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    }
}