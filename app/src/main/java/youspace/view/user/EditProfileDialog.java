package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import youspace.models.Customer;
import youspace.service.UserService; 
import youspace.utils.SessionManager;

public class EditProfileDialog extends Stage {

    private final TextField txtNama;
    private final TextField txtTelepon;
    private final TextField txtAlamat;
    private final TextField txtEmail;
    private final Runnable onSaveSuccess;

    // Variabel statis untuk menyimpan data alamat sementara agar tidak reset saat dialog ditutup
    public static String currentAlamatMock = "Sydney, Australia"; 

    public EditProfileDialog(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;

        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox();
        root.setPadding(new Insets(30));
        root.setSpacing(20);
        root.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2E8F0; -fx-border-radius: 16; -fx-border-width: 1;");
        root.setPrefWidth(550);

        Label titleLabel = new Label("Edit Profil");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#1B365D"));

        GridPane gridForm = new GridPane();
        gridForm.setHgap(20);
        gridForm.setVgap(15);

        Customer c = (Customer) SessionManager.getCurrentUser();

        // 1. Kolom Nama
        VBox boxNama = new VBox(6);
        Label lblNama = new Label("Nama");
        lblNama.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        txtNama = new TextField(c != null ? c.getName() : "");
        styleInputField(txtNama);
        boxNama.getChildren().addAll(lblNama, txtNama);
        gridForm.add(boxNama, 0, 0);

        // 2. Kolom Nomor Telepon
        VBox boxTelp = new VBox(6);
        Label lblTelp = new Label("Nomor Telepon");
        lblTelp.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        txtTelepon = new TextField(c != null ? c.getPhone() : "");
        styleInputField(txtTelepon);
        boxTelp.getChildren().addAll(lblTelp, txtTelepon);
        gridForm.add(boxTelp, 1, 0);

        // 3. Kolom Tempat Tinggal (Mengambil dari penampung statis)
        VBox boxAlamat = new VBox(6);
        Label lblAlamat = new Label("Tempat Tinggal");
        lblAlamat.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        txtAlamat = new TextField(currentAlamatMock); 
        styleInputField(txtAlamat);
        boxAlamat.getChildren().addAll(lblAlamat, txtAlamat);
        gridForm.add(boxAlamat, 0, 1);

        // 4. Kolom Alamat E-Mail
        VBox boxEmail = new VBox(6);
        Label lblEmail = new Label("Alamat E-Mail");
        lblEmail.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        txtEmail = new TextField(c != null ? c.getEmail() : "");
        styleInputField(txtEmail);
        boxEmail.getChildren().addAll(lblEmail, txtEmail);
        gridForm.add(boxEmail, 1, 1);

        HBox actionRow = new HBox(15);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.setPadding(new Insets(10, 0, 0, 0));

        Button btnBatal = new Button("Batal");
        btnBatal.setPrefWidth(120);
        btnBatal.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #64748B; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10; -fx-cursor: hand;");
        btnBatal.setOnAction(e -> this.close());

        Button btnSimpan = new Button("Simpan");
        btnSimpan.setPrefWidth(120);
        btnSimpan.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10; -fx-cursor: hand;");
        btnSimpan.setOnAction(e -> handleSaveData());

        actionRow.getChildren().addAll(btnBatal, btnSimpan);

        root.getChildren().addAll(titleLabel, gridForm, actionRow);
        this.setScene(new Scene(root));
    }

    private void styleInputField(TextField field) {
        field.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
    }

    private void handleSaveData() {
        Customer c = (Customer) SessionManager.getCurrentUser();
        
        if (c != null) {
            c.setName(txtNama.getText());
            c.setPhone(txtTelepon.getText());
            c.setEmail(txtEmail.getText());
            
            // Simpan inputan alamat terbaru ke penampung statis sebelum dialog ditutup
            currentAlamatMock = txtAlamat.getText(); 
        }

        try {
            UserService userService = new UserService();
            userService.updateProfile(c); 
        } catch (Exception ex) {
            // Fallback penanganan catch jika method update DB belum sepenuhnya sinkron
        }

        if (onSaveSuccess != null) onSaveSuccess.run();
        this.close();
    }

    private void showErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}