@ -0,0 +1,34 @@
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIelias {
    public static void main(String[] args) {
        try {
            // URL de l'API
            String url = "http://eliascastel.ddns.net:3001/";

            // Ouverture de la connexion
            HttpURLConnection connexion = (HttpURLConnection) new URL(url).openConnection();
            connexion.setRequestMethod("GET");

            // Lecture de la réponse
            BufferedReader lecteur = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            StringBuilder réponse = new StringBuilder();
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                réponse.append(ligne);
            }
            lecteur.close();

            // Affichage de la réponse
            System.out.println(réponse.toString());

            // Fermeture de la connexion
            connexion.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
