package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.utils.SessionManager;
import youspace.view.LoginView;

public class SidebarAdmin extends VBox {

    private final Stage stage;
    private final String activeMenu;

    // Konstruktor menerima Stage untuk perpindahan halaman, dan nama menu yang sedang aktif
    public SidebarAdmin(Stage stage, String activeMenu) {
        this.stage = stage;
        this.activeMenu = activeMenu;
        
        initSidebar();
    }

    private void initSidebar() {
        this.setPrefWidth(220);
        this.setSpacing(15);
        this.setPadding(new Insets(30, 15, 30, 15));
        this.setStyle("-fx-background-color: #1A365D;"); // Biru gelap navy sesuai Figma

        Label menuTitle = new Label("Menu");
        menuTitle.setFont(Font.font("System", FontWeight.BOLD, 12));
        menuTitle.setStyle("-fx-text-fill: #A0AEC0; -fx-padding: 0 0 10 10;");
        this.getChildren().add(menuTitle);

        // Membuat tombol-tombol menu dengan mengecek apakah menu tersebut aktif
        Button btnBeranda = createSidebarButton("🏠  Beranda", "Beranda");
        Button btnVenue = createSidebarButton("🏢  Venue", "Venue");
        Button btnPengguna = createSidebarButton("👥  Pengguna", "Pengguna");
        Button btnBooking = createSidebarButton("📅  Booking", "Booking");

        // Pengaturan Aksi Navigasi Antar Halaman
        btnBeranda.setOnAction(e -> {
            if (!activeMenu.equals("Beranda")) {
                stage.setScene(new AdminDashboardView(stage).createScene());
            }
        });
        
        btnVenue.setOnAction(e -> {
            if (!activeMenu.equals("Venue")) {
                stage.setScene(new youspace.view.admin.VenueAdminView(stage).createScene());
            }
        });

        btnPengguna.setOnAction(e -> {
            UserAdminView userView = new UserAdminView(stage);
            stage.setScene(userView.createScene());
        });

        btnBooking.setOnAction(e -> {
            if (!activeMenu.equals("Booking")) {
                stage.setScene(new youspace.view.admin.BookingAdminView(stage).createScene());
            }
        });

        // Spasi Pendorong untuk menempatkan tombol Log Out paling bawah
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("🚪  Log out");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setAlignment(Pos.CENTER_LEFT);
        btnLogout.setPadding(new Insets(12, 15, 12, 15));
        btnLogout.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: #FC8181; -fx-cursor: hand;");
        
        // Ubah bagian ini di dalam file SidebarAdmin.java kamu:


        btnLogout.setOnAction(e -> {
            // 1. Membuat pop-up konfirmasi sesuai mockup Figma
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Keluar");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin keluar dari akun ini?");
            
            // 2. Menunggu respon dari Admin (Klik OK atau Cancel)
            java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            
                 // 3. Jika klik OK, bersihkan session dan pindah ke halaman login
                SessionManager.logout(); // Menggunakan fungsi logout bawaanmu
    
                LoginView loginView = new LoginView(stage);
                stage.setScene(loginView.createScene());
                stage.centerOnScreen(); // Opsional: Supaya window login otomatis di tengah layar
                }
            });

        this.getChildren().addAll(btnBeranda, btnVenue, btnPengguna, btnBooking, spacer, btnLogout);
    }

    // Helper untuk kustomisasi style tombol berdasarkan menu aktif atau hover
    private Button createSidebarButton(String text, String menuName) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 15, 12, 15));
        btn.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        
        if (activeMenu.equalsIgnoreCase(menuName)) {
            // Jika menu ini yang sedang dibuka (Aktif)
            btn.setStyle("-fx-background-color: #2B6CB0; -fx-text-fill: #F6AD55; -fx-background-radius: 8; -fx-cursor: hand;");
        } else {
            // Jika menu normal (Tidak aktif)
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #E2E8F0; -fx-background-radius: 8; -fx-cursor: hand;");
            
            // Efek Hover
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #2D3748; -fx-text-fill: #FFFFFF; -fx-background-radius: 8;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #E2E8F0; -fx-background-radius: 8;"));
        }
        return btn;
    }
}