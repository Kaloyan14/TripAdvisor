import java.sql.Blob;

public class Images {
    private int id;
    private Location location;
    private Blob image;

    public Images(int id, Location location, Blob image) {
        this.id = id;
        this.location = location;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
