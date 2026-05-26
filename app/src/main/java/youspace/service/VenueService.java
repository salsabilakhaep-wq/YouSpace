package youspace.service;

import youspace.dao.VenueDAO;
import youspace.enums.VenueStatus;
import youspace.models.Venue;
import youspace.utils.ValidationUtil;

import java.util.List;

public class VenueService {

    private final VenueDAO venueDAO;

    public VenueService() {
        this.venueDAO = new VenueDAO();
    }

    public boolean addVenue(String name, String description, String category, String location,
                            int capacity, double pricePerDay, String imagePath) {
        validateVenueData(name, category, capacity, pricePerDay);

        Venue venue = new Venue();
        venue.setName(name);
        venue.setDescription(description);
        venue.setCategory(category);
        venue.setLocation(location);
        venue.setCapacity(capacity);
        venue.setPricePerDay(pricePerDay);
        venue.setImagePath(imagePath);
        venue.setStatus(VenueStatus.AVAILABLE);

        return venueDAO.addVenue(venue);
    }

    public boolean updateVenue(Venue venue) {
        validateVenueData(venue.getName(), venue.getCategory(), venue.getCapacity(), venue.getPricePerDay());
        return venueDAO.updateVenue(venue);
    }

    public boolean deleteVenue(int venueId) {
        if (venueId <= 0) {
            throw new IllegalArgumentException("Venue ID tidak valid.");
        }

        return venueDAO.deleteVenue(venueId);
    }

    public Venue getVenueById(int venueId) {
        return venueDAO.findById(venueId);
    }

    public List<Venue> getAllVenues() {
        return venueDAO.getAllVenues();
    }

    public List<Venue> searchVenues(String keyword) {
        if (ValidationUtil.isEmpty(keyword)) {
            return getAllVenues();
        }

        return venueDAO.searchVenues(keyword);
    }

    public List<Venue> getVenuesByCategory(String category) {
        return venueDAO.getVenuesByCategory(category);
    }

    private void validateVenueData(String name, String category, int capacity, double pricePerDay) {
        if (ValidationUtil.isEmpty(name)) {
            throw new IllegalArgumentException("Nama venue wajib diisi.");
        }

        if (ValidationUtil.isEmpty(category)) {
            throw new IllegalArgumentException("Kategori venue wajib diisi.");
        }

        if (!ValidationUtil.isPositiveNumber(capacity)) {
            throw new IllegalArgumentException("Kapasitas harus lebih dari 0.");
        }

        if (!ValidationUtil.isPositivePrice(pricePerDay)) {
            throw new IllegalArgumentException("Harga tidak boleh negatif.");
        }
    }
}