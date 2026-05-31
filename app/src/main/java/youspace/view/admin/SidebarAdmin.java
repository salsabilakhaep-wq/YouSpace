package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.view.user.LogoutDialog;

public class SidebarAdmin extends VBox {

    private final String activeMenu;

    public SidebarAdmin(String activeMenu) {
        this.activeMenu = activeMenu;

        // --- STYLING SIDEBAR ADMIN (Navy Blue) ---
        this.setPrefWidth(240);
        this.setStyle("-fx-background-color: #1B365D;"); 
        this.setPadding(new Insets(40, 24, 30, 24));
        this.setSpacing(8);

        // 1. BRAND / LOGO
        Label brandLabel = new Label("YouSpace");
        brandLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        brandLabel.setTextFill(Color.WHITE);
        brandLabel.setPadding(new Insets(0, 0, 30, 0));
        this.getChildren().add(brandLabel);

        // 2. SUB-MENU KELOMPOK: MENU MAIN
        Label menuHeader = new Label("Menu");
        menuHeader.setFont(Font.font("System", FontWeight.BOLD, 11));
        menuHeader.setTextFill(Color.web("#64748B"));
        menuHeader.setPadding(new Insets(10, 0, 5, 0));
        this.getChildren().add(menuHeader);

        // 3. DAFTAR MENU ITEM KHAS ADMIN (Sesuai Urutan Figma Admin)
        createAdminMenuItem("Beranda");
        createAdminMenuItem("Venue");
        createAdminMenuItem("Pengguna");
        createAdminMenuItem("Booking");

        // 4. SPACER (Mendorong tombol Logout ke dasar layar)
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(spacer);

        // 5. TOMBOL LOGOUT ADMIN
        Button btnLogout = new Button("🚪  Log out");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setAlignment(Pos.CENTER_LEFT);
        btnLogout.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        btnLogout.setPadding(new Insets(12, 16, 12, 16));
        btnLogout.setCursor(javafx.scene.Cursor.HAND);
        btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-background-radius: 8;");

        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-background-color: #24426F; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;"));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-background-radius: 8;"));

        btnLogout.setOnAction(e -> {
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            LogoutDialog confirmationDialog = new LogoutDialog(currentStage);
            confirmationDialog.showAndWait();
        });

        this.getChildren().add(btnLogout);
    }

    private void createAdminMenuItem(String menuName) {
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 16, 12, 16));
        btn.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        btn.setCursor(javafx.scene.Cursor.HAND);

        // Ikon & Label Teks Menu Admin
        switch (menuName) {
            case "Beranda"  -> btn.setText("🏠  " + menuName);
            case "Venue"    -> btn.setText("🏢  " + menuName);
            case "Pengguna" -> btn.setText("👥  " + menuName);
            case "Booking"  -> btn.setText("📅  " + menuName);
            default         -> btn.setText(menuName);
        }

        // State Visual: Aktif vs Pasif
        if (menuName.equalsIgnoreCase(activeMenu)) {
            btn.setStyle("-fx-background-color: #24426F; -fx-text-fill: #F59E0B; -fx-background-radius: 8;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #24426F; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;"));
        }

        // Aksi Routing Halaman Admin (Nanti file-file view ini akan kita buat satu per satu)
        btn.setOnAction(e -> {
            if (menuName.equalsIgnoreCase(activeMenu)) return;

            Stage stage = (Stage) btn.getScene().getWindow();
            switch (menuName) {
                case "Beranda"  -> stage.setScene(new Scene(new AdminDashboardView(), 1050, 650));
                case "Venue"    -> stage.setScene(new Scene(new AdminVenueView(), 1050, 650));
                case "Pengguna" -> stage.setScene(new Scene(new AdminUserView(), 1050, 650));
                case "Booking"  -> stage.setScene(new Scene(new AdminBookingView(), 1050, 650));
            }
        });

        this.getChildren().add(btn);
    }
}