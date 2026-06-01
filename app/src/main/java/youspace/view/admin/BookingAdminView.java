package youspace.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import youspace.dao.UserDAO;
import youspace.dao.VenueDAO;
import youspace.enums.BookingStatus;
import youspace.models.AppUser;
import youspace.models.Booking;
import youspace.models.Venue;
import youspace.service.BookingService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingAdminView {

    private final Stage stage;
    private final BookingService bookingService;
    private final UserDAO userDAO;
    private final VenueDAO venueDAO;
    
    private TableView<Booking> tableView;
    private TextField tfSearch;

    public BookingAdminView(Stage stage) {
        this.stage = stage;
        this.bookingService = new BookingService();
        this.userDAO = new UserDAO();   // Digunakan untuk lookup nama user berdasarkan user_id
        this.venueDAO = new VenueDAO(); // Digunakan untuk lookup nama venue berdasarkan venue_id
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8F9FA;");

        // Memanggil sidebar reusable milikmu
        SidebarAdmin sidebar = new SidebarAdmin(stage, "Booking");
        root.setLeft(sidebar);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(35, 40, 35, 40));

        // Header & Search Bar
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label txtHeader = new Label("Kelola Booking");
        txtHeader.setFont(Font.font("System", FontWeight.BOLD, 26));
        txtHeader.setStyle("-fx-text-fill: #1A202C;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        tfSearch = new TextField();
        tfSearch.setPromptText("🔍 Cari Nama User / Acara...");
        tfSearch.setPrefWidth(240);
        tfSearch.setStyle("-fx-background-radius: 10; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-padding: 8 12;");
        
        // Fitur pencarian otomatis ketika mengetik text
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            refreshTable(newValue);
        });

        headerRow.getChildren().addAll(txtHeader, spacer, tfSearch);
        mainContent.getChildren().add(headerRow);

        // Setup Tabel Reservasi
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-background-radius: 10; -fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");

        TableColumn<Booking, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        // Kolom Relasional: Mengubah ID User menjadi Nama User asli
        TableColumn<Booking, String> colUser = new TableColumn<>("Nama Pemesan");
        colUser.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Booking b = getTableRow().getItem();
                    AppUser user = userDAO.findById(b.getUserId());
                    setText(user != null ? user.getName() : "User #" + b.getUserId());
                }
            }
        });

        // Kolom Relasional: Mengubah ID Venue menjadi Nama Venue asli
        TableColumn<Booking, String> colVenue = new TableColumn<>("Venue");
        colVenue.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Booking b = getTableRow().getItem();
                    Venue venue = venueDAO.findById(b.getVenueId());
                    setText(venue != null ? venue.getName() : "Venue #" + b.getVenueId());
                }
            }
        });

        TableColumn<Booking, String> colEvent = new TableColumn<>("Nama Acara");
        colEvent.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        TableColumn<Booking, String> colTanggal = new TableColumn<>("Tanggal");
        colTanggal.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Booking b = getTableRow().getItem();
                    setText(b.getStartDate() + " s/d " + b.getEndDate());
                }
            }
        });

        // Format Tampilan Harga Ke Rupiah (Rp)
        TableColumn<Booking, Double> colHarga = new TableColumn<>("Total Harga");
        colHarga.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colHarga.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                    setText(rupiah.format(item).replace(",00", ""));
                }
            }
        });

        // Tampilan Badge Status Berwarna (APPROVED, COMPLETED, WAITING_PAYMENT)
        TableColumn<Booking, BookingStatus> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(param -> new TableCell<>() {
            private final Label badge = new Label();
            @Override
            protected void updateItem(BookingStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    badge.setText(item.name());
                    if (item == BookingStatus.COMPLETED) {
                        badge.setStyle("-fx-background-color: #C6F6D5; -fx-text-fill: #22543D; -fx-padding: 4 10; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 11;");
                    } else if (item == BookingStatus.APPROVED) {
                        badge.setStyle("-fx-background-color: #EBF8FF; -fx-text-fill: #2B6CB0; -fx-padding: 4 10; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 11;");
                    } else if (item == BookingStatus.REJECTED) {
                        badge.setStyle("-fx-background-color: #FED7D7; -fx-text-fill: #C53030; -fx-padding: 4 10; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 11;");
                    } else {
                        badge.setStyle("-fx-background-color: #FEFCBF; -fx-text-fill: #744210; -fx-padding: 4 10; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 11;");
                    }
                    setGraphic(badge);
                }
            }
        });

        // Kolom Aksi Tombol Selesai
        TableColumn<Booking, Void> colAksi = new TableColumn<>("Aksi");
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnSelesai = new Button("✓ Selesai");
            {
                btnSelesai.setStyle("-fx-background-color: #1A365D; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 11;");
                btnSelesai.setOnAction(e -> {
                    Booking selectedBooking = getTableView().getItems().get(getIndex());
                    
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Tandai pesanan acara '" + selectedBooking.getEventName() + "' ini sebagai Selesai?");
                    
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Memanggil metode completeBooking asli milikmu!
                        boolean sukses = bookingService.completeBooking(selectedBooking.getId());
                        if (sukses) {
                            refreshTable(tfSearch.getText());
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Booking currentBooking = getTableView().getItems().get(getIndex());
                    // Jika status sudah COMPLETED atau REJECTED, sembunyikan tombol selesainya
                    if (currentBooking.getStatus() == BookingStatus.COMPLETED || currentBooking.getStatus() == BookingStatus.REJECTED) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnSelesai);
                    }
                }
            }
        });
        colAksi.setPrefWidth(100);

        tableView.getColumns().addAll(colId, colUser, colVenue, colEvent, colTanggal, colHarga, colStatus, colAksi);
        mainContent.getChildren().add(tableView);

        refreshTable("");
        root.setCenter(mainContent);
        return new Scene(root, 960, 650);
    }

    // Fungsi pemrosesan filter pencarian data booking secara dinamis
    private void refreshTable(String keyword) {
        tableView.getItems().clear();
        List<Booking> allBookings = bookingService.getAllBookings();

        if (keyword == null || keyword.trim().isEmpty()) {
            tableView.getItems().addAll(allBookings);
        } else {
            String cleanKeyword = keyword.toLowerCase().trim();
            
            // Menyaring berdasarkan nama event acara, atau melakukan lookup nama user
            List<Booking> filteredList = allBookings.stream().filter(b -> {
                AppUser user = userDAO.findById(b.getUserId());
                String namaUser = (user != null) ? user.getName().toLowerCase() : "";
                String namaEvent = (b.getEventName() != null) ? b.getEventName().toLowerCase() : "";
                
                return namaUser.contains(cleanKeyword) || namaEvent.contains(cleanKeyword);
            }).collect(Collectors.toList());

            tableView.getItems().addAll(filteredList);
        }
    }
}