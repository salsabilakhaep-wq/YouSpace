package youspace.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.models.AppUser;
import youspace.service.AuthService;
import youspace.view.user.UserDashboardView;

public class AuthView extends HBox {

    private final AuthService authService;
    private final VBox formContainer;
    private boolean isLoginMode = true; // Flag untuk pindah mode form

    // Komponen Form Input
    private TextField txtName, txtEmail, txtPhone;
    private PasswordField txtPassword;
    private Button btnAction;
    private Label lblSwitchMode;

    public AuthView() {
        this.authService = new AuthService();
        this.setPrefSize(900, 550);
        this.setStyle("-fx-background-color: #FFFFFF;");

        // SISI KIRI: DEKORATIF / BRANDING
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(400);
        leftPanel.setStyle("-fx-background-color: #1B365D;"); // Navy Blue khas YouSpace
        leftPanel.setPadding(new Insets(40));
        leftPanel.setAlignment(Pos.CENTER_LEFT);
        leftPanel.setSpacing(10);

        Label brandLogo = new Label("YouSpace");
        brandLogo.setFont(Font.font("System", FontWeight.BOLD, 32));
        brandLogo.setTextFill(Color.WHITE);

        Label brandTagline = new Label("Solusi Terbaik untuk Penyewaan Gedung dan Space Kreatif.");
        brandTagline.setFont(Font.font("System", FontWeight.NORMAL, 14));
        brandTagline.setTextFill(Color.web("#A9B9CC"));
        brandTagline.setWrapText(true);

        leftPanel.getChildren().addAll(brandLogo, brandTagline);

        // SISI KANAN: FORM CONTAINER
        formContainer = new VBox();
        formContainer.setPadding(new Insets(50, 60, 50, 60));
        formContainer.setSpacing(15);
        formContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(formContainer, Priority.ALWAYS);

        // Render form login pertama kali saat aplikasi dibuka
        showLoginForm();

        // Gabungkan panel kiri dan panel kanan ke dalam AuthView (HBox)
        this.getChildren().addAll(leftPanel, formContainer);
    }

    private void showLoginForm() {
        formContainer.getChildren().clear();
        isLoginMode = true;

        Label titleLabel = new Label("Selamat Datang Kembali");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1B365D"));

        Label subtitleLabel = new Label("Silakan masuk menggunakan akun Anda");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
        subtitleLabel.setTextFill(Color.web("#64748B"));
        subtitleLabel.setPadding(new Insets(0, 0, 15, 0));

        // Input Email
        Label lblEmail = new Label("Email");
        lblEmail.setTextFill(Color.web("#475569"));
        lblEmail.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        txtEmail = new TextField();
        styleInputField(txtEmail, "nama@email.com");

        // Input Password
        Label lblPassword = new Label("Password");
        lblPassword.setTextFill(Color.web("#475569"));
        lblPassword.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        txtPassword = new PasswordField();
        styleInputField(txtPassword, "••••••••");

        // Tombol Login
        btnAction = new Button("Masuk Sekarang");
        stylePrimaryButton(btnAction);
        btnAction.setOnAction(e -> handleAuthAction());

        // Pengubah ke Mode Register
        HBox switchBox = new HBox();
        switchBox.setAlignment(Pos.CENTER);
        switchBox.setSpacing(5);
        switchBox.setPadding(new Insets(10, 0, 0, 0));
        
        Label lblHint = new Label("Belum punya akun?");
        lblHint.setTextFill(Color.web("#64748B"));
        
        lblSwitchMode = new Label("Daftar di sini");
        lblSwitchMode.setTextFill(Color.web("#F27442"));
        lblSwitchMode.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblSwitchMode.setStyle("-fx-cursor: hand;");
        lblSwitchMode.setOnMouseClicked(e -> showRegisterForm());
        
        switchBox.getChildren().addAll(lblHint, lblSwitchMode);

        formContainer.getChildren().addAll(titleLabel, subtitleLabel, lblEmail, txtEmail, lblPassword, txtPassword, btnAction, switchBox);
    }

