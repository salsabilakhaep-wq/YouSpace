package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import youspace.models.Venue;
import youspace.service.FavoriteService;
import youspace.service.VenueService;
import youspace.utils.SessionManager;
import youspace.view.SidebarApp;

import java.util.ArrayList;
import java.util.List;

public class FavoriteView extends HBox {

    private final FavoriteService favoriteService;
    private final VenueService venueService;
    private final GridPane gridContainer;

    public FavoriteView() {
        this.favoriteService = new FavoriteService();
        this.venueService = new VenueService();

        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);

        // Sidebar kiri dengan menu "Favorit" aktif
        SidebarApp sidebar = new SidebarApp("Favorit");

        // Konten Utama
        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setSpacing(25);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        Label titleLabel = new Label("Favorit Vanue");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1B365D"));

        // Container Scroll untuk Grid Card Venue
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F8FAFC; -fx-background-color: transparent; -fx-border-color: transparent;");

        gridContainer = new GridPane();
        gridContainer.setHgap(20);
        gridContainer.setVgap(20);
        scrollPane.setContent(gridContainer);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Load data favorit ke visual grid
        loadFavoriteData();

        mainContent.getChildren().addAll(titleLabel, scrollPane);
        this.getChildren().addAll(sidebar, mainContent);
    }

    private void loadFavoriteData() {
        gridContainer.getChildren().clear();
        int userId = SessionManager.getCurrentUser().getId();

        try {
            // Ambil semua data venue id yang difavoritkan oleh user ini
            List<Venue> favoriteVenues = favoriteService.getFavoriteVenuesByUser(userId);

            if (favoriteVenues == null || favoriteVenues.isEmpty()) {
                Label lblEmpty = new Label("Belum ada venue yang disimpan di favorit.");
                lblEmpty.setFont(Font.font("System", 14));
                lblEmpty.setTextFill(Color.web("#94A3B8"));
                gridContainer.add(lblEmpty, 0, 0);
                return;
            }

            int column = 0;
            int row = 0;

            for (Venue venue : favoriteVenues) {
                // Menggunakan kembali layout card dari VenueCardGrid agar desainnya seragam serasi
                // Kita buat method penciptaan card khusus untuk view favorit ini
                VBox card = createFavoriteCard(venue);
                gridContainer.add(card, column, row);

                column++;
                if (column == 3) { // 3 Card per baris sesuai figma grid
                    column = 0;
                    row++;
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat data favorit: " + e.getMessage());
        }
    }

    private VBox createFavoriteCard(Venue venue) {
        // Pembuatan card tiruan dari VenueCardGrid dengan penyesuaian visual figma membulat halus
        VBox card = new VBox();
        card.setPrefWidth(240);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 10, 0, 0, 5); -fx-border-color: #F1F5F9; -fx-border-radius: 16;");
        card.setPadding(new Insets(0, 0, 15, 0));

        // Placeholder Gambar Atas
        VBox imgHolder = new VBox();
        imgHolder.setPrefHeight(140);
        imgHolder.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 16 16 0 0;");
        imgHolder.setAlignment(Pos.CENTER);
        Label lblImg = new Label("📸 " + venue.getCategory());
        lblImg.setTextFill(Color.web("#94A3B8"));
        imgHolder.getChildren().add(lblImg);

        // Konten teks bawah gambar
        VBox infoBox = new VBox();
        infoBox.setPadding(new Insets(15, 15, 0, 15));
        infoBox.setSpacing(6);

        Label nameLbl = new Label(venue.getName());
        nameLbl.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLbl.setTextFill(Color.web("#1B365D"));

        Label categoryBadge = new Label(venue.getCategory().name());
        categoryBadge.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #3B82F6; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

        Label descLbl = new Label(venue.getDescription());
        descLbl.setFont(Font.font("System", 12));
        descLbl.setTextFill(Color.web("#64748B"));
        descLbl.setWrapText(true);
        descLbl.setMaxHeight(40);

        // Row harga dan tombol sewa
        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        bottomRow.setPadding(new Insets(10, 0, 0, 0));

        Label priceLbl = new Label("Rp " + String.format("%,.0f", venue.getPricePerDay()) + "/hari");
        priceLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        priceLbl.setTextFill(Color.web("#1B365D"));
        HBox.setHgrow(priceLbl, Priority.ALWAYS);

        javafx.scene.control.Button btnPesan = new javafx.scene.control.Button("Pesan Sekarang");
        btnPesan.setStyle("-fx-background-color: #F27442; -fx-text-fill: white; -fx-font-size: 10; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 10 6 10; -fx-cursor: hand;");
        btnPesan.setOnAction(e -> {
            BookingFormDialog dialog = new BookingFormDialog(venue, this::loadFavoriteData);
            dialog.showAndWait();
        });

        bottomRow.getChildren().addAll(priceLbl, btnPesan);
        infoBox.getChildren().addAll(nameLbl, categoryBadge, descLbl, bottomRow);
        card.getChildren().addAll(imgHolder, infoBox);

        return card;
    }
}