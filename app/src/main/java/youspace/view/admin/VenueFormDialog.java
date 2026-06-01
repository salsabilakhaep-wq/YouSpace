package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import youspace.enums.VenueCategory;
import youspace.enums.VenueStatus;
import youspace.models.Venue;
import youspace.service.VenueService;

public class VenueFormDialog extends Stage {

    private final Venue existingVenue;
    private final VenueService venueService;

    private ComboBox<VenueCategory> cbTipe;
    private ComboBox<VenueStatus> cbKondisi;
    private TextField tfNama, tfHarga, tfGambar, tfKapasitas;
    private TextArea taDeskripsi;

    public VenueFormDialog(Stage owner, Venue existingVenue) {
        this.existingVenue = existingVenue;
        this.venueService = new VenueService();

        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        setTitle(existingVenue == null ? "Tambah Venue" : "Edit Venue");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #F8F9FA;");

        Label txtTitle = new Label(existingVenue == null ? "Tambah Venue Baru" : "Edit Detail Venue");
        txtTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        layout.getChildren().add(txtTitle);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);

        // Kategori (Enum)
        grid.add(new Label("Tipe Ruangan"), 0, 0);
        cbTipe = new ComboBox<>();
        cbTipe.getItems().addAll(VenueCategory.values()); // Otomatis mengambil AULA, BALLROOM, dll.
        cbTipe.setMaxWidth(Double.MAX_VALUE);
        grid.add(cbTipe, 0, 1);

        // Kondisi / Status (Enum)
        grid.add(new Label("Kondisi Ruangan"), 1, 0);
        cbKondisi = new ComboBox<>();
        cbKondisi.getItems().addAll(VenueStatus.values()); // AVAILABLE, UNAVAILABLE
        cbKondisi.setMaxWidth(Double.MAX_VALUE);
        grid.add(cbKondisi, 1, 1);

        // Nama Ruangan
        grid.add(new Label("Nama Ruangan"), 0, 2, 2, 1);
        tfNama = new TextField();
        grid.add(tfNama, 0, 3, 2, 1);

        // Harga & Kapasitas
        grid.add(new Label("Harga/Hari (Rp)"), 0, 4);
        tfHarga = new TextField();
        grid.add(tfHarga, 0, 5);

        grid.add(new Label("Kapasitas (Orang)"), 1, 4);
        tfKapasitas = new TextField();
        grid.add(tfKapasitas, 1, 5);

        // Gambar
        grid.add(new Label("Unggah Gambar (.png, .jpg)"), 0, 6, 2, 1);
        tfGambar = new TextField();
        grid.add(tfGambar, 0, 7, 2, 1);

        // Deskripsi
        grid.add(new Label("Deskripsi & Fasilitas Ruangan"), 0, 8, 2, 1);
        taDeskripsi = new TextArea();
        taDeskripsi.setPrefHeight(80);
        taDeskripsi.setWrapText(true);
        grid.add(taDeskripsi, 0, 9, 2, 1);

        layout.getChildren().add(grid);

        // Jika Edit, isi form dengan data lama
        if (existingVenue != null) {
            cbTipe.setValue(existingVenue.getCategory());
            cbKondisi.setValue(existingVenue.getStatus());
            tfNama.setText(existingVenue.getName());
            tfHarga.setText(String.valueOf((int) existingVenue.getPricePerDay()));
            tfKapasitas.setText(String.valueOf(existingVenue.getCapacity()));
            tfGambar.setText(existingVenue.getImagePath());
            taDeskripsi.setText(existingVenue.getDescription());
        } else {
            cbKondisi.setValue(VenueStatus.AVAILABLE); // Default tambah baru
        }

        // Tombol Aksi
        HBox actionRow = new HBox(12);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnBatal = new Button("Batal");
        btnBatal.setOnAction(e -> close());
        
        Button btnSimpan = new Button(existingVenue == null ? "Simpan Venue" : "Simpan Perubahan");
        btnSimpan.setStyle("-fx-background-color: #1A365D; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSimpan.setOnAction(e -> handleSave());

        actionRow.getChildren().addAll(btnBatal, btnSimpan);
        layout.getChildren().add(actionRow);

        setScene(new Scene(layout, 460, 580));
    }

    private void handleSave() {
        if (tfNama.getText().isEmpty() || tfHarga.getText().isEmpty() || cbTipe.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Mohon lengkapi data form!");
            alert.showAndWait();
            return;
        }

        try {
            String name = tfNama.getText();
            String desc = taDeskripsi.getText();
            VenueCategory category = cbTipe.getValue();
            int capacity = Integer.parseInt(tfKapasitas.getText());
            double price = Double.parseDouble(tfHarga.getText());
            String img = tfGambar.getText();

            boolean sukses;

            if (existingVenue == null) {
                // Gunakan fungsi addVenue milikmu
                sukses = venueService.addVenue(name, desc, category, capacity, price, img);
            } else {
                // Set data baru ke objek existingVenue lalu panggil updateVenue milikmu
                existingVenue.setName(name);
                existingVenue.setDescription(desc);
                existingVenue.setCategory(category);
                existingVenue.setCapacity(capacity);
                existingVenue.setPricePerDay(price);
                existingVenue.setImagePath(img);
                existingVenue.setStatus(cbKondisi.getValue());

                sukses = venueService.updateVenue(existingVenue);
            }

            if (sukses) {
                close();
            } else {
                throw new Exception("Gagal mengeksekusi ke database.");
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menyimpan: " + ex.getMessage());
            alert.showAndWait();
        }
    }
}