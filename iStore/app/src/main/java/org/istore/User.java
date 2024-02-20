package org.istore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.mindrot.jbcrypt.BCrypt;

public class User {
    // stocker les informations de l'utilisateur connecté
    public static String id = "";
    public static String pseudo = "";
    public static String eMail = "";
    public static String role = "";
    public static String page = "";
    public static Integer store = null;

    public void connection(Object frame) {
        // Créer les champs de saisie pour l'email et le mot de passe
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
            try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
                String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, eMail);
                    stmt.setString(2, hashedPassword);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {

                            // Enregistrer les informations de l'utilisateur connecté
                            String role = rs.getString("role");
                            User.id = rs.getString("id");
                            User.pseudo = rs.getString("pseudo");
                            User.eMail = rs.getString("email");
                            User.role = role;
                            User.store = rs.getInt("store");

                            // Afficher un message de connexion réussie
                            JOptionPane.showMessageDialog(null, "Connexion réussie", "Connexion",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // Afficher le menu et le contenu de la frame
                            ((javax.swing.JFrame) frame).getJMenuBar().setVisible(true);

                            // si role ADMIN on affiche le menu admin
                            if ("ADMIN".equals(role)) {
                                ((javax.swing.JFrame) frame).getJMenuBar().getMenu(2).setVisible(true);
                            }

                            ((javax.swing.JFrame) frame).getContentPane().removeAll();
                            ((javax.swing.JFrame) frame).getContentPane().add(new javax.swing.JLabel("Bienvenue dans l'application iStore"));
                            ((javax.swing.JFrame) frame).revalidate();
                            new Store().itemList(frame);
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
                try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {

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
                                                            sql = "DELETE FROM whitelist WHERE email = ?";
                                                            try (PreparedStatement stmt5 = conn.prepareStatement(sql)) {
                                                                stmt5.setString(1, eMail);
                                                                stmt5.executeUpdate();
                                                            }
                                                        } catch (SQLException e) {
                                                            JOptionPane.showMessageDialog(null,
                                                                    "Erreur d'Inscription, veuillez réessayer ou contacter un Administrateur",
                                                                    "Erreur d'Inscription", JOptionPane.ERROR_MESSAGE);
                                                            e.printStackTrace();
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

    public void disconnection(Object frame) {
        // Réinitialiser les informations de l'utilisateur connecté
        User.id = "";
        User.pseudo = "";
        User.eMail = "";
        User.role = "";
        User.store = null;

        // Cacher le menu et le contenu de la frame
        ((javax.swing.JFrame) frame).getJMenuBar().setVisible(false);
        ((javax.swing.JFrame) frame).getJMenuBar().getMenu(2).setVisible(false);
        ((javax.swing.JFrame) frame).getContentPane().removeAll();
        JButton connectionButton = new JButton("Connexion");
        JButton registrationButton = new JButton("Inscription");

        // Ajout des actions aux boutons
        connectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action de connexion
                new User().connection(frame);
            }
        });

        registrationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action d'inscription
                new User().registration(frame);
            }
        });

        // Ajout des boutons à la frame avec BorderLayout
        ((javax.swing.JFrame) frame).setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(connectionButton);
        panel.add(registrationButton);
        ((javax.swing.JFrame) frame).add(panel, BorderLayout.CENTER);
        ((javax.swing.JFrame) frame).revalidate();
    }

    public void userList(Object frame) {
        User.page = "userList";
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            ((javax.swing.JFrame) frame).getContentPane().removeAll();
            ((javax.swing.JFrame) frame).setLayout(new BorderLayout());
            String sql = "SELECT * FROM store";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    String[][] stores = new String[0][0];
                    // il peut y avoir des sauts dans les id des magasins fait une liste liste =
                    // [id][nom du magasin]
                    while (rs.next()) {
                        String[] row = { rs.getString("id"), rs.getString("name") };
                        stores = java.util.Arrays.copyOf(stores, stores.length + 1);
                        stores[stores.length - 1] = row;
                    }
                    sql = "SELECT * FROM users";
                    try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                        try (ResultSet rs2 = stmt2.executeQuery()) {
                            String[] columnNames = { "Pseudo", "E-Mail", "Role", "Store" };
                            String[][] data = new String[0][0];
                            while (rs2.next()) {
                                String pseudo = rs2.getString("pseudo");
                                String eMail = rs2.getString("email");
                                String role = rs2.getString("role");
                                String store = rs2.getString("store");
                                if (store != null) {
                                    for (int i = 0; i < stores.length; i++) {
                                        if (store.equals(stores[i][0])) {
                                            store = stores[i][1];
                                        }
                                    }
                                }
                                String[] row = { pseudo, eMail, role, store };
                                data = java.util.Arrays.copyOf(data, data.length + 1);
                                data[data.length - 1] = row;
                            }
                            JTable table = new JTable(data, columnNames) {
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            JScrollPane scrollPane = new JScrollPane(table);
                            ((javax.swing.JFrame) frame).add(new JLabel("Liste des utilisateurs"), BorderLayout.NORTH);
                            if ("ADMIN".equals(User.role)) {
                                Button selectActionUser = new Button("Effectuer une action pour un utilisateur");
                                selectActionUser.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        new User().selectActionUser(frame);
                                    }
                                });
                                ((javax.swing.JFrame) frame).add(selectActionUser, BorderLayout.SOUTH);
                            }
                            ((javax.swing.JFrame) frame).add(scrollPane, BorderLayout.CENTER);
                            ((javax.swing.JFrame) frame).revalidate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des magasins",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void selectActionUser(Object frame) {
        String[] options = { "Ajouter", "Modifier", "Supprimer" };
        int response = JOptionPane.showOptionDialog(null, "Que voulez-vous faire ?", "Action pour un utilisateur",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (response) {
            case 0:
                new User().addUser(frame);
                break;
            case 1:
                new User().editUser(frame);
                break;
            case 2:
                new User().deleteUser(frame);
                break;
            default:
                break;
        }
    }

    public void addUser(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sqlSelect = "SELECT * FROM store";
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    JTextField emailField = new JTextField(10);
                    JComboBox<String> roleField = new JComboBox<>(new String[] { "EMPLOYEE", "MANAGER", "ADMIN" });
                    JComboBox<String> storeList = new JComboBox<>();
                    storeList.addItem("Sélectionnez un magasin");
                    while (rs.next()) {
                        storeList.addItem(rs.getString("id") + " - " + rs.getString("name"));
                    }
                    Object[] message = {
                            "E-Mail:", emailField,
                            "Rôle:", roleField,
                            "Magasin:", storeList,
                    };
                    int option = JOptionPane.showConfirmDialog((Component) frame, message, "Ajouter un utilisateur",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        String email = emailField.getText();
                        String role = (String) roleField.getSelectedItem();
                        String selectedStore = storeList.getSelectedItem().toString();
                        int storeId = 0;
                        if (!"Sélectionnez un magasin".equals(selectedStore)) {
                            String[] idStore = selectedStore.split(" - ");
                            storeId = Integer.parseInt(idStore[0]);
                        }
                        if (email.isEmpty()
                                || ("Sélectionnez un magasin".equals(selectedStore) && !"ADMIN".equals(role))) {
                            JOptionPane.showMessageDialog((Component) frame,
                                    "Veuillez remplir tous les champs, sauf le magasin pour un ADMIN",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Vérifier que l'email n'est pas déjà utilisé par un utilisateur
                            String sql = "SELECT * FROM users WHERE email = ?";
                            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                                stmt.setString(1, email);
                                try (ResultSet rs2 = stmt.executeQuery()) {
                                    if (rs2.next()) {
                                        JOptionPane.showMessageDialog((Component) frame, "Cet e-mail est déjà utilisé",
                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        // Vérifier que l'email est dans la whitelist
                                        String sqlWhitelist = "SELECT * FROM whitelist WHERE email = ?";
                                        try (PreparedStatement stmtWhitelist = conn.prepareStatement(sqlWhitelist)) {
                                            stmtWhitelist.setString(1, email);
                                            try (ResultSet rs3 = stmtWhitelist.executeQuery()) {
                                                if (rs3.next()) {
                                                    JOptionPane.showMessageDialog((Component) frame,
                                                            "Cet e-mail est déjà dans la whitelist",
                                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                                } else {
                                                    // Insertion de l'utilisateur dans la table des utilisateurs
                                                    if ("Sélectionnez un magasin".equals(selectedStore)) {
                                                        String sqlInsert = "INSERT INTO whitelist (email, role) VALUES (?, ?)";
                                                        try (PreparedStatement stmtInsert = conn
                                                                .prepareStatement(sqlInsert)) {
                                                            stmtInsert.setString(1, email);
                                                            stmtInsert.setString(2, role);
                                                            int rowsAffected = stmtInsert.executeUpdate();
                                                            if (rowsAffected > 0) {
                                                                JOptionPane.showMessageDialog((Component) frame,
                                                                        "Utilisateur ajouté avec succès",
                                                                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                                                                switch (User.page) {
                                                                    case "whitelistList":
                                                                        new Admin().whitelistList(frame);
                                                                        break;
                                                                    default:
                                                                        break;
                                                                }
                                                            } else {
                                                                JOptionPane.showMessageDialog((Component) frame,
                                                                        "Erreur lors de l'ajout de l'utilisateur",
                                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                            }
                                                        } catch (SQLException e) {
                                                            JOptionPane.showMessageDialog((Component) frame,
                                                                    "Erreur lors de l'ajout de l'utilisateur",
                                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        String sqlInsert = "INSERT INTO whitelist (email, role, store) VALUES (?, ?, ?)";
                                                        try (PreparedStatement stmtInsert = conn
                                                                .prepareStatement(sqlInsert)) {
                                                            stmtInsert.setString(1, email);
                                                            stmtInsert.setString(2, role);
                                                            stmtInsert.setInt(3, storeId);
                                                            int rowsAffected = stmtInsert.executeUpdate();
                                                            if (rowsAffected > 0) {
                                                                JOptionPane.showMessageDialog((Component) frame,
                                                                        "Utilisateur ajouté avec succès",
                                                                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                                                                switch (User.page) {
                                                                    case "whitelistList":
                                                                        new Admin().whitelistList(frame);
                                                                        break;
                                                                    default:
                                                                        break;
                                                                }
                                                                new Admin().whitelistList(frame);
                                                            } else {
                                                                JOptionPane.showMessageDialog((Component) frame,
                                                                        "Erreur lors de l'ajout de l'utilisateur",
                                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                            }
                                                        } catch (SQLException e) {
                                                            JOptionPane.showMessageDialog((Component) frame,
                                                                    "Erreur lors de l'ajout de l'utilisateur",
                                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            } catch (SQLException e) {
                                                JOptionPane.showMessageDialog((Component) frame,
                                                        "Erreur lors de la vérification de l'email dans la whitelist",
                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } catch (SQLException e) {
                                    JOptionPane.showMessageDialog((Component) frame,
                                            "Erreur lors de la vérification de l'email dans la table des utilisateurs",
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog((Component) frame,
                            "Une erreur s'est produite lors de la récupération des magasins",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog((Component) frame,
                        "Une erreur s'est produite lors de la récupération des magasins",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog((Component) frame,
                    "Une erreur s'est produite lors de la connexion à la base de donnée",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void editUser(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sql = "SELECT * FROM users";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    JComboBox<String> userList = new JComboBox<>();
                    userList.addItem("Sélectionnez un utilisateur");
                    while (rs.next()) {
                        userList.addItem(
                                rs.getString("id") + " - " + rs.getString("pseudo") + " - " + rs.getString("email"));
                    }
                    Object[] message = {
                            "Utilisateur:", userList,
                    };
                    int option = JOptionPane.showConfirmDialog((Component) frame, message,
                            "Modifier le magasin d'un utilisateur",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        String selectedUser = userList.getSelectedItem().toString();
                        if ("Sélectionnez un utilisateur".equals(selectedUser)) {
                            JOptionPane.showMessageDialog((Component) frame, "Veuillez sélectionner un utilisateur",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else {
                            String[] idUser = selectedUser.split(" - ");
                            int userId = Integer.parseInt(idUser[0]);
                            if (userId == 0) {
                                JOptionPane.showMessageDialog((Component) frame, "Veuillez sélectionner un utilisateur",
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JComboBox<String> optionsEditUser = new JComboBox<>(new String[] { "Modifier le pseudo",
                                        "Modifier l'e-mail", "Modifier le mot de passe",
                                        "Modifier le rôle et le magasin" });
                                Object[] messageEditUser = { "Action:", optionsEditUser };
                                int option2EditUser = JOptionPane.showConfirmDialog((Component) frame, messageEditUser,
                                        "Modifier un utilisateur",
                                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                if (option2EditUser == JOptionPane.OK_OPTION) {
                                    String action = (String) optionsEditUser.getSelectedItem();
                                    switch (action) {
                                        case "Modifier le pseudo":
                                            sql = "SELECT * FROM users WHERE id = ?";
                                            try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                                                stmt2.setInt(1, userId);
                                                try (ResultSet rs2 = stmt2.executeQuery()) {
                                                    if (rs2.next()) {
                                                        String currentPseudo = rs2.getString("pseudo");
                                                        String newPseudo = JOptionPane.showInputDialog(
                                                                (Component) frame,
                                                                "Nouveau pseudo", currentPseudo);
                                                        if (newPseudo != null) {
                                                            sql = "SELECT * FROM users WHERE pseudo = ?";
                                                            try (PreparedStatement stmt3 = conn.prepareStatement(sql)) {
                                                                stmt3.setString(1, newPseudo);
                                                                try (ResultSet rs3 = stmt3.executeQuery()) {
                                                                    if (rs3.next()) {
                                                                        JOptionPane.showMessageDialog((Component) frame,
                                                                                "Ce pseudo est déjà utilisé",
                                                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                    } else {
                                                                        sql = "UPDATE users SET pseudo = ? WHERE id = ?";
                                                                        try (PreparedStatement stmt4 = conn
                                                                                .prepareStatement(sql)) {
                                                                            stmt4.setString(1, newPseudo);
                                                                            stmt4.setInt(2, userId);
                                                                            int rowsAffected = stmt4.executeUpdate();
                                                                            if (rowsAffected > 0) {
                                                                                JOptionPane.showMessageDialog(
                                                                                        (Component) frame,
                                                                                        "Pseudo modifié avec succès",
                                                                                        "Succès",
                                                                                        JOptionPane.INFORMATION_MESSAGE);
                                                                                new User().userList(frame);
                                                                            } else {
                                                                                JOptionPane.showMessageDialog(
                                                                                        (Component) frame,
                                                                                        "Erreur lors de la modification du pseudo",
                                                                                        "Erreur",
                                                                                        JOptionPane.ERROR_MESSAGE);
                                                                            }
                                                                        } catch (SQLException e) {
                                                                            JOptionPane.showMessageDialog(
                                                                                    (Component) frame,
                                                                                    "Erreur lors de la modification du pseudo",
                                                                                    "Erreur",
                                                                                    JOptionPane.ERROR_MESSAGE);
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                } catch (SQLException e) {
                                                                    JOptionPane.showMessageDialog((Component) frame,
                                                                            "Erreur lors de la modification du pseudo",
                                                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                    e.printStackTrace();
                                                                }
                                                            } catch (SQLException e) {
                                                                JOptionPane.showMessageDialog((Component) frame,
                                                                        "Erreur lors de la modification du pseudo",
                                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            JOptionPane.showMessageDialog((Component) frame,
                                                                    "Veuillez saisir un pseudo",
                                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        case "Modifier l'e-mail":
                                            sql = "SELECT * FROM users WHERE id = ?";
                                            try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                                                stmt2.setInt(1, userId);
                                                try (ResultSet rs2 = stmt2.executeQuery()) {
                                                    if (rs2.next()) {
                                                        String currentEmail = rs2.getString("email");
                                                        String newEmail = JOptionPane.showInputDialog((Component) frame,
                                                                "Nouvel e-mail", currentEmail);
                                                        if (newEmail != null) {
                                                            sql = "SELECT * FROM users WHERE email = ?";
                                                            try (PreparedStatement stmt3 = conn.prepareStatement(sql)) {
                                                                stmt3.setString(1, newEmail);
                                                                try (ResultSet rs3 = stmt3.executeQuery()) {
                                                                    if (rs3.next()) {
                                                                        JOptionPane.showMessageDialog((Component) frame,
                                                                                "Cet e-mail est déjà utilisé",
                                                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                    } else {
                                                                        sql = "UPDATE users SET email = ? WHERE id = ?";
                                                                        try (PreparedStatement stmt4 = conn
                                                                                .prepareStatement(sql)) {
                                                                            stmt4.setString(1, newEmail);
                                                                            stmt4.setInt(2, userId);
                                                                            int rowsAffected = stmt4.executeUpdate();
                                                                            if (rowsAffected > 0) {
                                                                                JOptionPane.showMessageDialog(
                                                                                        (Component) frame,
                                                                                        "E-Mail modifié avec succès",
                                                                                        "Succès",
                                                                                        JOptionPane.INFORMATION_MESSAGE);
                                                                                new User().userList(frame);
                                                                            } else {
                                                                                JOptionPane.showMessageDialog(
                                                                                        (Component) frame,
                                                                                        "Erreur lors de la modification de l'e-mail",
                                                                                        "Erreur",
                                                                                        JOptionPane.ERROR_MESSAGE);
                                                                            }
                                                                        } catch (SQLException e) {
                                                                            JOptionPane.showMessageDialog(
                                                                                    (Component) frame,
                                                                                    "Erreur lors de la modification de l'e-mail",
                                                                                    "Erreur",
                                                                                    JOptionPane.ERROR_MESSAGE);
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                } catch (SQLException e) {
                                                                    JOptionPane.showMessageDialog((Component) frame,
                                                                            "Erreur lors de la modification de l'e-mail",
                                                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                    e.printStackTrace();
                                                                }
                                                            } catch (SQLException e) {
                                                                JOptionPane.showMessageDialog((Component) frame,
                                                                        "Erreur lors de la modification de l'e-mail",
                                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            JOptionPane.showMessageDialog((Component) frame,
                                                                    "Veuillez saisir un e-mail",
                                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        case "Modifier le mot de passe":
                                            JPasswordField passwordField = new JPasswordField(10);
                                            JPasswordField confirmPasswordField = new JPasswordField(10);
                                            passwordField.setEchoChar('●'); // Masque le texte saisi dans le champ de
                                                                            // mot de
                                                                            // passe
                                            confirmPasswordField.setEchoChar('●'); // Masque le texte saisi dans le
                                                                                   // champ de
                                                                                   // mot de passe
                                            Object[] message2 = { "Code:", passwordField, "Confirmer le code:",
                                                    confirmPasswordField };
                                            int option2 = JOptionPane.showConfirmDialog((Component) frame, message2,
                                                    "Modifier le mot de passe",
                                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                            if (option2 == JOptionPane.OK_OPTION) {
                                                char[] password = passwordField.getPassword();
                                                char[] confirmPassword = confirmPasswordField.getPassword();
                                                if (new String(password).equals(new String(confirmPassword))) {
                                                    String hashedPassword = BCrypt.hashpw(new String(password),
                                                            "$2a$10$VM/GfVScMMgdLVtHwABv6u");
                                                    sql = "UPDATE users SET password = ? WHERE id = ?";
                                                    try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                                                        stmt2.setString(1, hashedPassword);
                                                        stmt2.setInt(2, userId);
                                                        int rowsAffected = stmt2.executeUpdate();
                                                        if (rowsAffected > 0) {
                                                            JOptionPane.showMessageDialog((Component) frame,
                                                                    "Mot de passe modifié avec succès",
                                                                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                                                            new User().userList(frame);
                                                        } else {
                                                            JOptionPane.showMessageDialog((Component) frame,
                                                                    "Erreur lors de la modification du mot de passe",
                                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    } catch (SQLException e) {
                                                        JOptionPane.showMessageDialog((Component) frame,
                                                                "Erreur lors de la modification du mot de passe",
                                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    JOptionPane.showMessageDialog((Component) frame,
                                                            "Les mots de passe ne correspondent pas",
                                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                                }
                                            }
                                            break;
                                        case "Modifier le rôle et le magasin":
                                            sql = "SELECT * FROM store";
                                            try (PreparedStatement stmt3 = conn.prepareStatement(sql)) {
                                                try (ResultSet rs3 = stmt3.executeQuery()) {
                                                    JComboBox<String> roleField = new JComboBox<>(
                                                            new String[] { "EMPLOYEE", "MANAGER", "ADMIN" });
                                                    JComboBox<String> storeList = new JComboBox<>();
                                                    storeList.addItem("Sélectionnez un magasin");
                                                    int currentUserStoreId = 0;
                                                    sql = "SELECT role, store FROM users WHERE id = ?";
                                                    try (PreparedStatement currentStoreStmt = conn
                                                            .prepareStatement(sql)) {
                                                        currentStoreStmt.setInt(1, userId);
                                                        try (ResultSet currentStoreRs = currentStoreStmt
                                                                .executeQuery()) {
                                                            if (currentStoreRs.next()) {
                                                                String currentUserRole = currentStoreRs
                                                                        .getString("role");
                                                                roleField.setSelectedItem(currentUserRole);
                                                                currentUserStoreId = currentStoreRs.getInt("store");
                                                            }
                                                        }
                                                    }
                                                    while (rs3.next()) {
                                                        int storeId = rs3.getInt("id");
                                                        String storeName = rs3.getString("name");
                                                        storeList.addItem(storeId + " - " + storeName);
                                                        if (storeId == currentUserStoreId) {
                                                            storeList.setSelectedItem(storeId + " - " + storeName);
                                                        }
                                                    }
                                                    Object[] message3 = {
                                                            "Rôle:", roleField,
                                                            "Magasin:", storeList,
                                                    };
                                                    int option3 = JOptionPane.showConfirmDialog((Component) frame,
                                                            message3,
                                                            "Modifier le rôle et le magasin d'un utilisateur",
                                                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                                    if (option3 == JOptionPane.OK_OPTION) {
                                                        String role = (String) roleField.getSelectedItem();
                                                        String selectedStore = storeList.getSelectedItem().toString();
                                                        int storeId = 0;
                                                        if (!"Sélectionnez un magasin".equals(selectedStore)) {
                                                            String[] idStore = selectedStore.split(" - ");
                                                            storeId = Integer.parseInt(idStore[0]);
                                                        }
                                                        if ("Sélectionnez un magasin".equals(selectedStore)
                                                                && !"ADMIN".equals(role)) {
                                                            JOptionPane.showMessageDialog((Component) frame,
                                                                    "Le rôle MANAGER ou EMPLOYEE doit être associé à un magasin",
                                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        } else {
                                                            if ("Sélectionnez un magasin".equals(selectedStore)) {
                                                                sql = "UPDATE users SET role = ?, store = NULL WHERE id = ?";
                                                            } else {
                                                                sql = "UPDATE users SET role = ?, store = ? WHERE id = ?";
                                                            }
                                                            try (PreparedStatement stmt4 = conn.prepareStatement(sql)) {
                                                                stmt4.setString(1, role);
                                                                if (!"Sélectionnez un magasin".equals(selectedStore)) {
                                                                    stmt4.setInt(2, storeId);
                                                                }
                                                                stmt4.setInt(
                                                                        !"Sélectionnez un magasin".equals(selectedStore)
                                                                                ? 3
                                                                                : 2,
                                                                        userId);
                                                                int rowsAffected = stmt4.executeUpdate();
                                                                if (rowsAffected > 0) {
                                                                    JOptionPane.showMessageDialog((Component) frame,
                                                                            "Utilisateur modifié avec succès",
                                                                            "Succès",
                                                                            JOptionPane.INFORMATION_MESSAGE);
                                                                    new User().userList(frame);
                                                                } else {
                                                                    JOptionPane.showMessageDialog((Component) frame,
                                                                            "Erreur lors de la modification de l'utilisateur",
                                                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                }
                                                            } catch (SQLException e) {
                                                                JOptionPane.showMessageDialog((Component) frame,
                                                                        "Erreur lors de la modification de l'utilisateur",
                                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                } catch (SQLException e) {
                                                    JOptionPane.showMessageDialog((Component) frame,
                                                            "Une erreur s'est produite lors de la récupération des magasins",
                                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                                    e.printStackTrace();
                                                }
                                            } catch (SQLException e) {
                                                JOptionPane.showMessageDialog((Component) frame,
                                                        "Une erreur s'est produite lors de la récupération des magasins",
                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                e.printStackTrace();
                                            }
                                            break;

                                        default:
                                            break;
                                    }
                                }

                            }
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog((Component) frame, "Erreur lors de la récupération des utilisateurs",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog((Component) frame, "Erreur lors de la récupération des utilisateurs",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog((Component) frame, "Erreur lors de la récupération des utilisateurs",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteUser(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sql = "SELECT * FROM users";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    JComboBox<String> userList = new JComboBox<>();
                    userList.addItem("Sélectionnez un utilisateur");
                    while (rs.next()) {
                        userList.addItem(
                                rs.getString("id") + " - " + rs.getString("pseudo") + " - " + rs.getString("email"));
                    }
                    Object[] message = {
                            "Utilisateur:", userList,
                    };
                    int option = JOptionPane.showConfirmDialog((Component) frame, message, "Supprimer un utilisateur",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        String selectedUser = userList.getSelectedItem().toString();
                        if ("Sélectionnez un utilisateur".equals(selectedUser)) {
                            JOptionPane.showMessageDialog((Component) frame, "Veuillez sélectionner un utilisateur",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else {
                            String[] idUser = selectedUser.split(" - ");
                            int userId = Integer.parseInt(idUser[0]);
                            if (userId == 0) {
                                JOptionPane.showMessageDialog((Component) frame, "Veuillez sélectionner un utilisateur",
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                            } else {
                                // Demander une confirmation avant de supprimer
                                int response = JOptionPane.showConfirmDialog((Component) frame,
                                        "Voulez-vous vraiment supprimer cet utilisateur ?",
                                        "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (response == JOptionPane.YES_OPTION) {
                                    // Suppression de l'utilisateur dans la table des utilisateurs
                                    String sqlDelete = "DELETE FROM users WHERE id = ?";
                                    try (PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {
                                        stmtDelete.setInt(1, userId);
                                        int rowsAffected = stmtDelete.executeUpdate();
                                        if (rowsAffected > 0) {
                                            JOptionPane.showMessageDialog((Component) frame,
                                                    "Utilisateur supprimé avec succès",
                                                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                                            new User().userList(frame);
                                        } else {
                                            JOptionPane.showMessageDialog((Component) frame,
                                                    "Erreur lors de la suppression de l'utilisateur",
                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } catch (SQLException e) {
                                        JOptionPane.showMessageDialog((Component) frame,
                                                "Erreur lors de la suppression de l'utilisateur",
                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog((Component) frame, "Erreur lors de la récupération des utilisateurs",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog((Component) frame, "Erreur lors de la récupération des utilisateurs",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog((Component) frame, "Erreur lors de la récupération des utilisateurs",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void profile(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(User.id));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String pseudo = rs.getString("pseudo");
                        String email = rs.getString("email");
                        String role = rs.getString("role");
                        String store = rs.getString("store");
                        String storeName = "";
                        if (store != null) {
                            sql = "SELECT name FROM store WHERE id = ?";
                            try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                                stmt2.setInt(1, Integer.parseInt(store));
                                try (ResultSet rs2 = stmt2.executeQuery()) {
                                    if (rs2.next()) {
                                        storeName = rs2.getString("name");
                                    }
                                }
                            }
                        }
                        Object[] message = {
                                "Pseudo:", pseudo,
                                "E-Mail:", email,
                                "Rôle:", role,
                                "Magasin:", storeName,
                        };
                        String[] options = {  "Modifier", "Fermer", "Supprimer"};
                        int option = JOptionPane.showOptionDialog((Component) frame, message, "Profil",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        if (option == JOptionPane.CANCEL_OPTION) {
                            int response = JOptionPane.showConfirmDialog((Component) frame,
                                    "Voulez-vous vraiment supprimer votre profil ?",
                                    "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (response == JOptionPane.YES_OPTION) {
                                sql = "DELETE FROM users WHERE id = ?";
                                try (PreparedStatement stmt3 = conn.prepareStatement(sql)) {
                                    stmt3.setInt(1, Integer.parseInt(User.id));
                                    int rowsAffected = stmt3.executeUpdate();
                                    if (rowsAffected > 0) {
                                        JOptionPane.showMessageDialog((Component) frame,
                                                "Profil supprimé avec succès",
                                                "Succès", JOptionPane.INFORMATION_MESSAGE);
                                        new User().disconnection(frame);
                                    } else {
                                        JOptionPane.showMessageDialog((Component) frame,
                                                "Erreur lors de la suppression du profil",
                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (SQLException e) {
                                    JOptionPane.showMessageDialog((Component) frame,
                                            "Erreur lors de la suppression du profil",
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                            }
                        } else if (option == JOptionPane.OK_OPTION) {
                            // Modifier le profil
                            JTextField pseudoField = new JTextField(10);
                            JTextField emailField = new JTextField(10);
                            JPasswordField passwordField = new JPasswordField(10);
                            JPasswordField confirmPasswordField = new JPasswordField(10);
                            passwordField.setEchoChar('●'); // Masque le texte saisi dans le champ de mot de passe
                            confirmPasswordField.setEchoChar('●'); // Masque le texte saisi dans le champ de mot de passe
                            pseudoField.setText(pseudo);
                            emailField.setText(email);
                            Object[] message2 = {
                                    "Pseudo:", pseudoField,
                                    "E-Mail:", emailField,
                                    "Nouveau mot de passe:", passwordField,
                                    "Confirmer le mot de passe:", confirmPasswordField,
                            };
                            int option2 = JOptionPane.showConfirmDialog((Component) frame, message2,
                                    "Modifier le profil",
                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (option2 == JOptionPane.OK_OPTION) {
                                String newPseudo = pseudoField.getText();
                                String newEmail = emailField.getText();
                                char[] password = passwordField.getPassword();
                                char[] confirmPassword = confirmPasswordField.getPassword();
                                if (newPseudo.isEmpty() || newEmail.isEmpty()) {
                                    JOptionPane.showMessageDialog((Component) frame,
                                            "Veuillez remplir tous les champs",
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    // Vérifier si le pseudo est déjà utilisé
                                    sql = "SELECT * FROM users WHERE pseudo = ?";
                                    try (PreparedStatement stmt3 = conn.prepareStatement(sql)) {
                                        stmt3.setString(1, newPseudo);
                                        try (ResultSet rs3 = stmt3.executeQuery()) {
                                            if (rs3.next() && !pseudo.equals(newPseudo)) {
                                                JOptionPane.showMessageDialog((Component) frame,
                                                        "Ce pseudo est déjà utilisé",
                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                            } else {
                                                // Vérifier si l'e-mail est déjà utilisé
                                                sql = "SELECT * FROM users WHERE email = ?";
                                                try (PreparedStatement stmt4 = conn.prepareStatement(sql)) {
                                                    stmt4.setString(1, newEmail);
                                                    try (ResultSet rs4 = stmt4.executeQuery()) {
                                                        if (rs4.next() && !email.equals(newEmail)) {
                                                            JOptionPane.showMessageDialog((Component) frame,
                                                                    "Cet e-mail est déjà utilisé",
                                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        } else {
                                                            // Vérifier si les mots de passe correspondent
                                                            if (Arrays.equals(password, confirmPassword)) {
                                                                // Hasher le nouveau mot de passe
                                                                String hashedPassword = BCrypt.hashpw(new String(password), "$2a$10$VM/GfVScMMgdLVtHwABv6u");
                                                                // Mettre à jour le profil
                                                                sql = "UPDATE users SET pseudo = ?, email = ?, password = ? WHERE id = ?";
                                                                try (PreparedStatement updateStmt = conn.prepareStatement(sql)) {
                                                                    updateStmt.setString(1, newPseudo);
                                                                    updateStmt.setString(2, newEmail);
                                                                    updateStmt.setString(3, hashedPassword);
                                                                    updateStmt.setInt(4, Integer.parseInt(User.id));
                                                                    int rowsAffected = updateStmt.executeUpdate();
                                                                    if (rowsAffected > 0) {
                                                                        JOptionPane.showMessageDialog((Component) frame,
                                                                                "Profil mis à jour avec succès vous allez être déconnecté pour vous reconnecter avec vos nouveaux identifiants",
                                                                                "Succès", JOptionPane.INFORMATION_MESSAGE);
                                                                        new User().disconnection(frame);
                                                                    } else {
                                                                        JOptionPane.showMessageDialog((Component) frame,
                                                                                "Erreur lors de la mise à jour du profil",
                                                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                    }
                                                                }
                                                            } else {
                                                                JOptionPane.showMessageDialog((Component) frame,
                                                                        "Les mots de passe ne correspondent pas",
                                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (SQLException e) {
                                        JOptionPane.showMessageDialog((Component) frame,
                                                "Erreur lors de la vérification du pseudo",
                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog((Component) frame,
                    "Erreur lors de la récupération du profil",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}