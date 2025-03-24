public class Comment {
    private Interaction interaction;
    private Location location;
    private String text;

    public Comment(Interaction interaction, Location location, String text) {
        this.interaction = interaction;
        this.location = location;
        this.text = text;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
