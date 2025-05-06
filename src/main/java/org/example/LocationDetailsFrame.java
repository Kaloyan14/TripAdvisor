package org.example;

import jakarta.persistence.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

public class LocationDetailsFrame extends JFrame {
    private final EntityManager em;
    private final Location loc;
    private final User currentUser;

    public LocationDetailsFrame(EntityManager em, Location loc, User currentUser) {
        this.em = em;
        this.loc = loc;
        this.currentUser = currentUser;

        setTitle(loc.getName());
        setSize(700, 600);
        setLayout(new BorderLayout());

        buildUI();
    }

    private void buildUI() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Location Info"));

        JLabel imgLabel = loadImageLabel();

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.add(new JLabel("Name: " + loc.getName()));
        textPanel.add(new JLabel("Latitude: " + loc.getLatitude()));
        textPanel.add(new JLabel("Longitude: " + loc.getLongitude()));
        textPanel.add(new JLabel("<html>Description:<br/>" + loc.getDescription() + "</html>"));

        infoPanel.add(imgLabel, BorderLayout.WEST);
        infoPanel.add(textPanel, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.NORTH);

        JPanel commentWrapper = new JPanel(new BorderLayout());

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));

        List<Comment> comments = em.createQuery("SELECT c FROM Comment c WHERE c.location = :loc", Comment.class)
                .setParameter("loc", loc)
                .getResultList();

        for (Comment comment : comments) {
            JPanel cPanel = new JPanel(new BorderLayout());
            cPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createLineBorder(Color.GRAY)));

            String userText = comment.getUser() != null ? comment.getUser().getUsername() : "Anonymous";
            JLabel commentLabel = new JLabel("<html><b>" + userText + ":</b><br/>" + comment.getText() + "</html>");
            cPanel.add(commentLabel, BorderLayout.CENTER);

            if (currentUser != null) {
                List<CommentLike> existing = em.createQuery(
                                "SELECT cl FROM CommentLike cl WHERE cl.comment = :c AND cl.user = :u", CommentLike.class)
                        .setParameter("c", comment)
                        .setParameter("u", currentUser)
                        .getResultList();
                boolean alreadyLiked = !existing.isEmpty();

                JButton likeBtn = new JButton(alreadyLiked ? "Unlike Comment" : "Like Comment");
                likeBtn.setBackground(alreadyLiked ? Color.RED : null);

                likeBtn.addActionListener(e -> {
                    em.getTransaction().begin();
                    if (alreadyLiked) {
                        em.remove(existing.get(0));
                        likeBtn.setText("Like Comment");
                        likeBtn.setBackground(null);
                    } else {
                        CommentLike cl = new CommentLike();
                        cl.setUser(currentUser);
                        cl.setComment(comment);
                        cl.setTimestamp(LocalDateTime.now());
                        em.persist(cl);
                        likeBtn.setText("Unlike Comment");
                        likeBtn.setBackground(Color.RED);
                    }
                    em.getTransaction().commit();
                    buildUI();
                });

                cPanel.add(likeBtn, BorderLayout.EAST);
            }

            commentPanel.add(cPanel);
        }

        JScrollPane scrollPane = new JScrollPane(commentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(550, 250));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        commentWrapper.add(scrollPane, BorderLayout.CENTER);

        add(commentWrapper, BorderLayout.CENTER);


        JPanel addCommentPanel = new JPanel(new BorderLayout());
        JTextField commentField = new JTextField();
        JButton submitComment = new JButton("Submit Comment");

        if (currentUser == null) {
            commentField.setEnabled(false);
            submitComment.setEnabled(false);
        }

        submitComment.addActionListener(e -> {
            String text = commentField.getText().trim();
            if (!text.isEmpty()) {
                em.getTransaction().begin();
                Comment newComment = new Comment();
                newComment.setText(text);
                newComment.setUser(currentUser);
                newComment.setLocation(loc);
                newComment.setTimestamp(LocalDateTime.now());
                em.persist(newComment);
                em.getTransaction().commit();
                buildUI();
            }
        });

        addCommentPanel.add(commentField, BorderLayout.CENTER);
        addCommentPanel.add(submitComment, BorderLayout.EAST);
        add(addCommentPanel, BorderLayout.SOUTH);

        JPanel interactionPanel = new JPanel();
        interactionPanel.setLayout(new BoxLayout(interactionPanel, BoxLayout.Y_AXIS));

        if (currentUser != null) {
            List<Like> likes = em.createQuery(
                            "SELECT l FROM Like l WHERE l.user = :user AND l.location = :loc", Like.class)
                    .setParameter("user", currentUser)
                    .setParameter("loc", loc)
                    .getResultList();
            boolean liked = !likes.isEmpty();
            JButton likeLoc = new JButton(liked ? "Unlike Location" : "Like Location");
            likeLoc.setBackground(liked ? Color.RED : null);

            likeLoc.addActionListener(e -> {
                em.getTransaction().begin();
                if (liked) {
                    em.remove(likes.get(0));
                    likeLoc.setText("Like Location");
                    likeLoc.setBackground(null);
                } else {
                    Like l = new Like();
                    l.setUser(currentUser);
                    l.setLocation(loc);
                    l.setTimestamp(LocalDateTime.now());
                    em.persist(l);
                    likeLoc.setText("Unlike Location");
                    likeLoc.setBackground(Color.RED);
                }

                em.getTransaction().commit();
                buildUI();
            });

            List<Favorite> favs = em.createQuery(
                            "SELECT f FROM Favorite f WHERE f.user = :user AND f.location = :loc", Favorite.class)
                    .setParameter("user", currentUser)
                    .setParameter("loc", loc)
                    .getResultList();
            boolean fav = !favs.isEmpty();
            JButton favLoc = new JButton(fav ? "Unfavorite" : "Favorite Location");
            favLoc.setBackground(fav ? Color.YELLOW : null);

            favLoc.addActionListener(e -> {
                em.getTransaction().begin();
                if (fav) {
                    em.remove(favs.get(0));
                    favLoc.setText("Favorite Location");
                    favLoc.setBackground(null);
                } else {
                    Favorite f = new Favorite();
                    f.setUser(currentUser);
                    f.setLocation(loc);
                    f.setTimestamp(LocalDateTime.now());
                    em.persist(f);
                    favLoc.setText("Unfavorite");
                    favLoc.setBackground(Color.YELLOW);
                }

                em.getTransaction().commit();
                buildUI();
            });

            interactionPanel.add(likeLoc);
            interactionPanel.add(favLoc);
        } else {
            interactionPanel.add(new JLabel("Log in to interact"));
        }

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> buildUI());
        interactionPanel.add(Box.createVerticalStrut(10));
        interactionPanel.add(refresh);

        add(interactionPanel, BorderLayout.WEST);

        revalidate();
        repaint();
    }

    private JLabel loadImageLabel() {
        Images image = null;
        try {
            image = em.createQuery(
                            "SELECT i FROM Images i WHERE i.location = :loc", Images.class)
                    .setParameter("loc", loc)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ignored) {}

        if (image != null && image.getPicture() != null) {
            try {
                BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(image.getPicture()));
                if (bufImg != null) {
                    return new JLabel(new ImageIcon(bufImg.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                }
            } catch (Exception ignored) {}
        }

        JLabel placeholder = new JLabel();
        placeholder.setPreferredSize(new Dimension(100, 100));
        placeholder.setOpaque(true);
        placeholder.setBackground(Color.GRAY);
        return placeholder;
    }
}
