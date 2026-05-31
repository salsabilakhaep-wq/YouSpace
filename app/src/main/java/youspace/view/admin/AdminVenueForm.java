package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.view.admin.AdminVenueView.MockVenue;

public class AdminVenueForm extends HBox {

    private final boolean isEditMode;
    private final MockVenue targetVenue;

    public AdminVenueForm(boolean isEditMode, MockVenue venueData) {
        this.isEditMode = isEditMode;
        this.targetVenue = venueData;

        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);

        SidebarAdmin sidebar = new SidebarAdmin("Venue");
        this.getChildren().add(sidebar);

        VBox contentArea = new VBox();
        contentArea.setPadding(new Insets(30, 40, 30, 40));
        contentArea.setSpacing(20);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        Label titleLabel = new Label(isEditMode ? "Edit Venue" : "Tambah Venue");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1B365D"));
        contentArea.getChildren().add(titleLabel);

        VBox formCard = new VBox(20);
        formCard.setPadding(new Insets(25));
        formCard.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2E8F0; -fx-border-radius: 16;");
        VBox.setVgrow(formCard, Priority.ALWAYS);

        HBox topFormLayout = new HBox(30);
        
        // Sisi Kiri: Foto Upload Area
        VBox imageSection = new VBox(10);
        imageSection.setAlignment(Pos.CENTER);
        VBox imagePlaceholder = new VBox();
        imagePlaceholder.setPrefSize(320, 160);
        imagePlaceholder.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 8;");
        imagePlaceholder.setAlignment(Pos.CENTER);
        Label lblImg = new Label("🖼️  Gambar Ruangan Terpasang");
        lblImg.setTextFill(Color.web("#64748B"));
        imagePlaceholder.getChildren().add(lblImg);

        Button btnUpload = new Button("Unggah gambar .png, .jpg");
        btnUpload.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-text-fill: #475569; -fx-cursor: hand;");
        imageSection.getChildren().addAll(imagePlaceholder, btnUpload);

        // Sisi Kanan: Input Form Fields
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(20);
        inputGrid.setVgap(15);
        HBox.setHgrow(inputGrid, Priority.ALWAYS);

        ComboBox<String> cbTipe = new ComboBox<>();
        cbTipe.getItems().addAll("Ballroom", "Aula", "Meeting Room", "Studio");
        cbTipe.setPrefWidth(220);
        cbTipe.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 6;");

        ComboBox<String> cbKondisi = new ComboBox<>();
        cbKondisi.getItems().addAll("Baik", "Perbaikan", "Renovasi");
        cbKondisi.setPrefWidth(220);
        cbKondisi.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 6;");

        TextField txtNama = new TextField();
        txtNama.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 8;");

        TextField txtHarga = new TextField();
        txtHarga.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 8;");

        // Suntik data ke inputan jika dalam mode EDIT data
        if (isEditMode && targetVenue != null) {
            cbTipe.setValue(targetVenue.getTipe());
            cbKondisi.setValue(targetVenue.getKondisi());
            txtNama.setText(targetVenue.getNama());
            txtHarga.setText(targetVenue.getHarga());
        }

        inputGrid.add(new Label("Tipe"), 0, 0);
        inputGrid.add(cbTipe, 0, 1);
        inputGrid.add(new Label("Kondisi Ruangan"), 1, 0);
        inputGrid.add(cbKondisi, 1, 1);
        inputGrid.add(new Label("Nama Ruangan"), 0, 2);
        inputGrid.add(txtNama, 0, 3, 2, 1);
        inputGrid.add(new Label("Harga / hari (Rp)"), 0, 4);
        inputGrid.add(txtHarga, 0, 5, 2, 1);

        topFormLayout.getChildren().addAll(imageSection, inputGrid);
        formCard.getChildren().add(topFormLayout);

        // Sisi Bawah Form: Deskripsi & Fasilitas Checklist
        HBox bottomFormLayout = new HBox(30);
        VBox.setVgrow(bottomFormLayout, Priority.ALWAYS);

        VBox descBox = new VBox(8);
        HBox.setHgrow(descBox, Priority.ALWAYS);
        TextArea txtDesc = new TextArea();
        txtDesc.setWrapText(true);
        txtDesc.setStyle("-fx-control-inner-background: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 6;");
        if (isEditMode && targetVenue != null) txtDesc.setText(targetVenue.getDeskripsi());
        descBox.getChildren().addAll(new Label("Deskripsi"), txtDesc);

        VBox facilityBox = new VBox(8);
        facilityBox.setPrefWidth(350);
        GridPane facGrid = new GridPane();
        facGrid.setHgap(30); facGrid.setVgap(12); facGrid.setPadding(new Insets(10));
        facGrid.setStyle("-fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-background-color: #FFFFFF;");
        String[] facList = {"Meja & Kursi", "Catering", "Sound System", "Proyektor", "AC", "WiFi"};
        int r = 0, c = 0;
        for (String fac : facList) {
            CheckBox chk = new CheckBox(fac);
            chk.setSelected(true);
            chk.setStyle("-fx-mark-color: white; -fx-box-color: #1B365D;");
            facGrid.add(chk, c, r);
            c++; if (c > 1) { c = 0; r++; }
        }
        facilityBox.getChildren().addAll(new Label("Fasilitas"), facGrid);
        bottomFormLayout.getChildren().addAll(descBox, facilityBox);
        formCard.getChildren().add(bottomFormLayout);

        // Aksi Tombol Simpan (Logika Sinkronisasi CRUD List)
        HBox actionRow = new HBox();
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        Button btnSubmit = new Button(isEditMode ? "Simpan Perubahan" : "Simpan Venue");
        btnSubmit.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 30; -fx-cursor: hand;");
        
        btnSubmit.setOnAction(e -> {
            if (isEditMode && targetVenue != null) {
                // Skenario UPDATE
                targetVenue.setNama(txtNama.getText());
                targetVenue.setTipe(cbTipe.getValue());
                targetVenue.setKondisi(cbKondisi.getValue());
                targetVenue.setHarga(txtHarga.getText());
                targetVenue.setDeskripsi(txtDesc.getText());
            } else {
                // Skenario CREATE (Tambah Baru)
                int newNo = AdminVenueView.venueList.size() + 1;
                AdminVenueView.venueList.add(new MockVenue(
                    newNo,
                    txtNama.getText(),
                    cbTipe.getValue() != null ? cbTipe.getValue() : "Aula",
                    cbKondisi.getValue() != null ? cbKondisi.getValue() : "Baik",
                    "Tersedia",
                    txtHarga.getText(),
                    txtDesc.getText()
                ));
            }

            // Kembali ke halaman induk
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(new Scene(new AdminVenueView(), 1050, 650));
        });

        actionRow.getChildren().add(btnSubmit);
        formCard.getChildren().add(actionRow);
        contentArea.getChildren().add(formCard);
        this.getChildren().add(contentArea);
    }
}