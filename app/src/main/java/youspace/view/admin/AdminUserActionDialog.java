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

public class AdminUserActionDialog extends Stage {

    public interface OnConfirmListener {
        void onConfirm();
    }

    public AdminUserActionDialog(Stage owner, AdminUserView.MockUser user, String actionType, OnConfirmListener listener) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(owner);
        this.initStyle(StageStyle.UNDECORATED); // Tanpa top bar windows agar clean

        VBox container = new VBox(24);
        container.setPadding(new Insets(35, 40, 35, 40));
        container.setAlignment(Pos.CENTER);
        container.setPrefWidth(460);
        // Desain melengkung putih bersih figma
        container.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2E8F0; -fx-border-radius: 16; -fx-border-width: 1;");

        // Set tulisan text mengikuti tombol mana yang ditekan admin
        String promptText = actionType.equals("DELETE") 
                ? "Apakah and yakin ingin menghapus " + user.getNama() + "?"
                : "Apakah and yakin ingin menangguhkan " + user.getNama() + "?";

        Label lblMessage = new Label(promptText);
        lblMessage.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblMessage.setWrapText(true);
        lblMessage.setAlignment(Pos.CENTER);
        lblMessage.setStyle("-fx-text-fill: #1E293B;");

        // Baris aksi pilihan tombol kiri-kanan figma
        HBox buttonRow = new HBox(16);
        buttonRow.setAlignment(Pos.CENTER);

        Button btnCancel = new Button("Tidak");
        btnCancel.setPrefWidth(140);
        btnCancel.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10; -fx-cursor: hand;");
        btnCancel.setOnAction(e -> this.close());

        Button btnConfirm = new Button(actionType.equals("DELETE") ? "Ya, Hapus" : "Ya, Tangguhkan");
        btnConfirm.setPrefWidth(140);
        // Biru gelap YouSpace pradana
        btnConfirm.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10; -fx-cursor: hand;");
        
        btnConfirm.setOnAction(e -> {
            if (listener != null) {
                listener.onConfirm();
            }
            this.close();
        });

        buttonRow.getChildren().addAll(btnCancel, btnConfirm);
        container.getChildren().addAll(lblMessage, buttonRow);

        this.setScene(new Scene(container));
        
        // Memposisikan pop-up presisi tepat di tengah-tengah window aplikasi utama
        this.setOnShowing(e -> {
            this.setX(owner.getX() + (owner.getWidth() - this.getWidth()) / 2);
            this.setY(owner.getY() + (owner.getHeight() - this.getHeight()) / 2);
        });
    }
}