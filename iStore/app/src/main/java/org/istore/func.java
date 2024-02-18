package org.istore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.mindrot.jbcrypt.BCrypt;

import com.mysql.cj.xdevapi.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

public class func {
    // Constantes de connexion à la base de données
    private static final String DB_URL = "jdbc:mysql://eliascastel.ddns.net:3306/2java";
    private static final String DB_USER = "java";
    private static final String DB_PASSWORD = "!pn!XrZLgt-pn2RP";

    public void connection(Object frame) {
        JTextField eMailField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        passwordField.setEchoChar('●'); // Masque le texte saisi dans le champ de mot de passe

        Object[] message = { "E-Mail:", eMailField, "Code:", passwordField };
        int option = JOptionPane.showOptionDialog(null, message, "Connexion", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new String[] { "Connexion", "Inscription", "Code oublié" }, null);
        if (option == JOptionPane.OK_OPTION) {

            // Récupérer les informations de connexion
            String eMail = eMailField.getText();
            char[] password = passwordField.getPassword();

            // Hacher le mot de passe
            String hashedPassword = BCrypt.hashpw(new String(password), "$2a$10$VM/GfVScMMgdLVtHwABv6u");

            // Vérification des données de connexion
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, eMail);
                    stmt.setString(2, hashedPassword);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String role = rs.getString("role");
                            JOptionPane.showMessageDialog(null, "Connexion réussie", "Connexion",
                                    JOptionPane.INFORMATION_MESSAGE);
                            ((javax.swing.JFrame) frame).getJMenuBar().setVisible(true);
                            // Enregistrer les informations de l'utilisateur connecté
                            App.id = rs.getString("id");
                            App.pseudo = rs.getString("pseudo");
                            App.eMail = rs.getString("email");
                            App.role = role;
                            // ce qui est stocké dans la SQL [{"id": 1, "role_name": "responsable"}, {"id":
                            // 2, "role_name": "employee"}]
                            // ce qui est stocké dans le store SQL [{"id": 1, "store_name": "store1"},
                            // {"id": 2, "store_name": "store2"}]

                            String store = rs.getString("store");
                            JSONArray storeArray = new JSONArray(store);
                            String[][] storeTable = new String[storeArray.length()][2];
                            for (int i = 0; i < storeArray.length(); i++) {
                                JSONObject storeObject = storeArray.getJSONObject(i);
                                storeTable[i][0] = String.valueOf(storeObject.getInt("id"));
                                storeTable[i][1] = storeObject.getString("role");
                            }
                            App.store = storeTable;

                            System.out.println("id: " + App.id);
                            System.out.println("pseudo: " + App.pseudo);
                            System.out.println("eMail: " + App.eMail);
                            System.out.println("role: " + App.role);
                            System.out.println("Store:");
                            for (String[] storeRow : App.store) {
                                for (String storeData : storeRow) {
                                    System.out.print(storeData + " ");
                                }
                                System.out.println(); // Nouvelle ligne pour chaque ligne de données de magasin
                            }

                            // si role ADMIN on affiche le menu admin
                            if ("ADMIN".equals(role)) {
                                ((javax.swing.JFrame) frame).getJMenuBar().getMenu(2).setVisible(true);
                            }
                            ((javax.swing.JFrame) frame).getContentPane().removeAll();

                            ((javax.swing.JFrame) frame).getContentPane()
                                    .add(new javax.swing.JLabel("Hello World, Welcome to iStore!"));

                            ((javax.swing.JFrame) frame).revalidate();
                        } else {
                            JOptionPane.showMessageDialog(null, "Pseudo ou Mot de passe incorrect",
                                    "Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur de Connexion, veuillez réessayer \n ou contacter un Administrateur",
                        "Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } else if (option == 1) {
            // Gérer l'inscription
            registration(frame);
        } else if (option == 2) {
            // Gérer le code oublié
            JOptionPane.showMessageDialog(null, "Veuillez contacter un Administrateur", "Code oublié",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println("Login canceled!");
        }
    }

    public void registration(Object frame) {
        JTextField eMailField = new JTextField(10);
        JTextField pseudoField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JPasswordField confirmPasswordField = new JPasswordField(10);
        passwordField.setEchoChar('●'); // Masque le texte saisi dans le champ de mot de passe
        confirmPasswordField.setEchoChar('●'); // Masque le texte saisi dans le champ de mot de passe

        Object[] message = { "E-Mail:", eMailField, "Pseudo:", pseudoField, "Code:", passwordField,
                "Confirmer le code:", confirmPasswordField };
        int option = JOptionPane.showOptionDialog(null, message, "Inscription", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new String[] { "Inscription", "Connexion" }, null);
        if (option == JOptionPane.OK_OPTION) {

            String eMail = eMailField.getText();
            String pseudo = pseudoField.getText();
            char[] password = passwordField.getPassword();
            char[] confirmPassword = confirmPasswordField.getPassword();

            // Vérifier si les mots de passe sont identiques
            if (new String(password).equals(new String(confirmPassword))) {
                // Hacher le mot de passe
                String hashedPassword = BCrypt.hashpw(new String(password), "$2a$10$VM/GfVScMMgdLVtHwABv6u");

                // Vérification et insertion des données dans la base de données
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

                    // Vérification de l'email dans la whitelist
                    String sql = "SELECT * FROM whitelist WHERE email = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, eMail);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Récupération du rôle et du magasin (store) depuis la whitelist
                                String role = rs.getString("role");
                                String store = rs.getString("store");

                                // Vérification de l'email dans la table des utilisateurs
                                sql = "SELECT * FROM users WHERE email = ?";
                                try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                                    stmt2.setString(1, eMail);
                                    try (ResultSet rs2 = stmt2.executeQuery()) {
                                        if (rs2.next()) {
                                            JOptionPane.showMessageDialog(null,
                                                    "Vous ne pouvez pas vous inscrire avec cet e-mail",
                                                    "Erreur d'Inscription", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            // Vérification du pseudo dans la table des utilisateurs
                                            sql = "SELECT * FROM users WHERE pseudo = ?";
                                            try (PreparedStatement stmt3 = conn.prepareStatement(sql)) {
                                                stmt3.setString(1, pseudo);
                                                try (ResultSet rs3 = stmt3.executeQuery()) {
                                                    if (rs3.next()) {
                                                        JOptionPane.showMessageDialog(null,
                                                                "Ce pseudo est déjà utilisé",
                                                                "Erreur d'Inscription", JOptionPane.ERROR_MESSAGE);
                                                    } else {
                                                        // Insertion du nouvel utilisateur avec le rôle et le
                                                        // magasin (store) récupérés
                                                        sql = "INSERT INTO users (email, pseudo, password, role, store) VALUES (?, ?, ?, ?, ?)";
                                                        try (PreparedStatement stmt4 = conn.prepareStatement(sql)) {
                                                            stmt4.setString(1, eMail);
                                                            stmt4.setString(2, pseudo);
                                                            stmt4.setString(3, hashedPassword);
                                                            stmt4.setString(4, role);
                                                            stmt4.setString(5, store);
                                                            stmt4.executeUpdate();

                                                            JOptionPane.showMessageDialog(null,
                                                                    "Inscription réussie", "Inscription",
                                                                    JOptionPane.INFORMATION_MESSAGE);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Vous ne pouvez pas vous inscrire avec cet e-mail", "Erreur d'Inscription",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,
                            "Erreur d'Inscription, veuillez réessayer ou contacter un Administrateur",
                            "Erreur d'Inscription", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Les mots de passe ne correspondent pas", "Erreur d'Inscription",
                        JOptionPane.ERROR_MESSAGE);
            }

        } else if (option == 1) {
            connection(frame);
        } else {
            System.out.println("Registration canceled");
        }
    }
}