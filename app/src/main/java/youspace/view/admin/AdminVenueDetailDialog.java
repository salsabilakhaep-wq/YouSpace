package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import youspace.view.admin.AdminVenueView.MockVenue;

public class AdminVenueDetailDialog extends Stage {

    public AdminVenueDetailDialog(Stage primaryWindow, MockVenue venueData) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #CBD5E1; -fx-border-width: 1; -fx-border-radius: 16;");
        root.setPrefWidth(600);

        // Header Dialog
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label(venueData.getNama());
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#1E293B"));
        
        VBox sp = new VBox();
        HBox.setHgrow(sp, Priority.ALWAYS);
        
        Button btnClose = new Button("✕");
        btnClose.setFont(Font.font("System", FontWeight.BOLD, 16));
        btnClose.setStyle("-fx-background-color: transparent; -fx-text-fill: #64748B; -fx-cursor: hand;");
        btnClose.setOnAction(e -> this.close());
        header.getChildren().addAll(title, sp, btnClose);
        root.getChildren().add(header);

        // Banner Visual
        VBox imgBanner = new VBox();
        imgBanner.setPrefHeight(160);
        imgBanner.setStyle("-fx-background-color: #1B365D; -fx-background-radius: 12;");
        imgBanner.setAlignment(Pos.CENTER);
        Label lblBannerText = new Label("📍 " + venueData.getTipe() + " - Kondisi: " + venueData.getKondisi());
        lblBannerText.setTextFill(Color.WHITE);
        lblBannerText.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        imgBanner.getChildren().add(lblBannerText);
        root.getChildren().add(imgBanner);

        // Deskripsi Text Area Mockup
        Label lblDescTitle = new Label("Deskripsi Ruangan:");
        lblDescTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label lblDescContent = new Label(venueData.getDeskripsi());
        lblDescContent.setWrapText(true);
        lblDescContent.setTextFill(Color.web("#475569"));
        root.getChildren().addAll(lblDescTitle, lblDescContent);

        // Footer Informasi Harga & Akses Button Edit
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER_RIGHT);
        
        Label priceLabel = new Label("Rp " + venueData.getHarga() + " / hari");
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        priceLabel.setTextFill(Color.web("#F59E0B"));
        
        VBox spacerFooter = new VBox();
        HBox.setHgrow(spacerFooter, Priority.ALWAYS);

        Button btnEdit = new Button("Edit Venue");
        btnEdit.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 10 24; -fx-cursor: hand;");
        
        btnEdit.setOnAction(e -> {
            this.close();
            // Arahkan ke form dengan membawa objek yang akan diedit
            primaryWindow.setScene(new Scene(new AdminVenueForm(true, venueData), 1050, 650));
        });

        footer.getChildren().addAll(priceLabel, spacerFooter, btnEdit);
        root.getChildren().add(footer);

        this.setScene(new Scene(root));
    }
}