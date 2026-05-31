package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminLogoutDialog extends Stage {

    private boolean confirmLogout = false;

    public AdminLogoutDialog(Stage owner) {
        // Setup modality agar user fokus ke pop-up (tidak bisa klik window belakang)
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(owner);
        this.initStyle(StageStyle.UNDECORATED); // Menghilangkan top bar Windows/Mac agar bersih mirip figma

        // Container utama pop-up
        VBox container = new VBox(24);
        container.setPadding(new Insets(35, 40, 35, 40));
        container.setAlignment(Pos.CENTER);
        container.setPrefWidth(420);
        // Desain rounded corner putih bersih sesuai komponen figma
        container.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2E8F0; -fx-border-radius: 16; -fx-border-width: 1;");

        // Label teks konfirmasi
        Label lblMessage = new Label("Apakah anda yakin ingin keluar?");
        lblMessage.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblMessage.setStyle("-fx-text-fill: #1E293B;");

        // Baris tombol aksi (Tidak & Ya, Keluar)
        HBox buttonRow = new HBox(16);
        buttonRow.setAlignment(Pos.CENTER);

        // Tombol "Tidak" (Abu-abu lembut)
        Button btnCancel = new Button("Tidak");
        btnCancel.setPrefWidth(130);
        btnCancel.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10; -fx-cursor: hand;");
        btnCancel.setOnAction(e -> {
            confirmLogout = false;
            this.close();
        });

        // Tombol "Ya, Keluar" (Biru Gelap YouSpace)
        Button btnConfirm = new Button("Ya, Keluar");
        btnConfirm.setPrefWidth(130);
        btnConfirm.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10; -fx-cursor: hand;");
        btnConfirm.setOnAction(e -> {
            confirmLogout = true;
            this.close();
        });

        buttonRow.getChildren().addAll(btnCancel, btnConfirm);
        container.getChildren().addAll(lblMessage, buttonRow);

        this.setScene(new Scene(container));

        // Logika agar pop-up otomatis muncul tepat di tengah-tengah aplikasi utama
        this.setOnShowing(e -> {
            this.setX(owner.getX() + (owner.getWidth() - this.getWidth()) / 2);
            this.setY(owner.getY() + (owner.getHeight() - this.getHeight()) / 2);
        });
    }

    public boolean isConfirmLogout() {
        return confirmLogout;
    }
}