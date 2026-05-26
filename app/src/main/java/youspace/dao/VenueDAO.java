package youspace.dao;

import youspace.config.DatabaseConfig;
import youspace.enums.VenueStatus;
import youspace.models.Venue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueDAO {

    public boolean addVenue(Venue venue) {
        String sql = """
            INSERT INTO venues (name, description, category, location, capacity, price_per_day, image_path, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, venue.getName());
            stmt.setString(2, venue.getDescription());
            stmt.setString(3, venue.getCategory());
            stmt.setString(4, venue.getLocation());
            stmt.setInt(5, venue.getCapacity());
            stmt.setDouble(6, venue.getPricePerDay());
            stmt.setString(7, venue.getImagePath());
            stmt.setString(8, venue.getStatus().name());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Gagal tambah venue: " + e.getMessage());
            return false;
        }
    }

    public boolean updateVenue(Venue venue) {
        String sql = """
            UPDATE venues
            SET name = ?, description = ?, category = ?, location = ?, capacity = ?,
                price_per_day = ?, image_path = ?, status = ?
            WHERE id = ?;
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, venue.getName());
            stmt.setString(2, venue.getDescription());
            stmt.setString(3, venue.getCategory());
            stmt.setString(4, venue.getLocation());
            stmt.setInt(5, venue.getCapacity());
            stmt.setDouble(6, venue.getPricePerDay());
            stmt.setString(7, venue.getImagePath());
            stmt.setString(8, venue.getStatus().name());
            stmt.setInt(9, venue.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Gagal update venue: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteVenue(int venueId) {
        String sql = "DELETE FROM venues WHERE id = ?;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, venueId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Gagal hapus venue: " + e.getMessage());
            return false;
        }
    }

    public Venue findById(int venueId) {
        String sql = "SELECT * FROM venues WHERE id = ?;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, venueId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVenue(rs);
            }
        } catch (SQLException e) {
            System.out.println("Gagal mencari venue: " + e.getMessage());
        }

        return null;
    }

    public List<Venue> getAllVenues() {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM venues ORDER BY id DESC;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                venues.add(mapResultSetToVenue(rs));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil venue: " + e.getMessage());
        }

        return venues;
    }

    public List<Venue> searchVenues(String keyword) {
        List<Venue> venues = new ArrayList<>();
        String sql = """
            SELECT * FROM venues
            WHERE name LIKE ? OR category LIKE ? OR location LIKE ?
            ORDER BY id DESC;
        """;

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                venues.add(mapResultSetToVenue(rs));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mencari venue: " + e.getMessage());
        }

        return venues;
    }

    public List<Venue> getVenuesByCategory(String category) {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM venues WHERE category = ? ORDER BY id DESC;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, category);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                venues.add(mapResultSetToVenue(rs));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil venue kategori: " + e.getMessage());
        }

        return venues;
    }

    public int countVenues() {
        String sql = "SELECT COUNT(*) AS total FROM venues;";

        try (
            Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menghitung venue: " + e.getMessage());
        }

        return 0;
    }

    private Venue mapResultSetToVenue(ResultSet rs) throws SQLException {
        return new Venue(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("category"),
            rs.getString("location"),
            rs.getInt("capacity"),
            rs.getDouble("price_per_day"),
            rs.getString("image_path"),
            VenueStatus.valueOf(rs.getString("status"))
        );
    }
}