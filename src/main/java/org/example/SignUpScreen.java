package org.example;


import jakarta.persistence.*;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpScreen extends JFrame {

    public static byte[] hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public SignUpScreen(EntityManager em) {
        setTitle("Sign Up");
        setSize(300, 250);
        setLayout(new GridLayout(5, 2));

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton submit = new JButton("Register");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel());
        add(submit);

        submit.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            byte[] hashedPassword = hashPassword(password);

            em.getTransaction().begin();
            try {
                User user = new User();
                user.setUsername(username);
                user.setEmail(email);
                user.setPasswordHash(hashedPassword);
                em.persist(user);
                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "User registered successfully!");
                dispose();
            } catch (Exception ex) {
                em.getTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}

