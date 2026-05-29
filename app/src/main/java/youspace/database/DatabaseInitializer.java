package youspace.database;

import java.sql.Connection;
import java.sql.Statement;

import youspace.config.DatabaseConfig;

public class DatabaseInitializer {

    public static void initialize() {
        createTables();
        insertDefaultAdmin();
    }

    private static void createTables() {
        String usersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                phone TEXT NOT NULL,
                role TEXT NOT NULL CHECK(role IN ('USER', 'ADMIN')),
                status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'SUSPENDED')),
                created_at TEXT DEFAULT CURRENT_TIMESTAMP
            );
        """;

        String venuesTable = """
            CREATE TABLE IF NOT EXISTS venues (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT,
                category TEXT NOT NULL
                    CHECK(category IN ('BALLROOM', 'MEETING_ROOM', 'AULA', 'STUDIO', 'WEDDING_HALL', 'TOURNAMENT_HALL')),
                capacity INTEGER NOT NULL,
                price_per_day REAL NOT NULL,
                image_path TEXT,
                status TEXT NOT NULL DEFAULT 'AVAILABLE'
                    CHECK(status IN ('AVAILABLE', 'UNAVAILABLE')),
                created_at TEXT DEFAULT CURRENT_TIMESTAMP
            );
        """;

        String bookingsTable = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                venue_id INTEGER NOT NULL,
                event_name TEXT NOT NULL,
                guest_count INTEGER NOT NULL,
                start_date TEXT NOT NULL,
                end_date TEXT NOT NULL,
                total_price REAL NOT NULL,
                status TEXT NOT NULL DEFAULT 'PENDING'
                    CHECK(status IN ('PENDING', 'WAITING_PAYMENT', 'WAITING_CONFIRMATION', 'APPROVED', 'REJECTED', 'COMPLETED')),
                note TEXT,
                created_at TEXT DEFAULT CURRENT_TIMESTAMP,

                FOREIGN KEY(user_id) REFERENCES users(id),
                FOREIGN KEY(venue_id) REFERENCES venues(id)
            );
        """;

        String paymentsTable = """
            CREATE TABLE IF NOT EXISTS payments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                booking_id INTEGER NOT NULL UNIQUE,
                method TEXT NOT NULL CHECK(method IN ('TRANSFER_BANK', 'QRIS')),
                payment_status TEXT NOT NULL DEFAULT 'PAID'
                    CHECK(payment_status IN ('PAID')),
                paid_at TEXT DEFAULT CURRENT_TIMESTAMP,

                FOREIGN KEY(booking_id) REFERENCES bookings(id)
            );
        """;

        String favoritesTable = """
            CREATE TABLE IF NOT EXISTS favorites (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                venue_id INTEGER NOT NULL,
                created_at TEXT DEFAULT CURRENT_TIMESTAMP,

                FOREIGN KEY(user_id) REFERENCES users(id),
                FOREIGN KEY(venue_id) REFERENCES venues(id),

                UNIQUE(user_id, venue_id)
            );
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement()
        ) {
            stmt.execute(usersTable);
            stmt.execute(venuesTable);
            stmt.execute(bookingsTable);
            stmt.execute(paymentsTable);
            stmt.execute(favoritesTable);

            System.out.println("Database berhasil dibuat.");
        } catch (Exception e) {
            System.out.println("Gagal membuat database.");
            e.printStackTrace();
        }
    }

    private static void insertDefaultAdmin() {
        String sql = """
            INSERT OR IGNORE INTO users (id, name, email, password, phone, role, status)
            VALUES (1, 'Admin YouSpace', 'admin@youspace.com', 'admin123', '08123456789', 'ADMIN', 'ACTIVE');
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println("Gagal membuat admin default.");
            e.printStackTrace();
        }
    }
}