    private void showRegisterForm() {
        formContainer.getChildren().clear();
        isLoginMode = false;

        Label titleLabel = new Label("Buat Akun Baru");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1B365D"));

        Label subtitleLabel = new Label("Lengkapi data untuk menikmati kemudahan sewa space");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
        subtitleLabel.setTextFill(Color.web("#64748B"));
        subtitleLabel.setPadding(new Insets(0, 0, 10, 0));

        // Input Nama Lengkap
        Label lblName = new Label("Nama Lengkap");
        lblName.setTextFill(Color.web("#475569"));
        txtName = new TextField();
        styleInputField(txtName, "Masukkan nama lengkap");

        // Input Email
        Label lblEmail = new Label("Email");
        lblEmail.setTextFill(Color.web("#475569"));
        txtEmail = new TextField();
        styleInputField(txtEmail, "nama@email.com");

        // Input Nomor Telepon
        Label lblPhone = new Label("Nomor HP");
        lblPhone.setTextFill(Color.web("#475569"));
        txtPhone = new TextField();
        styleInputField(txtPhone, "08XXXXXXXXXX");

        // Input Password
        Label lblPassword = new Label("Password");
        lblPassword.setTextFill(Color.web("#475569"));
        txtPassword = new PasswordField();
        styleInputField(txtPassword, "Minimal 6 karakter");

        // Tombol Register
        btnAction = new Button("Daftar Akun");
        stylePrimaryButton(btnAction);
        btnAction.setOnAction(e -> handleAuthAction());

        // Pengubah ke Mode Login
        HBox switchBox = new HBox();
        switchBox.setAlignment(Pos.CENTER);
        switchBox.setSpacing(5);
        
        Label lblHint = new Label("Sudah memiliki akun?");
        lblHint.setTextFill(Color.web("#64748B"));
        
        lblSwitchMode = new Label("Masuk di sini");
        lblSwitchMode.setTextFill(Color.web("#F27442"));
        lblSwitchMode.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblSwitchMode.setStyle("-fx-cursor: hand;");
        lblSwitchMode.setOnMouseClicked(e -> showLoginForm());
        
        switchBox.getChildren().addAll(lblHint, lblSwitchMode);

        formContainer.getChildren().addAll(titleLabel, subtitleLabel, lblName, txtName, lblEmail, txtEmail, lblPhone, txtPhone, lblPassword, txtPassword, btnAction, switchBox);
    }

    private void handleAuthAction() {
        try {
            if (isLoginMode) {
                // Panggil backend login via AuthService
                AppUser user = authService.login(txtEmail.getText(), txtPassword.getText());
                showAlert(Alert.AlertType.INFORMATION, "Login Sukses", "Selamat Datang, " + user.getName() + "!");
                
                // Jika sukses login, arahkan langsung ke halaman utama Dashboard User
                Stage currentStage = (Stage) this.getScene().getWindow();
                Scene dashboardScene = new Scene(new UserDashboardView(), 1050, 650);
                currentStage.setScene(dashboardScene);
            } else {
                // Panggil backend register via AuthService
                boolean success = authService.register(
                    txtName.getText(), 
                    txtEmail.getText(), 
                    txtPassword.getText(), 
                    txtPhone.getText()
                );
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Registrasi Sukses", "Akun berhasil dibuat. Silakan login.");
                    showLoginForm(); // Kembalikan ke panel login
                }
            }
        } catch (IllegalArgumentException ex) {
            // Menangkap pesan kesalahan validasi dari AuthService / ValidationUtil
            showAlert(Alert.AlertType.ERROR, "Autentikasi Gagal", ex.getMessage());
        }
    }

    private void styleInputField(TextField field, String placeholder) {
        field.setPromptText(placeholder);
        field.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");
        field.setMaxWidth(Double.MAX_VALUE);
    }

    private void stylePrimaryButton(Button btn) {
        btn.setStyle("-fx-background-color: #F27442; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12; -fx-cursor: hand;");
        btn.setMaxWidth(Double.MAX_VALUE);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}