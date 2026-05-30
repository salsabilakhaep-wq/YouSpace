package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import youspace.enums.VenueCategory;
import youspace.models.Venue;
import youspace.service.VenueService;
import youspace.view.SidebarApp;

import java.util.List;

public class VenueCardGrid extends HBox {

    private final VenueService venueService;
    private GridPane gridLayout;
    private HBox filterBar;

    public VenueCardGrid() {
        // Menggunakan VenueService sebagai jembatan ke database via DAO
        this.venueService = new VenueService();

        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);

        SidebarApp sidebar = new SidebarApp("Venue");

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setSpacing(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        Label titleLabel = new Label("Eksplorasi Space");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1B365D"));

        // Barisan Tombol Filter Kategori (Aula, Studio, Ballroom, dll.)
        filterBar = new HBox();
        filterBar.setSpacing(10);
        setupFilterButtons();

        // Menggunakan ScrollPane agar jika isi gedung banyak, halaman bisa di-scroll ke bawah
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F8FAFC; -fx-background-color: transparent; -fx-border-color: transparent;");

        gridLayout = new GridPane();
        gridLayout.setHgap(20);
        gridLayout.setVgap(20);
        gridLayout.setPadding(new Insets(5, 5, 5, 5));
        
        scrollPane.setContent(gridLayout);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Muat semua data pertama kali tanpa filter kategori (null)
        loadVenueData(null);

        mainContent.getChildren().addAll(titleLabel, filterBar, scrollPane);
        this.getChildren().addAll(sidebar, mainContent);
    }

    private void setupFilterButtons() {
        // Array mapping antara teks tombol visual dan nilai asli Enum VenueCategory kamu
        Object[][] categories = {
            {"Semua Space", null},
            {"Aula", VenueCategory.AULA},
            {"Ballroom", VenueCategory.BALLROOM},
            {"Meeting Room", VenueCategory.MEETING_ROOM},
            {"Studio", VenueCategory.STUDIO}
        };

        for (int i = 0; i < categories.length; i++) {
            String buttonText = (String) categories[i][0];
            VenueCategory categoryEnum = (VenueCategory) categories[i][1];
            
            Button btn = new Button(buttonText);
            btn.setPadding(new Insets(8, 16, 8, 16));
            btn.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
            btn.setStyle("-fx-cursor: hand;");
            
            // Set warna aktif pada pilihan pertama ("Semua Space")
            if (i == 0) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: #1B365D; -fx-text-fill: white; -fx-background-radius: 20;");
            } else {
                btn.setStyle(btn.getStyle() + "-fx-background-color: #E2E8F0; -fx-text-fill: #64748B; -fx-background-radius: 20;");
            }

            btn.setOnAction(e -> {
                // Reset semua tombol filter ke warna abu-abu standar
                filterBar.getChildren().forEach(node -> 
                    node.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #64748B; -fx-background-radius: 20; -fx-cursor: hand;")
                );
                // Beri warna Navy Blue aktif ke tombol yang sedang diklik
                btn.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand;");
                
                // Ambil data ulang dari service sesuai filter yang dipilih
                loadVenueData(categoryEnum);
            });

            filterBar.getChildren().add(btn);
        }
    }

    private void loadVenueData(VenueCategory categoryFilter) {
        gridLayout.getChildren().clear(); 
        List<Venue> venueList;
        
        try {
            if (categoryFilter == null) {
                // Panggil backend service untuk ambil keseluruhan data
                venueList = venueService.getAllVenues();
            } else {
                // Panggil backend service berdasarkan filter kategori enum khusus
                venueList = venueService.getVenuesByCategory(categoryFilter);
            }

            int column = 0, row = 0;
            for (Venue venue : venueList) {
                VBox card = createVerticalVenueCard(venue);
                gridLayout.add(card, column, row);
                
                column++;
                if (column == 3) { // Menampilkan maksimal 3 kolom card ke samping sebelum turun baris baru
                    column = 0;
                    row++;
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat data katalog via VenueService: " + e.getMessage());
        }
    }

    private VBox createVerticalVenueCard(Venue venue) {
        VBox card = new VBox();
        card.setPrefWidth(280);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.04), 10, 0, 0, 4); -fx-border-color: #F1F5F9; -fx-border-radius: 16;");
        card.setPadding(new Insets(14));
        card.setSpacing(12);

        // Kotak visual placeholder gambar
        VBox imgBox = new VBox();
        imgBox.setPrefHeight(150);
        imgBox.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 12;"); 

        VBox detailsBox = new VBox();
        detailsBox.setSpacing(6);

        Label nameLabel = new Label(venue.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#1B365D"));

        // Membaca objek Enum Category bawaan model
        Label typeLabel = new Label(venue.getCategory().name() + " • Kapasitas " + venue.getCapacity() + " Pax");
        typeLabel.setFont(Font.font("System", FontWeight.MEDIUM, 11));
        typeLabel.setTextFill(Color.web("#64748B"));

        String formattedPrice = String.format("Rp %,.0f", venue.getPricePerDay());
        Label priceLabel = new Label(formattedPrice);
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        priceLabel.setTextFill(Color.web("#F27442"));
        
        Label unitLabel = new Label(" / hari");
        unitLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        unitLabel.setTextFill(Color.web("#64748B"));
        
        HBox priceRow = new HBox(priceLabel, unitLabel);
        priceRow.setAlignment(Pos.BASELINE_LEFT);

        Button btnPesan = new Button("Sewa Space");
        btnPesan.setMaxWidth(Double.MAX_VALUE);
        btnPesan.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10; -fx-cursor: hand;");
        
        // Memanfaatkan helper method isAvailable() asli dari model class Venue kamu
        if (!venue.isAvailable()) {
            btnPesan.setDisable(true);
            btnPesan.setText("Tidak Tersedia");
            btnPesan.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #94A3B8; -fx-background-radius: 10; -fx-padding: 10;");
        } else {
            // Pasangkan aksi klik untuk memicu form pemesanan (Akan dipasangkan ke Tahap 4)
            btnPesan.setOnAction(e -> {
                BookingFormDialog dialogForm = new BookingFormDialog(venue, () -> {
                    loadVenueData(null);
                });
            dialogForm.showAndWait();
            });
        }

        detailsBox.getChildren().addAll(nameLabel, typeLabel, priceRow);
        card.getChildren().addAll(imgBox, detailsBox, btnPesan);
        
        return card;
    }
}