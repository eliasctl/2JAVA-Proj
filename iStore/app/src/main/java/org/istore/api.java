package org.istore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    public static void postQuery() {
        try {
            // Créer l'URL de l'API
            URL url = new URL("http://localhost:3002/test");

            // Ouvrir une connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Spécifier la méthode de requête
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Créer la chaîne JSON des données à envoyer
            String jsonInputString = "{\"username\": \"testee\"}";

            // Écrire les données JSON dans le corps de la requête
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

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

    public static void registerUser(String email, String pseudo, String password) {
        try {
            // Créer l'URL de l'API
            URL url = new URL("http://localhost:3002/register");

            // Ouvrir une connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Spécifier la méthode de requête
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Créer la chaîne JSON des données à envoyer
            String jsonInputString = "{\"email\": \"" + email + "\", \"pseudo\": \"" + pseudo + "\", \"password\": \"" + password + "\"}";

            // Écrire les données JSON dans le corps de la requête
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

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
