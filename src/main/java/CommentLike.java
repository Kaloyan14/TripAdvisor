public class CommentLike {
    private Interaction interaction;
    private Comment comment;

    public CommentLike(Interaction interaction, Comment comment) {
        this.interaction = interaction;
        this.comment = comment;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
