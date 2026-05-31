package youspace.view;

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
import youspace.view.user.LogoutDialog; // Import Dialog yang baru kita buat

public class SidebarApp extends VBox {

    private final String activeMenu;

    public SidebarApp(String activeMenu) {
        this.activeMenu = activeMenu;

        // --- STYLING SIDEBAR (Navy Blue khas Figma YouSpace) ---
        this.setPrefWidth(240);
        this.setStyle("-fx-background-color: #1B365D;"); // Warna dasar navy figma
        this.setPadding(new Insets(40, 24, 30, 24));
        this.setSpacing(8);

        // 1. HEADER BRAND / LOGO
        Label brandLabel = new Label("YouSpace");
        brandLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        brandLabel.setTextFill(Color.WHITE);
        brandLabel.setPadding(new Insets(0, 0, 30, 0));
        this.getChildren().add(brandLabel);

        // 2. SUB-MENU KELOMPOK: MENU MAIN
        createSubMenuHeader("Menu");
        createMenuItem("Beranda");
        createMenuItem("Venue");
        createMenuItem("Favorit");

        // 给 MENU UTAMA SPACING SEBELUM MASUK KELOMPOK BOOKING
        VBox.setMargin(this.getChildren().get(this.getChildren().size() - 1), new Insets(0, 0, 15, 0));

        // 3. SUB-MENU KELOMPOK: BOOKING & PROFILE
        createSubMenuHeader("Booking");
        createMenuItem("Pesanan");
        createMenuItem("Profile");

        // 4. SPACER OTOMATIS (Mendorong tombol Logout ke dasar layar sesuai Figma)
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(spacer);

        // 5. TOMBOL LOGOUT (Paling Bawah)
        Button btnLogout = new Button("🚪  Log out");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setAlignment(Pos.CENTER_LEFT);
        btnLogout.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        btnLogout.setPadding(new Insets(12, 16, 12, 16));
        btnLogout.setCursor(javafx.scene.Cursor.HAND);
        
        // Style khusus tombol Logout (Transparan dengan text putih/abu tipis)
        btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-background-radius: 8;");

        // Efek Hover Tombol Logout
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-background-color: #24426F; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;"));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-background-radius: 8;"));

        // AKSI KLIK TOMBOL LOGOUT PADA SIDEBAR
        btnLogout.setOnAction(e -> {
            // Mengambil window stage utama aplikasi yang sedang aktif berjalan
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            
            // Tampilkan pop-up dialog konfirmasi moderen Figma
            LogoutDialog confirmationDialog = new LogoutDialog(currentStage);
            confirmationDialog.showAndWait();
        });

        this.getChildren().add(btnLogout);
    }

    private void createSubMenuHeader(String title) {
        Label header = new Label(title);
        header.setFont(Font.font("System", FontWeight.BOLD, 11));
        header.setTextFill(Color.web("#64748B")); // Warna abu-abu muted text figma
        header.setPadding(new Insets(10, 0, 5, 0));
        this.getChildren().add(header);
    }

    private void createMenuItem(String menuName) {
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 16, 12, 16));
        btn.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        btn.setCursor(javafx.scene.Cursor.HAND);

        // Pemetaan Label & Ikon Teks Menu
        switch (menuName) {
            case "Beranda" -> btn.setText("🏠  " + menuName);
            case "Venue"   -> btn.setText("🏢  " + menuName);
            case "Favorit" -> btn.setText("🔖  " + menuName);
            case "Pesanan" -> btn.setText("📅  " + menuName);
            case "Profile" -> btn.setText("👤  " + menuName);
            default        -> btn.setText(menuName);
        }

        // --- Logika Visual Tab Aktif vs Tidak Aktif ---
        if (menuName.equalsIgnoreCase(activeMenu)) {
            // Menu Sedang Aktif (Background Highlight Sedikit Terang + Teks Emas/Putih Terang)
            btn.setStyle("-fx-background-color: #24426F; -fx-text-fill: #F59E0B; -fx-background-radius: 8;");
        } else {
            // Menu Pasif
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;");
            
            // Efek Hover untuk Menu Pasif
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #24426F; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;"));
        }

        // --- Logika Pengalihan Halaman (Routing) Saat Diklik ---
        btn.setOnAction(e -> {
            if (menuName.equalsIgnoreCase(activeMenu)) return; // Blokir jika klik menu yang sama

            Stage stage = (Stage) btn.getScene().getWindow();
            switch (menuName) {
                case "Beranda" -> stage.setScene(new Scene(new youspace.view.user.UserDashboardView(), 1050, 650));
                case "Venue"   -> stage.setScene(new Scene(new youspace.view.user.VenueCardGrid(), 1050, 650));
                case "Favorit" -> stage.setScene(new Scene(new youspace.view.user.FavoriteView(), 1050, 650));
                case "Pesanan" -> stage.setScene(new Scene(new youspace.view.user.PesananView(), 1050, 650));
                case "Profile" -> stage.setScene(new Scene(new youspace.view.user.ProfileView(), 1050, 650));
            }
        });

        this.getChildren().add(btn);
    }
}