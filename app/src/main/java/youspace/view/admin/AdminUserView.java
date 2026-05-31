package youspace.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AdminUserView extends HBox {

    public static ObservableList<MockUser> userList = FXCollections.observableArrayList();

    static {
        if (userList.isEmpty()) {
            userList.add(new MockUser(1, "Sutirman", "081234567890", "sutirman@gmail.com", "Aktif"));
            userList.add(new MockUser(2, "Hanni Pham", "081123456789", "sutirman@gmail.com", "Aktif"));
            userList.add(new MockUser(3, "Mas Dahri Ganteng", "085298765432", "dahri.ganteng@gmail.com", "Aktif"));
            userList.add(new MockUser(4, "Alif Syamsir", "082111222333", "alif.syamsir@unhas.ac.id", "Ditangguhkan"));
        }
    }

    private TableView<MockUser> userTable;

    public AdminUserView() {
        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);

        // Sidebar Navigasi Admin
        SidebarAdmin sidebar = new SidebarAdmin("Pengguna");
        this.getChildren().add(sidebar);

        // Konten Utama
        VBox contentArea = new VBox();
        contentArea.setPadding(new Insets(40, 40, 30, 40));
        contentArea.setSpacing(20);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // Baris Header: Judul Halaman + Search Bar (Sesuai Figma)
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Kelola Pengguna");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1E293B"));

        VBox spacer = new VBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Search Field "Cari Nama..."
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("🔍 Cari Nama...");
        txtSearch.setPrefWidth(220);
        txtSearch.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 12;");

        headerRow.getChildren().addAll(titleLabel, spacer, txtSearch);
        contentArea.getChildren().add(headerRow);

        // --- TABEL WORKER ---
        userTable = new TableView<>();
        userTable.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2E8F0; -fx-border-radius: 12;");
        VBox.setVgrow(userTable, Priority.ALWAYS);

        TableColumn<MockUser, Integer> colNo = new TableColumn<>("No.");
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colNo.setPrefWidth(50);
        colNo.setStyle("-fx-alignment: CENTER;");

        TableColumn<MockUser, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colNama.setPrefWidth(160);

        TableColumn<MockUser, String> colTelepon = new TableColumn<>("Nomor Telpon");
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        colTelepon.setPrefWidth(140);

        TableColumn<MockUser, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(200);

        // Kolom Status dengan Badge Hijau/Merah
        TableColumn<MockUser, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(110);
        colStatus.setStyle("-fx-alignment: CENTER;");
        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    badge.setFont(Font.font("System", FontWeight.BOLD, 11));
                    if (item.equalsIgnoreCase("Aktif")) {
                        badge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-padding: 3 10; -fx-background-radius: 6;");
                    } else {
                        badge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #B91C1C; -fx-padding: 3 10; -fx-background-radius: 6;");
                    }
                    setGraphic(badge);
                }
            }
        });

        // Kolom Dual Aksi (! dan 🗑️) sesuai gambar figma
        TableColumn<MockUser, Void> colAksi = new TableColumn<>("Aksi");
        colAksi.setPrefWidth(120);
        colAksi.setStyle("-fx-alignment: CENTER;");

        Callback<TableColumn<MockUser, Void>, TableCell<MockUser, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<MockUser, Void> call(final TableColumn<MockUser, Void> param) {
                return new TableCell<>() {
                    private final Button btnSuspend = new Button(" ! ");
                    private final Button btnHapus = new Button("🗑️");
                    private final HBox box = new HBox(8, btnSuspend, btnHapus);

                    {
                        box.setAlignment(Pos.CENTER);
                        
                        // Styling Tombol Suspend (Kuning/Abu lembut untuk warning)
                        btnSuspend.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #6B7280; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 3 8;");
                        btnSuspend.setOnAction(e -> {
                            MockUser selectedUser = getTableView().getItems().get(getIndex());
                            Stage mainStage = (Stage) getScene().getWindow();
                            
                            // Panggil Dialog Suspend figma
                            AdminUserActionDialog dialog = new AdminUserActionDialog(mainStage, selectedUser, "SUSPEND", () -> {
                                selectedUser.setStatus("Ditangguhkan");
                                userTable.refresh();
                            });
                            dialog.showAndWait();
                        });

                        // Styling Tombol Hapus (Merah transparan tipis figma)
                        btnHapus.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #EF4444; -fx-background-radius: 4; -fx-cursor: hand; -fx-padding: 3 8;");
                        btnHapus.setOnAction(e -> {
                            MockUser selectedUser = getTableView().getItems().get(getIndex());
                            Stage mainStage = (Stage) getScene().getWindow();
                            
                            // Panggil Dialog Hapus figma
                            AdminUserActionDialog dialog = new AdminUserActionDialog(mainStage, selectedUser, "DELETE", () -> {
                                userList.remove(selectedUser);
                                refreshRowNumbers();
                            });
                            dialog.showAndWait();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : box);
                    }
                };
            }
        };
        colAksi.setCellFactory(cellFactory);

        userTable.getColumns().addAll(colNo, colNama, colTelepon, colEmail, colStatus, colAksi);

        // --- LOGIKA FILTER PENCARIAN REAL-TIME ---
        FilteredList<MockUser> filteredData = new FilteredList<>(userList, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getNama().toLowerCase().contains(lowerCaseFilter);
            });
        });
        userTable.setItems(filteredData);

        ScrollPane scrollPane = new ScrollPane(userTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        contentArea.getChildren().add(scrollPane);
        this.getChildren().add(contentArea);
    }

    private void refreshRowNumbers() {
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setNo(i + 1);
        }
    }

    // Model data POJO menyesuaikan urutan kolom figma
    public static class MockUser {
        private int no;
        private String nama;
        private String telepon;
        private String email;
        private String status;

        public MockUser(int no, String nama, String telepon, String email, String status) {
            this.no = no;
            this.nama = nama;
            this.telepon = telepon;
            this.email = email;
            this.status = status;
        }

        public int getNo() { return no; }
        public void setNo(int no) { this.no = no; }
        public String getNama() { return nama; }
        public void setNama(String nama) { this.nama = nama; }
        public String getTelepon() { return telepon; }
        public void setTelepon(String telepon) { this.telepon = telepon; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}