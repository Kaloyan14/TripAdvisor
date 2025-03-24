public class User {
    private int id;
    private String username;
    private byte[] passwordHash;
    private String email;

    public User(int id, String username, byte[] passwordHash, String email) {   
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
