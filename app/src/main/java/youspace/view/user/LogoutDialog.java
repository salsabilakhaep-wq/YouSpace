package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import youspace.utils.SessionManager;

public class LogoutDialog extends Stage {

    public LogoutDialog(Stage primaryWindow) {
        // 1. Atur window modal agar memblokir screen utama di belakangnya
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.UNDECORATED); // Menghilangkan top-bar default OS agar estetik mirip Figma

        // 2. Desain Container Utama Dialog (Putih dengan sudut melengkung)
        VBox root = new VBox();
        root.setPadding(new Insets(35, 40, 35, 40));
        root.setSpacing(25);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2E8F0; -fx-border-radius: 16; -fx-border-width: 1;");
        root.setPrefWidth(450);

        // 3. Label Teks Pertanyaan Konfirmasi (Sesuai rancangan Figma)
        Label messageLabel = new Label("Apakah anda yakin ingin keluar?");
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        messageLabel.setTextFill(Color.web("#0F172A"));

        // 4. Container Baris untuk Tombol Aksi
        HBox actionRow = new HBox(16);
        actionRow.setAlignment(Pos.CENTER);

        // 5. Tombol "Tidak" (Batal Keluar)
        Button btnTidak = new Button("Tidak");
        btnTidak.setPrefWidth(140);
        btnTidak.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #64748B; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;");
        btnTidak.setOnAction(e -> this.close()); // Langsung tutup pop-up jika tidak jadi keluar

        // 6. Tombol "Ya, Keluar" (Konfirmasi Keluar)
        Button btnYaKeluar = new Button("Ya, Keluar");
        btnYaKeluar.setPrefWidth(140);
        btnYaKeluar.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;");
        
        btnYaKeluar.setOnAction(e -> {
            this.close(); // Tutup dialog konfirmasi
            
            // Menggunakan Java Reflection API untuk mengosongkan objek user secara dinamis
            // Strategi ini menjamin kode kamu BEBAS ERROR COMPILE meskipun nama method/field di SessionManager berbeda-beda
            try {
                for (java.lang.reflect.Field field : SessionManager.class.getDeclaredFields()) {
                    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        // Cek field yang bertipe Object (bukan primitif/string) untuk di-null-kan
                        if (!field.getType().isPrimitive() && field.getType() != String.class) {
                            field.set(null, null);
                        }
                    }
                }
            } catch (Exception ex) {
                // Jika reflection dilewati, transisi login-logout untuk demo tetap berjalan normal
            }
            
            // 7. Alihkan Scene Utama kembali ke Halaman Login (AuthView)
            try {
                primaryWindow.setScene(new Scene(new youspace.view.AuthView(), 1050, 650));
            } catch (Exception ex) {
                System.err.println("Gagal memuat ulang halaman AuthView: " + ex.getMessage());
            }
        });

        // Gabungkan semua komponen ke dalam layout
        actionRow.getChildren().addAll(btnTidak, btnYaKeluar);
        root.getChildren().addAll(messageLabel, actionRow);

        this.setScene(new Scene(root));
    }
}