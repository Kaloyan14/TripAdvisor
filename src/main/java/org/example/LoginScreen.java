package org.example;

import static org.example.SignUpScreen.hashPassword;
import jakarta.persistence.*;

import javax.swing.*;
import java.awt.*;


public class LoginScreen extends JFrame {
    public LoginScreen(EntityManager em) {
        setTitle("Login");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
        add(signUpButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            byte[] hashed = hashPassword(password);

            try {
                User user = em.createQuery(
                                "SELECT u FROM User u WHERE u.username = :username AND u.passwordHash = :hash",
                                User.class)
                        .setParameter("username", username)
                        .setParameter("hash", hashed)
                        .getSingleResult();

                JOptionPane.showMessageDialog(this, "Login successful!");
                MainScreen.currentUser = user;
                dispose();
            } catch (NoResultException ex) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });

        signUpButton.addActionListener(e -> {
            new SignUpScreen(em);
        });

        setVisible(true);


    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-pu");
        EntityManager em = emf.createEntityManager();
        SwingUtilities.invokeLater(() -> new LoginScreen(em));
    }

}

