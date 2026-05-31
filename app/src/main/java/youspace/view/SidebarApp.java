package youspace.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.utils.SessionManager;
import youspace.view.user.UserDashboardView;
import youspace.view.user.VenueCardGrid;

public class SidebarApp extends VBox {

    public SidebarApp(String activeMenu) {
        this.setPrefWidth(220);
        this.setStyle("-fx-background-color: #1B365D;"); // Navy khas YouSpace
        this.setPadding(new Insets(30, 15, 30, 15));
        this.setSpacing(10);

        // Logo Aplikasi di Sidebar
        Label brandLogo = new Label("YouSpace");
        brandLogo.setFont(Font.font("System", FontWeight.BOLD, 22));
        brandLogo.setTextFill(Color.WHITE);
        brandLogo.setPadding(new Insets(0, 0, 30, 10));
        this.getChildren().add(brandLogo);

        // Menu items
        createMenuItem("Beranda", activeMenu);
        createMenuItem("Venue", activeMenu);
        createMenuItem("Favorit", activeMenu);
        createMenuItem("Pesanan", activeMenu);
        createMenuItem("Profile", activeMenu);
        
        // Spacer Bawah
        VBox bottomSpacer = new VBox();
        VBox.setVgrow(bottomSpacer, javafx.scene.layout.Priority.ALWAYS);
        this.getChildren().add(bottomSpacer);

        // Tombol Logout
        Button btnLogout = new Button("🚪 Keluar");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setAlignment(Pos.CENTER_LEFT);
        btnLogout.setPadding(new Insets(10, 15, 10, 15));
        btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: #E2E8F0; -fx-cursor: hand; -fx-font-weight: bold;");
        btnLogout.setOnAction(e -> {
            SessionManager.logout();
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(new Scene(new AuthView(), 900, 550));
        });
        this.getChildren().add(btnLogout);
    }

    private void createMenuItem(String menuName, String activeMenu) {
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 15, 12, 15));
        
        // Menentukan icon penanda text
        switch (menuName) {
            case "Beranda" -> btn.setText("🏠  " + menuName);
            case "Venue" -> btn.setText("🏢  " + menuName);
            case "Favorit" -> btn.setText("🔖  " + menuName);
            case "Pesanan" -> btn.setText("📅  " + menuName);
            case "Profile" -> btn.setText("👤  " + menuName);
            default -> btn.setText(menuName);
        }

        // Styling kondisi aktif / tidak aktif
        if (menuName.equalsIgnoreCase(activeMenu)) {
            btn.setStyle("-fx-background-color: #F27442; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #A9B9CC; -fx-background-radius: 8; -fx-cursor: hand;");
            
            // Efek hover sederhana
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #244473; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #A9B9CC; -fx-background-radius: 8; -fx-cursor: hand;"));
        }

        // Event Klik Navigasi Antar Halaman
        btn.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            if (menuName.equals("Beranda") && !activeMenu.equals("Beranda")) {
                stage.setScene(new Scene(new UserDashboardView(), 1050, 650));
            } else if (menuName.equals("Venue") && !activeMenu.equals("Venue")) {
                stage.setScene(new Scene(new VenueCardGrid(), 1050, 650));
            } else if (menuName.equals("Favorit") && !activeMenu.equals("Favorit")) {
                stage.setScene(new Scene(new youspace.view.user.FavoriteView(), 1050, 650));
            } else if (menuName.equals("Pesanan") && !activeMenu.equals("Pesanan")) {
                stage.setScene(new Scene(new youspace.view.user.PesananView(), 1050, 650));
            } else if (menuName.equals("Profile") && !activeMenu.equals("Profile")) {
                stage.setScene(new Scene(new youspace.view.user.ProfileView(), 1050, 650));
            }
        });

        this.getChildren().add(btn);
    }
}