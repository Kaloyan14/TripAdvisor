package org.example;

import jakarta.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class AddLocationFrame extends JFrame {

    private EntityManagerFactory emf;
    private EntityManager em;

    public AddLocationFrame() {
        emf = Persistence.createEntityManagerFactory("my-pu");
        em = emf.createEntityManager();

        setTitle("Add Location");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        JLabel lblName = new JLabel("Location Name:");
        JTextField txtName = new JTextField();
        JLabel lblLatitude = new JLabel("Latitude:");
        JTextField txtLatitude = new JTextField();
        JLabel lblLongitude = new JLabel("Longitude:");
        JTextField txtLongitude = new JTextField();
        JLabel lblDescription = new JLabel("Description:");
        JTextArea txtDescription = new JTextArea();
        txtDescription.setRows(4);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setLineWrap(true);

        JLabel lblCategory = new JLabel("Categories:");
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        List<JCheckBox> categoryCheckBoxes = new ArrayList<>();

        JLabel lblImage = new JLabel("Upload Image:");
        JButton btnImage = new JButton("Choose Image");
        final File[] selectedImage = {null};

        JButton btnSubmit = new JButton("Submit");

        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblLatitude);
        panel.add(txtLatitude);
        panel.add(lblLongitude);
        panel.add(txtLongitude);
        panel.add(lblDescription);
        panel.add(new JScrollPane(txtDescription));
        panel.add(lblCategory);
        panel.add(categoryPanel);
        panel.add(lblImage);
        panel.add(btnImage);
        panel.add(btnSubmit);

        btnImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "gif"));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedImage[0] = fileChooser.getSelectedFile();
            }
        });

        btnSubmit.addActionListener(e -> {
            String name = txtName.getText();
            double latitude = Double.parseDouble(txtLatitude.getText());
            double longitude = Double.parseDouble(txtLongitude.getText());
            String description = txtDescription.getText();

            // Collect all selected categories
            List<Category> selectedCategories = new ArrayList<>();
            for (JCheckBox checkBox : categoryCheckBoxes) {
                if (checkBox.isSelected()) {
                    selectedCategories.add((Category) checkBox.getClientProperty("category"));
                }
            }

            saveLocationToDatabase(name, latitude, longitude, description, selectedCategories, selectedImage[0]);

            dispose();
        });

        loadCategories(categoryPanel, categoryCheckBoxes);

        add(panel);

        setVisible(true);
    }

    private void loadCategories(JPanel categoryPanel, List<JCheckBox> categoryCheckBoxes) {
        TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c", Category.class);
        List<Category> categories = query.getResultList();

        for (Category category : categories) {
            JCheckBox checkBox = new JCheckBox(category.getName());
            checkBox.putClientProperty("category", category);
            categoryCheckBoxes.add(checkBox);
            categoryPanel.add(checkBox);
        }
    }

    private void saveLocationToDatabase(String name, double latitude, double longitude, String description, List<Category> categories, File imageFile) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            Location location = new Location();
            location.setName(name);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setDescription(description);

            em.persist(location);

            if (imageFile != null) {
                FileInputStream imageStream = new FileInputStream(imageFile);
                byte[] imageBytes = imageStream.readAllBytes();

                Images image = new Images();
                image.setLocation(location);
                image.setPicture(imageBytes);
                em.persist(image);
            }

            if (categories != null && !categories.isEmpty()) {
                for (Category category : categories) {
                    if (category != null) {
                        System.out.println(category);
                        LocationCategory locationCategoryLink = new LocationCategory();
                        locationCategoryLink.setLocation(location);
                        locationCategoryLink.setCategory(category);
                        em.persist(locationCategoryLink);
                    }
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
