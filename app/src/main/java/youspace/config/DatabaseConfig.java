package youspace.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String DATABASE_URL = "jdbc:sqlite:youspace.db";

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
    Connection conn = DriverManager.getConnection(DATABASE_URL);
    System.out.println("Terhubung ke database SQLite: " + DATABASE_URL);
    return conn;
    }
}