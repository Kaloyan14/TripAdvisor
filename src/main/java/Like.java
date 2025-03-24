public class Like {
    private Interaction interaction;
    private Location location;

    public Like(Interaction interaction, Location location) {
        this.interaction = interaction;
        this.location = location;
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
}
