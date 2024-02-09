import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class main {
    public static void main(String[] args) {
        try {
            // Créer l'URL de l'API
            System.setProperty("javax.net.debug", "all");
            URL url = new URL("https://eliascastel.ddns.net:3001");

            // Ouvrir une connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Définir la méthode de requête (GET, POST, etc.)
            conn.setRequestMethod("GET");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// // Informations de connexion à la base de données
// String url = "jdbc:mysql://localhost:8889/java";
// String utilisateur = "root";
// String motDePasse = "root";

// // Établir la connexion
// try {
//     Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);

//     // Créer une déclaration
//     Statement statement = connexion.createStatement();

//     // Exécuter une requête SQL
//     String sql = "SELECT * FROM users";
//     ResultSet resultSet = statement.executeQuery(sql);

//     // Traiter les résultats
//     while (resultSet.next()) {
//         // Lire les données de chaque ligne
//         int id = resultSet.getInt("id");
//         String username = resultSet.getString("pseudo");
//         String email = resultSet.getString("email");
//         // Afficher les données
//         System.out.println("ID: " + id + ", Username: " + username + ", Email: " + email);
//     }

//     // Fermer les ressources
//     resultSet.close();
//     statement.close();
//     connexion.close();
// } catch (SQLException e) {
//     // Gérer les exceptions liées à la connexion ou à l'exécution de la requête
//     e.printStackTrace();
// }