package youspace.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.view.admin.AdminDashboardView;
import youspace.view.user.UserDashboardView;

public class AuthView extends HBox {

    public AuthView() {
        this.setPrefSize(1050, 650);
        this.setStyle("-fx-background-color: #F8FAFC;");

        // --- SISI KIRI: BANNER SELAMAT DATANG (Sesuai Figma) ---
        VBox leftBanner = new VBox();
        leftBanner.setPadding(new Insets(60));
        leftBanner.setSpacing(10);
        leftBanner.setAlignment(Pos.CENTER_LEFT);
        leftBanner.setStyle("-fx-background-color: #FFFFFF;"); // Background putih bersih
        leftBanner.setPrefWidth(450);

        Label brandLabel = new Label("SavEat"); // Mengikuti nama brand di mockup figma kiri atas
        brandLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        brandLabel.setTextFill(Color.web("#0F172A"));

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label welcomeTitle = new Label("Halo, Min!!");
        welcomeTitle.setFont(Font.font("System", FontWeight.BOLD, 32));
        welcomeTitle.setTextFill(Color.web("#0F172A"));

        Label welcomeDesc = new Label("Yuk, masuk dan kelola ruang terbaik untuk acara hari ini.");
        welcomeDesc.setFont(Font.font("System", FontWeight.NORMAL, 14));
        welcomeDesc.setTextFill(Color.web("#64748B"));
        welcomeDesc.setWrapText(true);

        leftBanner.getChildren().addAll(brandLabel, spacer, welcomeTitle, welcomeDesc);

        // --- SISI KANAN: FORM INPUT LOGIN ---
        VBox formContainer = new VBox();
        formContainer.setPadding(new Insets(80, 100, 80, 100));
        formContainer.setSpacing(20);
        formContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(formContainer, Priority.ALWAYS);

        Label lblEmail = new Label("Email / Nama");
        lblEmail.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Masukkan email Anda");
        txtEmail.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 10; -fx-padding: 12;");

        Label lblPassword = new Label("Kata Sandi");
        lblPassword.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan kata sandi Anda");
        txtPassword.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 10; -fx-padding: 12;");

        Hyperlink linkLupaSandi = new Hyperlink("Lupa kata sandi?");
        linkLupaSandi.setFont(Font.font("System", FontWeight.NORMAL, 12));
        linkLupaSandi.setTextFill(Color.web("#2563EB"));

        Button btnMasuk = new Button("Masuk");
        btnMasuk.setMaxWidth(Double.MAX_VALUE);
        btnMasuk.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 14; -fx-cursor: hand;");
        
        // --- LOGIC AUTHENTICATION MULTI-ROLE (USER & ADMIN) ---
        btnMasuk.setOnAction(e -> {
            String inputUser = txtEmail.getText().trim().toLowerCase();
            Stage stage = (Stage) btnMasuk.getScene().getWindow();

            // Pengecekan cerdas bypass login role admin
            if (inputUser.equals("admin") || inputUser.equals("admin@youspace.com")) {
                // Jika inputnya admin, belokkan ke Dashboard Admin
                stage.setScene(new Scene(new AdminDashboardView(), 1050, 650));
            } else {
                // Jika input biasa, arahkan ke dashboard customer/user seperti biasa
                stage.setScene(new Scene(new SidebarApp("Beranda"), 1050, 650));
            }
        });

        formContainer.getChildren().addAll(lblEmail, txtEmail, lblPassword, txtPassword, linkLupaSandi, btnMasuk);
        
        // Satukan sisi kiri dan kanan ke dalam root layout HBox
        this.getChildren().addAll(leftBanner, formContainer);
    }
}