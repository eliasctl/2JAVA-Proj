package org.istore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Conf {
    // prompt au lancement de l'application pour demander quelle base de données utiliser
    public static String[] prompt = { "Choisir une base de données", "Local", "Eli" };
    public static String db = prompt[1];
    // Constantes de connexion à la base de données
    public static String DB_URL = "";
    public static String DB_USER = "";
    public static String DB_PASSWORD = "";
    public void selectTataBase() {
        // prompt au lancement de l'application pour demander quelle base de données utiliser
        db = (String) JOptionPane.showInputDialog(null, "Choisir une base de données", "Base de données",
                JOptionPane.QUESTION_MESSAGE, null, prompt, prompt[0]);
        // si db est null on quitte l'application
        if (db == null) {
            System.exit(0);
        }
        // si db est Local on utilise la base de données locale
        if (db.equals("Local")) {
            // Constantes de connexion à la base de données
            DB_URL = "jdbc:mysql://localhost:8889/java";
            DB_USER = "root";
            DB_PASSWORD = "root";
            // Connexion à la base de données
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                System.out.println("Connexion à la base de données réussie");
            } catch (SQLException e) {
                System.out.println("Erreur de connexion à la base de données");
                e.printStackTrace();
            }
        } else if (db.equals("Eli")) {
            // Constantes de connexion à la base de données
            DB_URL = "jdbc:mysql://eliascastel.ddns.net:3306/java";
            DB_USER = "java";
            DB_PASSWORD = "!pn!XrZLgt-pn2RP";
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
