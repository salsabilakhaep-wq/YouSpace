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

public class AuthView extends HBox {

    private VBox leftBanner;
    private VBox rightFormContainer;
    
    // Elemen Dinamis Sisi Kiri (Banner)
    private Label welcomeTitle;
    private Label welcomeDesc;

    public AuthView() {
        this.setPrefSize(1050, 650);
        this.setStyle("-fx-background-color: #FFFFFF;"); // Background putih bersih figma

        // 1️⃣ SISI KIRI: BANNER TEKSTUAL (Tetap Di Kedua Halaman)
        leftBanner = new VBox();
        leftBanner.setPadding(new Insets(80, 60, 80, 80));
        leftBanner.setSpacing(10);
        leftBanner.setAlignment(Pos.TOP_LEFT);
        leftBanner.setPrefWidth(480);
        leftBanner.setStyle("-fx-background-color: #FFFFFF;");

        Label brandLabel = new Label("YouSpace");
        brandLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        brandLabel.setTextFill(Color.web("#0F172A"));

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        welcomeTitle = new Label();
        welcomeTitle.setFont(Font.font("System", FontWeight.BOLD, 36));
        welcomeTitle.setTextFill(Color.web("#0F172A"));
        welcomeTitle.setWrapText(true);

        welcomeDesc = new Label();
        welcomeDesc.setFont(Font.font("System", FontWeight.NORMAL, 15));
        welcomeDesc.setTextFill(Color.web("#64748B"));
        welcomeDesc.setWrapText(true);
        welcomeDesc.setMaxWidth(360);

        leftBanner.getChildren().addAll(brandLabel, spacer, welcomeTitle, welcomeDesc);

        // 2️⃣ SISI KANAN: CONTAINER FORM INTERAKTIF
        rightFormContainer = new VBox();
        HBox.setHgrow(rightFormContainer, Priority.ALWAYS);
        
        // Tampilan awal default saat dibuka: SIGN IN
        showSignInForm();

        this.getChildren().addAll(leftBanner, rightFormContainer);
    }

