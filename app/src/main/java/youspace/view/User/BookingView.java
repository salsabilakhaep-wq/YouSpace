package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.view.user.components.SidebarUser;
public class BookingView {
    private final Stage stage;

    public BookingView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        
        // Menggunakan Sidebar komponen Anda, tandai menu "Booking" sebagai aktif
        root.setLeft(new SidebarUser(stage, "Booking"));

        // 1. KONTEN UTAMA (Sisi Kanan)
        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(40));
        mainContent.setStyle("-fx-background-color: #FAFAFA;");

        // Header Title
        Label title = new Label("Booking");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #003459;");

        // 2. FILTER TABS (Semua, Selesai, Dalam Proses)
        HBox filterTabs = new HBox(15);
        
        Button btnSemua = new Button("Semua");
        btnSemua.setStyle("-fx-background-color: #003F66; -fx-text-fill: white; -fx-background-radius: 20px; -fx-padding: 8 24; -fx-font-weight: bold;");
        
        Button btnSelesai = new Button("Selesai");
        btnSelesai.setStyle("-fx-background-color: #DCEEFA; -fx-text-fill: #003459; -fx-background-radius: 20px; -fx-padding: 8 24; -fx-font-weight: bold;");
        
        Button btnProses = new Button("Dalam Proses");
        btnProses.setStyle("-fx-background-color: #DCEEFA; -fx-text-fill: #003459; -fx-background-radius: 20px; -fx-padding: 8 24; -fx-font-weight: bold;");
        
        filterTabs.getChildren().addAll(btnSemua, btnSelesai, btnProses);

        // 3. CARDS CONTAINER (Menggunakan FlowPane agar layout otomatis turun ke bawah jika layar mengecil)
        FlowPane cardsGrid = new FlowPane();
        cardsGrid.setHgap(25);
        cardsGrid.setVgap(25);
        cardsGrid.setStyle("-fx-background-color: transparent;");

        // Membuat Kartu 1: Calestia Ballroom (Status: Dalam Proses)
        VBox card1 = createVenueCard(
            "Calestia Ballroom", 
            "Ballroom", 
            "Ballroom Luxury First menawarkan ruang berskala besar dengan fasilitas mutakhir dan pelayanan premium. Pilihan utama untuk konferensi global, peluncuran produk, dan gala dinner yang membutuhkan impresi profesional berkelas.",
            80000000,
            "Dalam Proses",
            "#CBEAFA", // Background Status (Biru Muda)
            "#003459"  // Warna Teks Status
        );

        // Membuat Kartu 2: Velora Hall (Status: Selesai)
        VBox card2 = createVenueCard(
            "Velora Hall", 
            "Ballroom", 
            "Ballroom Luxury First menawarkan ruang berskala besar dengan fasilitas mutakhir dan pelayanan premium. Pilihan utama untuk konferensi global, peluncuran produk, dan gala dinner yang membutuhkan impresi profesional berkelas.",
            80000000,
            "Selesai",
            "#B6FFA5", // Background Status (Hijau Muda)
            "#1E5500"  // Warna Teks Status
        );

        cardsGrid.getChildren().addAll(card1, card2);

        // Pembungkus Scroll Pane agar halaman bisa digulir vertikal
        ScrollPane scrollPane = new ScrollPane(cardsGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        // Gabungkan Judul, Filter, dan Grid Kartu ke Konten Utama
        mainContent.getChildren().addAll(title, filterTabs, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.setCenter(mainContent);

        return new Scene(root, 1000, 700);
    }

    /**
     * Helper Method untuk membuat komponen Kartu Venue secara dinamis (Figma Component)
     */
    private VBox createVenueCard(String name, String category, String desc, double pricePerDay, String statusText, String statusBgColor, String statusTextColor) {
        VBox card = new VBox();
        card.setPrefWidth(330);
        card.setMaxWidth(330);
        card.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-background-radius: 20px; " +
            "-fx-border-radius: 20px; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 15, 0, 0, 8);"
        );

        // 1. Gambar Placeholder Area (Atas)
        VBox imagePlaceholder = new VBox();
        imagePlaceholder.setPrefHeight(180);
        imagePlaceholder.setStyle("-fx-background-color: #CCCCCC; -fx-background-radius: 20px 20px 0px 0px;"); // Rounded hanya atas

        // 2. Bagian Detail Informasi (Bawah)
        VBox details = new VBox(10);
        details.setPadding(new Insets(20));

        Label lblName = new Label(name);
        lblName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblName.setStyle("-fx-text-fill: #000000;");

        Label lblCategory = new Label(category);
        lblCategory.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        lblCategory.setStyle("-fx-background-color: #DCEEFA; -fx-text-fill: #003459; -fx-padding: 4 12; -fx-background-radius: 5px;");

        Label lblDesc = new Label(desc);
        lblDesc.setFont(Font.font("Segoe UI", 12));
        lblDesc.setStyle("-fx-text-fill: #666666; -fx-line-spacing: 3px;");
        lblDesc.setWrapText(true);
        lblDesc.setPrefHeight(90); // Mengunci tinggi text agar sejajar antar kartu

        // Footer di dalam kartu (Harga + Tombol Status)
        HBox cardFooter = new HBox();
        cardFooter.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(cardFooter, new Insets(10, 0, 0, 0));

        Label lblPrice = new Label("Rp " + String.format("%,d", (int) pricePerDay) + "/hari");
        lblPrice.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        lblPrice.setStyle("-fx-text-fill: #000000;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnStatus = new Button(statusText + "  →");
        btnStatus.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        btnStatus.setStyle(
            "-fx-background-color: " + statusBgColor + "; " +
            "-fx-text-fill: " + statusTextColor + "; " +
            "-fx-background-radius: 10px; " +
            "-fx-padding: 8 16; " +
            "-fx-cursor: hand;"
        );

        cardFooter.getChildren().addAll(lblPrice, spacer, btnStatus);
        details.getChildren().addAll(lblName, lblCategory, lblDesc, cardFooter);
        card.getChildren().addAll(imagePlaceholder, details);

        return card;
    }
}
