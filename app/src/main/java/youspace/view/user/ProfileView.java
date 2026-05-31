package youspace.view.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import youspace.models.Customer; 
import youspace.utils.SessionManager;
import youspace.view.SidebarApp;

public class ProfileView extends HBox {

    private Label lblNamaLengkap;
    private Label lblUsernameBawah;
    private Label lblTempatTinggal;
    private Label lblEmail;
    private Label lblTelepon;

    public ProfileView() {
        this.setStyle("-fx-background-color: #F8FAFC;");
        this.setPrefSize(1050, 650);

        SidebarApp sidebar = new SidebarApp("Profile");

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setSpacing(30);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        Label titleLabel = new Label("Profil Pengguna");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1B365D"));

        HBox profileCard = new HBox();
        profileCard.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 24; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.02), 15, 0, 0, 8); -fx-border-color: #F1F5F9; -fx-border-radius: 24;");
        profileCard.setPadding(new Insets(40));
        profileCard.setSpacing(40);
        profileCard.setAlignment(Pos.CENTER_LEFT);
        profileCard.setPrefWidth(800);

        VBox leftSection = new VBox();
        leftSection.setSpacing(12);
        leftSection.setAlignment(Pos.CENTER);

        Circle avatarCircle = new Circle(70);
        avatarCircle.setFill(Color.web("#DBEAFE"));
        
        Label lblAvatarPlaceholder = new Label("🦆"); 
        lblAvatarPlaceholder.setFont(Font.font("System", 48));
        
        javafx.scene.layout.StackPane avatarPane = new javafx.scene.layout.StackPane(avatarCircle, lblAvatarPlaceholder);

        lblUsernameBawah = new Label();
        lblUsernameBawah.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblUsernameBawah.setTextFill(Color.web("#1B365D"));

        leftSection.getChildren().addAll(avatarPane, lblUsernameBawah);

        VBox rightSection = new VBox();
        rightSection.setSpacing(16);
        rightSection.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(rightSection, Priority.ALWAYS);

        lblNamaLengkap = new Label();
        lblNamaLengkap.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblNamaLengkap.setTextFill(Color.web("#1B365D"));

        lblTempatTinggal = new Label();
        lblTempatTinggal.setFont(Font.font("System", 14));
        lblTempatTinggal.setTextFill(Color.web("#475569"));

        lblEmail = new Label();
        lblEmail.setFont(Font.font("System", 14));
        lblEmail.setTextFill(Color.web("#475569"));

        lblTelepon = new Label();
        lblTelepon.setFont(Font.font("System", 14));
        lblTelepon.setTextFill(Color.web("#475569"));

        Button btnEditProfile = new Button("📝  Edit Profil");
        btnEditProfile.setStyle("-fx-background-color: #1B365D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20 10 20; -fx-cursor: hand;");
        
        btnEditProfile.setOnAction(e -> {
            EditProfileDialog dialog = new EditProfileDialog(this::refreshProfileData);
            dialog.showAndWait();
        });

        rightSection.getChildren().addAll(lblNamaLengkap, lblTempatTinggal, lblEmail, lblTelepon, btnEditProfile);

        profileCard.getChildren().addAll(leftSection, rightSection);
        mainContent.getChildren().addAll(titleLabel, profileCard);
        this.getChildren().addAll(sidebar, mainContent);

        refreshProfileData();
    }

    public void refreshProfileData() {
        Customer currentCustomer = (Customer) SessionManager.getCurrentUser();
        
        if (currentCustomer != null) {
            // Membaca nilai alamat dinamis langsung dari penampung statis EditProfileDialog
            String alamat = EditProfileDialog.currentAlamatMock; 
            
            lblNamaLengkap.setText(currentCustomer.getName());
            lblUsernameBawah.setText(currentCustomer.getName().toLowerCase().replace(" ", ""));
            lblTempatTinggal.setText("📍  " + alamat);
            lblEmail.setText("✉️  " + currentCustomer.getEmail());
            lblTelepon.setText("📞  " + currentCustomer.getPhone());
        }
    }
}