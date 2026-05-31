package youspace.service;

import youspace.dao.FavoriteDAO;
import youspace.models.Venue;

import java.util.List;

public class FavoriteService {

    private final FavoriteDAO favoriteDAO;

    public FavoriteService() {
        this.favoriteDAO = new FavoriteDAO();
    }

    public boolean addFavorite(int userId, int venueId) {
        return favoriteDAO.addFavorite(userId, venueId);
    }

    public boolean removeFavorite(int userId, int venueId) {
        return favoriteDAO.removeFavorite(userId, venueId);
    }

    public List<Venue> getFavoriteVenuesByUser(int userId) {
        return favoriteDAO.getFavoriteVenuesByUser(userId);
    }

    public boolean isFavorite(int userId, int venueId) {
        List<Venue> favorites = getFavoriteVenuesByUser(userId);
        if (favorites == null) return false;
        
        for (Venue v : favorites) {
            if (v.getId() == venueId) {
                return true;
            }
        }
        return false;
    }
}