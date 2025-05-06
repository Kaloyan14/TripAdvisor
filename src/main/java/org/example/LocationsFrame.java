package org.example;

import jakarta.persistence.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class LocationsFrame extends JFrame {
    private EntityManager em;
    private User currentUser;

    public LocationsFrame(EntityManager em, User currentUser) {
        this.em = em;
        this.currentUser = currentUser;
        setTitle("Locations");
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        filterPanel.add(new JLabel("Filter by categories:"));

        List<Category> categories = em.createQuery("FROM Category", Category.class).getResultList();
        JCheckBox[] categoryCheckBoxes = new JCheckBox[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            JCheckBox checkBox = new JCheckBox(category.getName());
            categoryCheckBoxes[i] = checkBox;
            filterPanel.add(checkBox);
        }

        JButton filterButton = new JButton("Apply Filter");
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);

        showLocations(listPanel, null);

        filterButton.addActionListener(e -> {
            List<Category> selectedCategories = getSelectedCategories(categoryCheckBoxes);

            showLocations(listPanel, selectedCategories);
        });
    }

    private void showLocations(JPanel listPanel, List<Category> selectedCategories) {
        listPanel.removeAll();

        List<Location> locations = getFilteredLocations(selectedCategories);

        for (Location loc : locations) {
            JPanel locPanel = new JPanel(new BorderLayout());
            locPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            Images image = null;
            try {
                image = em.createQuery("SELECT i FROM Images i WHERE i.location = :loc", Images.class)
                        .setParameter("loc", loc)
                        .setMaxResults(1)
                        .getSingleResult();
            } catch (NoResultException ignored) {}

            JLabel imgLabel;
            if (image != null && image.getPicture() != null) {
                ImageIcon img = new ImageIcon(image.getPicture());
                Image scaled = img.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imgLabel = new JLabel(new ImageIcon(scaled));
            } else {
                imgLabel = new JLabel(grayPlaceholder());
            }

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.add(new JLabel("<html><b>" + loc.getName() + "</b></html>"));
            textPanel.add(new JLabel("<html>" + loc.getDescription() + "</html>"));

            locPanel.add(imgLabel, BorderLayout.WEST);
            locPanel.add(textPanel, BorderLayout.CENTER);

            locPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            locPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new LocationDetailsFrame(em, loc, currentUser).setVisible(true);
                }
            });

            listPanel.add(locPanel);
        }

        revalidate();
        repaint();
    }

    private List<Category> getSelectedCategories(JCheckBox[] categoryCheckBoxes) {
        List<Category> selectedCategories = new java.util.ArrayList<>();
        for (int i = 0; i < categoryCheckBoxes.length; i++) {
            if (categoryCheckBoxes[i].isSelected()) {
                selectedCategories.add(em.createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
                        .setParameter("name", categoryCheckBoxes[i].getText())
                        .getSingleResult());
            }
        }
        return selectedCategories;
    }

    private List<Location> getFilteredLocations(List<Category> selectedCategories) {
        if (selectedCategories == null || selectedCategories.isEmpty()) {
            return em.createQuery("FROM Location", Location.class).getResultList();
        }

        String queryStr = "SELECT loc FROM Location loc " +
                "JOIN LocationCategory lc ON lc.location = loc " +
                "WHERE lc.category IN :categories " +
                "GROUP BY loc.id " +
                "HAVING COUNT(DISTINCT lc.category) = :categoryCount";

        return em.createQuery(queryStr, Location.class)
                .setParameter("categories", selectedCategories)
                .setParameter("categoryCount", selectedCategories.size())
                .getResultList();
    }

    private Icon grayPlaceholder() {
        BufferedImage gray = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = gray.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 100, 100);
        g.dispose();
        return new ImageIcon(gray);
    }
}
