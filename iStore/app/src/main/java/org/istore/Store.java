package org.istore;

import javax.swing.*;
import java.awt.event.*;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Store {
    public void selectStore(Object frame) {
        if ("ADMIN".equals(User.role)) {
            // faire une liste des magasins pour une liste déroulante et l'id du sélect =
            // User.store
            try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
                String sql = "SELECT * FROM store";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    try (ResultSet rs = stmt.executeQuery()) {
                        JComboBox<String> store = new JComboBox<>();
                        while (rs.next()) {
                            store.addItem(rs.getString("id") + " - " + rs.getString("name"));
                        }
                        Object[] message = {
                                "Choisir le magasin:", store
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Choisir le magasin",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            User.store = Integer.parseInt(store.getSelectedItem().toString().split(" - ")[0]);
                            switch (User.page) {
                                case "userStore":
                                    new Store().userStore(frame);
                                    break;
                                case "itemList":
                                    new Store().itemList(frame);
                                    break;
                                default:
                                    break;
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
    }

    private void verifyStore(Object frame) {
        if (User.store == null || User.store == 0) {
            if ("ADMIN".equals(User.role)) {
                selectStore(frame);
            } else {
                JOptionPane.showMessageDialog(null, "Vous n'avez pas de magasin attribué",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    public void userStore(Object frame) {
        User.page = "userStore";
        verifyStore(frame);
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            ((javax.swing.JFrame) frame).getContentPane().removeAll();
            ((javax.swing.JFrame) frame).setLayout(new BorderLayout());
            String sql = "SELECT * FROM store WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, User.store);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    sql = "SELECT * FROM users WHERE store = ?";
                    try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                        stmt2.setInt(1, User.store);
                        try (ResultSet rs2 = stmt2.executeQuery()) {
                            String[] columnNames = { "Pseudo", "E-Mail", "Role" };
                            Object[][] data = new String[0][0];
                            while (rs2.next()) {
                                data = new String[rs2.getRow()][3];
                                data[rs2.getRow() - 1][0] = rs2.getString("pseudo");
                                data[rs2.getRow() - 1][1] = rs2.getString("email");
                                data[rs2.getRow() - 1][2] = rs2.getString("role");
                            }
                            JTable table = new JTable(data, columnNames) {
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            JScrollPane scrollPane = new JScrollPane(table);
                            ((javax.swing.JFrame) frame).add(
                                    new JLabel("Liste personnel du magasin : " + rs.getString("name")),
                                    BorderLayout.NORTH);
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

    public void itemList(Object frame) {
        User.page = "itemList";
        verifyStore(frame);
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            ((javax.swing.JFrame) frame).getContentPane().removeAll();
            ((javax.swing.JFrame) frame).setLayout(new BorderLayout());
            String sql = "SELECT * FROM store WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, User.store);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storeName = rs.getString("name");
                        sql = "SELECT * FROM items WHERE inventory = ?";
                        try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                            stmt2.setInt(1, User.store);
                            try (ResultSet rs2 = stmt2.executeQuery()) {
                                List<String[]> dataList = new ArrayList<>(); 
                                while (rs2.next()) {
                                    String[] itemData = new String[3];
                                    itemData[0] = rs2.getString("name");
                                    itemData[1] = rs2.getFloat("price") + " €";
                                    itemData[2] = rs2.getString("stock");
                                    dataList.add(itemData); // Ajouter les données à la liste
                                }

                                // Convertir la liste en un tableau pour l'affichage dans le tableau
                                Object[][] data = new Object[dataList.size()][3];
                                for (int i = 0; i < dataList.size(); i++) {
                                    data[i] = dataList.get(i);
                                }

                                String[] columnNames = { "Nom", "Prix", "Quantité" };
                                JTable table = new JTable(data, columnNames) {
                                    public boolean isCellEditable(int row, int column) {
                                        return false;
                                    }
                                };
                                JScrollPane scrollPane = new JScrollPane(table);
                                ((javax.swing.JFrame) frame).add(new JLabel("Inventaire du magasin : " + storeName),
                                        BorderLayout.NORTH);
                                Button selectActionItem = new Button("Effectuer une action sur l'inventaire");
                                selectActionItem.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        new Store().selectActionItem(frame);
                                    }
                                });
                                ((javax.swing.JFrame) frame).add(selectActionItem, BorderLayout.SOUTH);
                                ((javax.swing.JFrame) frame).add(scrollPane, BorderLayout.CENTER);
                                ((javax.swing.JFrame) frame).revalidate();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Magasin introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des articles", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void selectActionItem(Object frame) {
        if (!"EMPLOYEE".equals(User.role)) {
            String[] options = { "Ajouter un article", "Modifier un article (nom et prix)", "Modifier le stock",
                    "Supprimer un article" };
            String action = (String) JOptionPane.showInputDialog(null, "Choisir une action", "Action",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (action == null) {
                return;
            }
            switch (action) {
                case "Ajouter un article":
                    new Store().addItem(frame);
                    break;
                case "Modifier un article (nom et prix)":
                    new Store().updateItem(frame);
                    break;
                case "Modifier le stock":
                    new Store().updateStock(frame);
                    break;
                case "Supprimer un article":
                    // new Store().deleteItem(frame);
                    break;
                default:
                    break;
            }
        } else {
            new Store().updateStock(frame);
        }
    }

    private void addItem(Object frame) {
        JTextField name = new JTextField(10);
        JTextField price = new JTextField(10);
        JTextField stock = new JTextField(10);
        Object[] message = {
                "Nom:", name,
                "Prix:", price,
                "Stock:", stock
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Ajouter un article",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (name.getText().isEmpty() || price.getText().isEmpty() || stock.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tous les champs sont obligatoires",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            price.setText(price.getText().replace(",", "."));

            if (!price.getText().matches("^[0-9]+(\\.[0-9]+)?$")) {
                JOptionPane.showMessageDialog(null, "Le prix doit être un nombre positif",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!stock.getText().matches("^[0-9]+$")) {
                JOptionPane.showMessageDialog(null, "Le stock doit être un nombre entier positif",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
                String sql = "INSERT INTO items (name, price, stock, inventory) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name.getText());
                    stmt.setFloat(2, Float.parseFloat(price.getText()));
                    stmt.setInt(3, Integer.parseInt(stock.getText()));
                    stmt.setInt(4, User.store);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Article ajouté avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    new Store().itemList(frame);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'article",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void updateItem(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sql = "SELECT * FROM items WHERE inventory = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, User.store);
                try (ResultSet rs = stmt.executeQuery()) {
                    JComboBox<String> item = new JComboBox<>();
                    while (rs.next()) {
                        item.addItem(rs.getString("id") + " - " + rs.getString("name") + " - " + rs.getString("price") + " €");
                    }
                    Object[] message = {"Choisir l'article:", item};
                    int option = JOptionPane.showConfirmDialog(null, message, "Choisir l'article", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String[] itemData = item.getSelectedItem().toString().split(" - ");
                        String itemName = itemData[1];
                        String itemPrice = itemData[2].replace(" €", "");
                        
                        JTextField name = new JTextField(itemName);
                        JTextField price = new JTextField(itemPrice);
                        Object[] message2 = {"Nom:", name, "Prix:", price};
                        int option2 = JOptionPane.showConfirmDialog(null, message2, "Modifier l'article", JOptionPane.OK_CANCEL_OPTION);
                        if (option2 == JOptionPane.OK_OPTION) {
                            if (name.getText().isEmpty() || price.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Tous les champs sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            price.setText(price.getText().replace(",", "."));
                            if (!price.getText().matches("^[0-9]+(\\.[0-9]+)?$")) {
                                JOptionPane.showMessageDialog(null, "Le prix doit être un nombre positif", "Erreur", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            sql = "UPDATE items SET name = ?, price = ? WHERE id = ?";
                            try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                                stmt2.setString(1, name.getText());
                                stmt2.setFloat(2, Float.parseFloat(price.getText()));
                                stmt2.setInt(3, Integer.parseInt(itemData[0]));
                                stmt2.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Article modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                                new Store().itemList(frame);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des articles", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStock(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sql = "SELECT * FROM items WHERE inventory = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, User.store);
                try (ResultSet rs = stmt.executeQuery()) {
                    JComboBox<String> item = new JComboBox<>();
                    while (rs.next()) {
                        item.addItem(rs.getString("id") + " - " + rs.getString("name") + " - " + rs.getString("stock"));
                    }
                    Object[] message = {"Choisir l'article:", item};
                    int option = JOptionPane.showConfirmDialog(null, message, "Choisir l'article", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String[] itemData = item.getSelectedItem().toString().split(" - ");
                        String itemStock = itemData[2];
                        
                        JTextField stock = new JTextField(itemStock);
                        Object[] message2 = {"Stock:", stock};
                        int option2 = JOptionPane.showConfirmDialog(null, message2, "Modifier le stock", JOptionPane.OK_CANCEL_OPTION);
                        if (option2 == JOptionPane.OK_OPTION) {
                            if (stock.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Le stock est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (!stock.getText().matches("^[0-9]+$")) {
                                JOptionPane.showMessageDialog(null, "Le stock doit être un nombre entier positif", "Erreur", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            sql = "UPDATE items SET stock = ? WHERE id = ?";
                            try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                                stmt2.setInt(1, Integer.parseInt(stock.getText()));
                                stmt2.setInt(2, Integer.parseInt(itemData[0]));
                                stmt2.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Stock modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                                new Store().itemList(frame);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des articles", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteItem(Object frame) {
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String sql = "SELECT * FROM items WHERE inventory = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, User.store);
                try (ResultSet rs = stmt.executeQuery()) {
                    JComboBox<String> item = new JComboBox<>();
                    while (rs.next()) {
                        item.addItem(rs.getString("id") + " - " + rs.getString("name"));
                    }
                    Object[] message = {"Choisir l'article:", item};
                    int option = JOptionPane.showConfirmDialog(null, message, "Choisir l'article", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        int itemId = Integer.parseInt(item.getSelectedItem().toString().split(" - ")[0]);
                        sql = "DELETE FROM items WHERE id = ?";
                        try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                            stmt2.setInt(1, itemId);
                            stmt2.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Article supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            new Store().itemList(frame);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des articles", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

}