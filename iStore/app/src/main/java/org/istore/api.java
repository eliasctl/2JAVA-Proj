package org.istore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.swing.JOptionPane;

public class api {
    public void testAPI() {
        try {
            // Créer l'URL de l'API
            URL url = new URI("http", null, "localhost", 3002, "/test", null, null).toURL();

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
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void postQuery() {
        try {
            // Créer l'URL de l'API
            URL url = new URI("http", null, "localhost", 3002, "/test", null, null).toURL();

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
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(String email, String pseudo, String password) {
        try {
            // Créer l'URL de l'API
            URL url = new URI("http", null, "localhost", 3002, "/register", null, null).toURL();

            // Ouvrir une connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Spécifier la méthode de requête
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Créer la chaîne JSON des données à envoyer
            String jsonInputString = "{\"email\": \"" + email + "\", \"pseudo\": \"" + pseudo + "\", \"password\": \""
                    + password + "\"}";

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

            // Afficher un message en fonction de la réponse de l'API
            if (conn.getResponseCode() == 200) {
                JOptionPane.showMessageDialog(null, "Inscription réussie", "Inscription",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erreur d'Inscription, veuillez réessayer \\n" + //
                        " ou contacter un Administrateur", "Erreur d'Inscription", JOptionPane.ERROR_MESSAGE);
            }

            // Fermer la connexion
            conn.disconnect();
        } catch (IOException | URISyntaxException e) {
            if (e.toString().contains("401")) {
                JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vous inscire \n avec cet e-mail",
                        "Erreur d'Inscription", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Erreur d'Inscription, veuillez réessayer \n ou contacter un Administrateur",
                        "Erreur d'Inscription", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loginUser(String pseudo, String password) {
        try {
            // Créer l'URL de l'API
            URL url = new URI("http", null, "localhost", 3002, "/login", null, null).toURL();

            // Ouvrir une connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Spécifier la méthode de requête
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Créer la chaîne JSON des données à envoyer
            String jsonInputString = "{\"pseudo\": \"" + pseudo + "\", \"password\": \"" + password + "\"}";

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

            // Afficher un message en fonction de la réponse de l'API
            if (conn.getResponseCode() == 200) {
                JOptionPane.showMessageDialog(null, "Connexion réussie", "Connexion", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erreur de Connexion, veuillez réessayer \\n" + //
                        " ou contacter un Administrateur", "Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
            }

            // Fermer la connexion
            conn.disconnect();
        } catch (IOException | URISyntaxException e) {
            JOptionPane.showMessageDialog(null, "Erreur de Connexion, veuillez réessayer \\n" + //
                    " ou contacter un Administrateur", "Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
        }
    }

}
