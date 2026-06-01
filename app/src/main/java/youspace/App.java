package youspace;

import javafx.application.Application;
import javafx.stage.Stage;
import youspace.database.DatabaseInitializer;
import youspace.view.LoginView;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Menyiapkan database SQLite saat start up
        DatabaseInitializer.initialize();
        
        // Membuka halaman login view sebagai landing page utama
        LoginView loginView = new LoginView(stage);
        stage.setScene(loginView.createScene());
        
        stage.setTitle("YouSpace - Aplikasi Penyewaan Venue");
        stage.setResizable(false); // Mengunci ukuran jendela agar layout konsisten sesuai figma
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}