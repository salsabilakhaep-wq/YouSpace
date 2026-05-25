package youspace.models;

public class Favorite {

    private int id;
    private int userId;
    private int venueId;

    public Favorite() {
    }

    public Favorite(int id, int userId, int venueId) {
        this.id = id;
        this.userId = userId;
        this.venueId = venueId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID favorite tidak boleh negatif.");
        }
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID tidak valid.");
        }
        this.userId = userId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        if (venueId <= 0) {
            throw new IllegalArgumentException("Venue ID tidak valid.");
        }
        this.venueId = venueId;
    }
}