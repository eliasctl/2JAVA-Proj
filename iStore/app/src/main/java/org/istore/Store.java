package org.istore;

import javax.swing.*;

import java.awt.*;
import java.sql.*;

public class Store {
    public void selectStore(Object frame) {
        if ("ADMIN".equals(User.role)) {
            // faire une liste des magasins pour une liste déroulante et l'id du sélect = User.store
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
            }
            else {
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
                            ((javax.swing.JFrame) frame).add(new JLabel("Liste personnel du magasin : " + rs.getString("name")), BorderLayout.NORTH);
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
}