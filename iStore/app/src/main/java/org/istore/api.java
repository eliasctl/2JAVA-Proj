package org.istore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class api {
    public static void testAPI() {
        try {
            // Créer l'URL de l'API
            URL url = new URL("http://eliascastel.ddns.net:3001/");

            // Ouvrir une connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Spécifier la méthode de requête
            conn.setRequestMethod("GET");

            // Lire la réponse de l'API
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Afficher la réponse de l'API
            System.out.println(response.toString());

            // Fermer la connexion
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