    /**
     * RENDER FORM SIGN IN (Sesuai image_4fd4fd.jpg)
     */
    private void showSignInForm() {
        rightFormContainer.getChildren().clear();
        rightFormContainer.setPadding(new Insets(80, 120, 80, 80));
        rightFormContainer.setSpacing(18);
        rightFormContainer.setAlignment(Pos.CENTER_LEFT);

        // Update Teks Sisi Kiri
        welcomeTitle.setText("Masuk ke YouSpace");
        welcomeDesc.setText("Yuk, masuk dan temukan ruang terbaik untuk acaramu hari ini.");

        // Field Email / Nama
        Label lblEmail = new Label("Email / Nama");
        lblEmail.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Masukkan email Anda");
        txtEmail.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 12; -fx-padding: 12; -fx-text-fill: #1E293B;");

        // Field Password
        Label lblPassword = new Label("Kata Sandi");
        lblPassword.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan kata Anda");
        txtPassword.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 12; -fx-padding: 12; -fx-text-fill: #1E293B;");

        // Label Warning/Error khusus jika password/email admin salah masukkan
        Label lblError = new Label("");
        lblError.setTextFill(Color.web("#EF4444")); // Warna merah peringatan
        lblError.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));

        Hyperlink linkLupaSandi = new Hyperlink("Lupa kata sandi?");
        linkLupaSandi.setFont(Font.font("System", FontWeight.NORMAL, 12));
        linkLupaSandi.setTextFill(Color.web("#3B82F6"));
        linkLupaSandi.setPadding(Insets.EMPTY);

        // Tombol Masuk
        Button btnMasuk = new Button("Masuk");
        btnMasuk.setPrefWidth(280);
        btnMasuk.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 12; -fx-cursor: hand;");
        
        // --- PROSES VALIDASI AKUN FIX ADMIN DENGAN PASSWORD "OOP5SUKSES" ---
        btnMasuk.setOnAction(e -> {
            String inputUser = txtEmail.getText().trim().toLowerCase();
            String inputPass = txtPassword.getText();
            Stage stage = (Stage) btnMasuk.getScene().getWindow();

            if (inputUser.equals("admin@gmail.com")) {
                // Jika email admin, wajib cek password
                if (inputPass.equals("OOP5SUKSES")) {
                    stage.setScene(new Scene(new AdminDashboardView(), 1050, 650));
                } else {
                    // Password salah, munculkan pesan error di bawah inputan
                    lblError.setText("❌ Kata sandi Admin salah!");
                }
            } else {
                // Jika selain email admin, otomatis dianggap sebagai User biasa (Bypass password untuk mock data)
                if (inputUser.isEmpty()) {
                    lblError.setText("❌ Email / Nama tidak boleh kosong!");
                } else {
                    stage.setScene(new Scene(new SidebarApp("Beranda"), 1050, 650));
                }
            }
        });

        // Teks Navigasi ke Sign Up bawah tombol
        HBox toggleLayout = new HBox(4);
        toggleLayout.setAlignment(Pos.CENTER_LEFT);
        toggleLayout.setPrefWidth(280);
        toggleLayout.setPadding(new Insets(5, 0, 0, 0));
        
        Label lblHint = new Label("Belum punya akun?");
        lblHint.setFont(Font.font("System", 12));
        lblHint.setTextFill(Color.web("#64748B"));
        
        Hyperlink linkDaftar = new Hyperlink("Daftar di sini!");
        linkDaftar.setFont(Font.font("System", FontWeight.BOLD, 12));
        linkDaftar.setTextFill(Color.web("#3B82F6"));
        linkDaftar.setPadding(Insets.EMPTY);
        
        linkDaftar.setOnAction(e -> showSignUpForm());
        toggleLayout.getChildren().addAll(lblHint, linkDaftar);

        VBox btnWrapper = new VBox(8, btnMasuk, toggleLayout);
        btnWrapper.setPadding(new Insets(15, 0, 0, 0));

        rightFormContainer.getChildren().addAll(lblEmail, txtEmail, lblPassword, txtPassword, lblError, linkLupaSandi, btnWrapper);
    }

    /**
     * RENDER FORM SIGN UP (Sesuai image_4fd562.jpg)
     */
    private void showSignUpForm() {
        rightFormContainer.getChildren().clear();
        rightFormContainer.setPadding(new Insets(40, 120, 40, 80));
        rightFormContainer.setSpacing(12);
        rightFormContainer.setAlignment(Pos.CENTER_LEFT);

        // Update Teks Sisi Kiri
        welcomeTitle.setText("Buat akun YouSpace");
        welcomeDesc.setText("Temukan ruang paling pas buat setiap momen seru kamu. Your event, your choice, YourSpace.");

        // Field Nama
        Label lblNama = new Label("Nama");
        lblNama.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        TextField txtNama = new TextField();
        txtNama.setPromptText("Masukkan nama Anda");
        txtNama.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 12; -fx-padding: 11;");

        // Field Email
        Label lblEmail = new Label("Email");
        lblEmail.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Masukkan email Anda");
        txtEmail.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 12; -fx-padding: 11;");

        // Field Password
        Label lblPassword = new Label("Kata Sandi");
        lblPassword.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan kata Anda");
        txtPassword.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 12; -fx-padding: 11;");

        // Field Nomor Telepon
        Label lblTelepon = new Label("Nomor Telpon");
        lblTelepon.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        TextField txtTelepon = new TextField();
        txtTelepon.setPromptText("Masukkan nomor telpon Anda");
        txtTelepon.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 12; -fx-padding: 11;");

        // Tombol Daftar Sekarang
        Button btnDaftar = new Button("Daftar Sekarang");
        btnDaftar.setPrefWidth(280);
        btnDaftar.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 12; -fx-cursor: hand;");
        
        btnDaftar.setOnAction(e -> {
            Stage stage = (Stage) btnDaftar.getScene().getWindow();
            stage.setScene(new Scene(new SidebarApp("Beranda"), 1050, 650));
        });

        // Teks Navigasi balik ke Sign In bawah tombol
        HBox toggleLayout = new HBox(4);
        toggleLayout.setAlignment(Pos.CENTER_LEFT);
        toggleLayout.setPrefWidth(280);
        toggleLayout.setPadding(new Insets(5, 0, 0, 0));
        
        Label lblHint = new Label("Sudah punya akun?");
        lblHint.setFont(Font.font("System", 12));
        lblHint.setTextFill(Color.web("#64748B"));
        
        Hyperlink linkMasuk = new Hyperlink("Masuk di sini!");
        linkMasuk.setFont(Font.font("System", FontWeight.BOLD, 12));
        linkMasuk.setTextFill(Color.web("#3B82F6"));
        linkMasuk.setPadding(Insets.EMPTY);
        
        linkMasuk.setOnAction(e -> showSignInForm());
        toggleLayout.getChildren().addAll(lblHint, linkMasuk);

        VBox btnWrapper = new VBox(8, btnDaftar, toggleLayout);
        btnWrapper.setPadding(new Insets(15, 0, 0, 0));

        rightFormContainer.getChildren().addAll(lblNama, txtNama, lblEmail, txtEmail, lblPassword, txtPassword, lblTelepon, txtTelepon, btnWrapper);
    }
}