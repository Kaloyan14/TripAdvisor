package org.example;

import jakarta.persistence.*;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {

    static User currentUser = null;
    public MainScreen(EntityManager em) {
        setTitle("Main Frame");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        JButton showLocationsButton = new JButton("Show Locations");
        JButton addLocationButton = new JButton("Add Location");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        buttonPanel.add(showLocationsButton);
        buttonPanel.add(addLocationButton);

        add(buttonPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            LoginScreen loginFrame = new LoginScreen(em);
            loginFrame.setVisible(true);
        });

        signUpButton.addActionListener(e -> {
            SignUpScreen signUpFrame = new SignUpScreen(em);
            signUpFrame.setVisible(true);
        });

        showLocationsButton.addActionListener(e -> {
            LocationsFrame locationsFrame = new LocationsFrame(em, currentUser);
            locationsFrame.setVisible(true);
        });

        addLocationButton.addActionListener(e -> new AddLocationFrame());

    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-pu");
        EntityManager em = emf.createEntityManager();

        SwingUtilities.invokeLater(() -> {
            MainScreen frame = new MainScreen(em);
            frame.setVisible(true);
        });
    }
}
