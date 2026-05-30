package youspace;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
// Pastikan baris import ini tertulis dengan benar untuk memanggil AuthView
import youspace.view.AuthView;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Memanggil AuthView yang berada di package youspace.view
            AuthView authPage = new AuthView();
            Scene scene = new Scene(authPage, 900, 550);
            
            primaryStage.setTitle("YouSpace - Autentikasi Sistem");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Gagal meluncurkan aplikasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}