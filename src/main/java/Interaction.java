import java.sql.Timestamp;

public class Interaction {
    private int id;
    private User user;
    private Timestamp timestamp;

    public Interaction(int id, User user, Timestamp timestamp) {
        this.id = id;
        this.user = user;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
