import java.util.List;

public class Location {
    private int id;
    private String name;
    private double lat;
    private double lon;
    private String description;
    private List<Category> categories;

    public Location(int id, String name, double lat, double lon, String description, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.description = description;
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
