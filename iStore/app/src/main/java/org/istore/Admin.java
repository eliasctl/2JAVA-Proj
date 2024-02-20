package org.istore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.*;
import java.awt.event.*;

public class Admin {
    public void storeList(Object frame) {
        User.page = "storeList";
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            ((javax.swing.JFrame) frame).getContentPane().removeAll();
            Button selectActionStore = new Button("Effectuer une action pour un magasin");
            selectActionStore.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new Admin().selectActionStore(frame);
                }
            });
            ((javax.swing.JFrame) frame).setLayout(new BorderLayout());
            String sql = "SELECT * FROM store";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    String[] columnNames = { "ID", "Nom" };
                    String[][] data = new String[0][0];
                    while (rs.next()) {
                        String id = rs.getString("id");
                        String name = rs.getString("name");
                        String[][] newData = new String[data.length + 1][2]; // Correction de la taille du tableau
                        for (int i = 0; i < data.length; i++) {
                            for (int j = 0; j < 2; j++) { // Correction de la taille du tableau
                                newData[i][j] = data[i][j];
                            }
                        }
                        data = newData; // Mise à jour de la référence du tableau
                        data[data.length - 1][0] = id; // Utilisation de data.length pour ajouter dans la nouvelle ligne
                        data[data.length - 1][1] = name; // Utilisation de data.length pour ajouter dans la nouvelle
                                                         // ligne
                    }
                    JTable table = new JTable(data, columnNames) {
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    JScrollPane scrollPane = new JScrollPane(table);
                    ((javax.swing.JFrame) frame).add(new JLabel("Liste des magasins"), BorderLayout.NORTH);
                    ((javax.swing.JFrame) frame).add(selectActionStore, BorderLayout.SOUTH);
                    ((javax.swing.JFrame) frame).add(scrollPane, BorderLayout.CENTER);
                    ((javax.swing.JFrame) frame).revalidate();

                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des Magasins",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void selectActionStore(Object frame) {
        String[] options = { "Ajouter", "Modifier", "Supprimer" };
        int response = JOptionPane.showOptionDialog(null, "Que voulez-vous faire ?", "Action sur le magasin",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (response) {
            case 0:
                new Admin().addStore(frame);
                break;
            case 1:
                new Admin().modifyStore(frame);
                break;
            case 2:
                new Admin().deleteStore(frame);
                break;
            default:
                break;
        }
    }

    private void addStore(Object frame) {
        JTextField name = new JTextField(10);
        Object[] message = { "Nom du magasin:", name };
        int option = JOptionPane.showConfirmDialog(null, message, "Ajouter un magasin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
                // vérifier si le magasin existe déjà
                String sqlCheck = "SELECT * FROM store WHERE name = ?";
                try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
                    stmtCheck.setString(1, name.getText());
                    try (ResultSet rs = stmtCheck.executeQuery()) {
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "Le magasin existe déjà",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            String sql = "INSERT INTO store (name) VALUES (?)";
                            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                                stmt.setString(1, name.getText());
                                stmt.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Magasin ajouté avec succès",
                                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                                new Admin().storeList(frame);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du magasin",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void modifyStore(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sqlSelect = "SELECT * FROM store";
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    JComboBox<String> storeList = new JComboBox<String>();
                    while (rs.next()) {
                        storeList.addItem(rs.getString("id") + " - " + rs.getString("name"));
                    }
                    Object[] message = { "ID du magasin:", storeList };
                    int option = JOptionPane.showConfirmDialog(null, message, "Modifier un magasin",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String selectedStore = storeList.getSelectedItem().toString();
                        String[] idName = selectedStore.split(" - ");
                        JTextField name = new JTextField(10);
                        name.setText(idName[1]); // Remplir le champ de texte avec le nom actuel du magasin
                        Object[] message2 = { "Nouveau nom du magasin:", name };
                        int option2 = JOptionPane.showConfirmDialog(null, message2, "Modifier un magasin",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (option2 == JOptionPane.OK_OPTION) {
                            try (Connection conn2 = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER,
                                    Conf.DB_PASSWORD)) {
                                // vérifier si le magasin existe
                                String sqlCheck = "SELECT * FROM store WHERE id = ?";
                                try (PreparedStatement stmtCheck = conn2.prepareStatement(sqlCheck)) {
                                    stmtCheck.setString(1, idName[0]);
                                    try (ResultSet rsCheck = stmtCheck.executeQuery()) {
                                        if (!rsCheck.next()) {
                                            JOptionPane.showMessageDialog(null, "Le magasin n'existe pas",
                                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                                            return;
                                        } else {
                                            String sqlUpdate = "UPDATE store SET name = ? WHERE id = ?";
                                            try (PreparedStatement stmtUpdate = conn2.prepareStatement(sqlUpdate)) {
                                                stmtUpdate.setString(1, name.getText());
                                                stmtUpdate.setString(2, idName[0]);
                                                stmtUpdate.executeUpdate();
                                                JOptionPane.showMessageDialog(null, "Magasin modifié avec succès",
                                                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                                                new Admin().storeList(frame);
                                            }
                                        }
                                    }
                                }
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "Erreur lors de la modification du magasin",
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de la récupération des magasins",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteStore(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sqlSelect = "SELECT * FROM store";
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    JComboBox<String> storeList = new JComboBox<String>();
                    while (rs.next()) {
                        storeList.addItem(rs.getString("id") + " - " + rs.getString("name"));
                    }
                    Object[] message = { "ID du magasin:", storeList };
                    int option = JOptionPane.showConfirmDialog(null, message, "Supprimer un magasin",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String[] idName = storeList.getSelectedItem().toString().split(" - ");
                        try (Connection conn2 = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER,
                                Conf.DB_PASSWORD)) {
                            // Vérifier si le magasin existe
                            String sqlCheck = "SELECT * FROM store WHERE id = ?";
                            try (PreparedStatement stmtCheck = conn2.prepareStatement(sqlCheck)) {
                                stmtCheck.setString(1, idName[0]);
                                try (ResultSet rsCheck = stmtCheck.executeQuery()) {
                                    if (!rsCheck.next()) {
                                        JOptionPane.showMessageDialog(null, "Le magasin n'existe pas",
                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    } else {
                                        String sqlDelete = "DELETE FROM store WHERE id = ?";
                                        try (PreparedStatement stmtDelete = conn2.prepareStatement(sqlDelete)) {
                                            stmtDelete.setString(1, idName[0]);
                                            stmtDelete.executeUpdate();
                                            JOptionPane.showMessageDialog(null, "Magasin supprimé avec succès",
                                                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                                            new Admin().storeList(frame);
                                        }
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du magasin",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
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

    public void whitelistList(Object frame) {
        User.page = "whitelistList";
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            ((javax.swing.JFrame) frame).getContentPane().removeAll();
            Button selectActionWhitelist = new Button("Effectuer une modification sur la whitelist");
            selectActionWhitelist.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new Admin().selectActionWhitelist(frame);
                }
            });
            ((javax.swing.JFrame) frame).setLayout(new BorderLayout());
            String sql = "SELECT * FROM whitelist";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    String[] columnNames = { "E-Mail", "Rôle", "Magasin" };
                    String[][] data = new String[0][0];
                    while (rs.next()) {
                        String email = rs.getString("email");
                        String role = rs.getString("role");
                        String store = rs.getString("store");
                        String[][] newData = new String[data.length + 1][3]; // Correction de la taille du tableau
                        for (int i = 0; i < data.length; i++) {
                            for (int j = 0; j < 3; j++) { // Correction de la taille du tableau
                                newData[i][j] = data[i][j];
                            }
                        }
                        data = newData; // Mise à jour de la référence du tableau
                        data[data.length - 1][0] = email; // Utilisation de data.length pour ajouter dans la nouvelle
                                                          // ligne
                        data[data.length - 1][1] = role; // Utilisation de data.length pour ajouter dans la nouvelle
                                                         // ligne
                        data[data.length - 1][2] = store; // Utilisation de data.length pour ajouter dans la nouvelle
                                                          // ligne
                    }
                    JTable table = new JTable(data, columnNames) {
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    JScrollPane scrollPane = new JScrollPane(table);
                    ((javax.swing.JFrame) frame).add(new JLabel("Whitelist"), BorderLayout.NORTH);
                    ((javax.swing.JFrame) frame).add(selectActionWhitelist, BorderLayout.SOUTH);
                    ((javax.swing.JFrame) frame).add(scrollPane, BorderLayout.CENTER);
                    ((javax.swing.JFrame) frame).revalidate();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des listes blanches",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void selectActionWhitelist(Object frame) {
        String[] options = { "Ajouter", "Supprimer" };
        int response = JOptionPane.showOptionDialog(null, "Que voulez-vous faire ?", "Action sur la liste blanche",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (response) {
            case 0:
                new User().addUser(frame);
                break;
            case 1:
                new Admin().deleteWhitelist(frame);
                break;
            default:
                break;
        }
    }

    private void deleteWhitelist(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sqlSelect = "SELECT * FROM whitelist";
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    JComboBox<String> userList = new JComboBox<String>();
                    userList.addItem("Sélectionner un utilisateur");
                    while (rs.next()) {
                        userList.addItem(rs.getString("id") + " - " + rs.getString("email"));
                    }
                    Object[] message = { "ID de l'utilisateur:", userList };
                    int option = JOptionPane.showConfirmDialog(null, message,
                            "Supprimer un utilisateur de la whitelist",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (userList.getSelectedItem().toString().equals("Sélectionner un utilisateur")) {
                            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un utilisateur",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String[] idEmail = userList.getSelectedItem().toString().split(" - ");
                        try (Connection conn2 = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER,
                                Conf.DB_PASSWORD)) {
                            String sqlDelete = "DELETE FROM whitelist WHERE id = ?";
                            try (PreparedStatement stmtDelete = conn2.prepareStatement(sqlDelete)) {
                                stmtDelete.setString(1, idEmail[0]);
                                stmtDelete.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Utilisateur supprimé avec succès",
                                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                                new Admin().whitelistList(frame);
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de l'utilisateur",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des utilisateurs",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des utilisateurs",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des utilisateurs",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void inventoryList(Object frame) {
        User.page = "inventoryList";
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            ((javax.swing.JFrame) frame).getContentPane().removeAll();
            ((javax.swing.JFrame) frame).setLayout(new BorderLayout());
            String sql = "SELECT DISTINCT inventory FROM items";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    // faire un tableau avec les inventaires et le nombre d'articles à l'intérieur
                    String[] columnNames = { "Inventaire", "Nombre d'articles" };
                    String[][] data = new String[0][0];
                    while (rs.next()) {
                        String inventory = rs.getString("inventory");
                        sql = "SELECT name FROM store WHERE id = ?";
                        try (PreparedStatement stmtStore = conn.prepareStatement(sql)) {
                            stmtStore.setString(1, inventory);
                            try (ResultSet rsStore = stmtStore.executeQuery()) {
                                if (rsStore.next()) {
                                    String store = rsStore.getString("name");
                                    String sqlCount = "SELECT COUNT(*) FROM items WHERE inventory = ?";
                                    try (PreparedStatement stmtCount = conn.prepareStatement(sqlCount)) {
                                        stmtCount.setString(1, inventory);
                                        try (ResultSet rsCount = stmtCount.executeQuery()) {
                                            if (rsCount.next()) {
                                                String count = rsCount.getString(1);
                                                String[][] newData = new String[data.length + 1][2];
                                                for (int i = 0; i < data.length; i++) {
                                                    for (int j = 0; j < 2; j++) { // Correction de la taille du tableau
                                                        newData[i][j] = data[i][j];
                                                    }
                                                }
                                                data = newData; // Mise à jour de la référence du tableau
                                                data[data.length - 1][0] = store;
                                                data[data.length - 1][1] = count;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    JTable table = new JTable(data, columnNames) {
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    JScrollPane scrollPane = new JScrollPane(table);
                    ((javax.swing.JFrame) frame).add(new JLabel("Inventaires"), BorderLayout.NORTH);
                    // Ajouter un bouton pour effectuer une action sur un inventaire
                    Button selectActionInventory = new Button("Supprimer un inventaire");
                    selectActionInventory.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            new Admin().deleteInventory(frame);
                        }
                    });
                    ((javax.swing.JFrame) frame).add(selectActionInventory, BorderLayout.SOUTH);
                    ((javax.swing.JFrame) frame).add(scrollPane, BorderLayout.CENTER);
                    ((javax.swing.JFrame) frame).revalidate();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des inventaires",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteInventory(Object frame) {
        //
    }
}
