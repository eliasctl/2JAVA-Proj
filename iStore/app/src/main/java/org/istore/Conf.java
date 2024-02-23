package org.istore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Conf
 * Classe pour la configuration de la base de données
 */
public class Conf {
    // prompt au lancement de l'application pour demander quelle base de données utiliser
    /**
     * prompt au lancement de l'application pour demander quelle base de données utiliser
     */
    public static String[] prompt = { "Choisir une base de données", "Serveur", "Autre"};
    /**
     * db Base de données à utiliser
     */
    public static String db = prompt[1];
    // Constantes de connexion à la base de données
    /**
     * DB_URL URL de la base de données
     */
    public static String DB_URL = "jdbc:mysql://localhost:3306/java";
    /**
     * DB_USER Utilisateur de la base de données
     */
    public static String DB_USER = "root";
    /**
     * DB_PASSWORD Mot de passe de la base de données
     */
    public static String DB_PASSWORD = "root";
    /**
     * selectTataBase Méthode pour sélectionner la base de données à utiliser
     */
    public void selectTataBase() {
        // prompt au lancement de l'application pour demander quelle base de données utiliser
        db = (String) JOptionPane.showInputDialog(null, "Choisir une base de données", "Base de données",
                JOptionPane.QUESTION_MESSAGE, null, prompt, prompt[0]);
        // si db est null on quitte l'application
        if (db == null) {
            System.exit(0);
        }
        // si db est Local on utilise la base de données locale
        if (db.equals("Autre")) {
            // Demander à l'utilisateur de renseigner les informations de connexion à la base de données
            JTextField url = new JTextField(20);
            JTextField user = new JTextField(20);
            JTextField password = new JTextField(20);
            // Création de la fenêtre de dialogue
            Object[] message = { "URL (jdbc:mysql://url:port/base):", url, "Utilisateur:", user, "Mot de passe:", password };
            int option = JOptionPane.showConfirmDialog(null, message, "Connexion à la base de données",
                    JOptionPane.OK_CANCEL_OPTION);
            // Si l'utilisateur clique sur OK, on récupère les informations de connexion
            if (option == JOptionPane.OK_OPTION) {
                // Constantes de connexion à la base de données
                DB_URL = url.getText();
                DB_USER = user.getText();
                DB_PASSWORD = password.getText();
                // Connexion à la base de données
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    System.out.println("Connexion à la base de données réussie");
                } catch (SQLException e) {
                    System.out.println("Erreur de connexion à la base de données");
                    e.printStackTrace();
                }
            } else {
                System.exit(0);
            }
            // Connexion à la base de données
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                System.out.println("Connexion à la base de données réussie");
            } catch (SQLException e) {
                System.out.println("Erreur de connexion à la base de données");
                e.printStackTrace();
            }
        } else if (db.equals("Serveur")) {
            // Constantes de connexion à la base de données
            DB_URL = "jdbc:mysql://adresseDuServeur:Port/NomdeLaBase";
            DB_USER = "NomUtilisateur";
            DB_PASSWORD = "MotDePasse";
            // Connexion à la base de données
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                System.out.println("Connexion à la base de données réussie");
            } catch (SQLException e) {
                System.out.println("Erreur de connexion à la base de données");
                e.printStackTrace();
            }
        } else {
            new Conf().selectTataBase();
        }
    }
}
