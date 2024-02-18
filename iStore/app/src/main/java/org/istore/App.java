package org.istore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {

    // stocker les informations de l'utilisateur connecté
    public static String id = "";
    public static String pseudo = "";
    public static String eMail = "";
    public static String role = "";
    public static String[][] store = new String[0][0];

    public String getGreeting() {
        return "Welcome on the iStore";
    }

    public static void main(String[] arg) {
        JFrame frame = new JFrame("IStore");

        /* Créer le conteneur */
        JPanel container = new JPanel(new FlowLayout());
        /* Ajout de composants aux conteneurs */
        frame.getContentPane().add(container, BorderLayout.NORTH);

        /* Bar de menu */
        JMenuBar menuBar = new JMenuBar();
        /* Créer le menu1 */
        JMenu menu1 = new JMenu("Fichier");
        /* Ajouter les composants du menu1 */
        JMenuItem demarrer = new JMenuItem("Démarrer");
        menu1.add(demarrer);
        JMenuItem fin = new JMenuItem("Fin");
        menu1.add(fin);
        JMenuItem connexion = new JMenuItem("Connexion");
        menu1.add(connexion);
        /* Ajouer le menu1 à la bar du menu */
        menuBar.add(menu1);
        /* Créer le menu2 */
        JMenu menu2 = new JMenu("Edition");
        /* Ajouter les composants du menu2 */
        JMenuItem annuler = new JMenuItem("Annuler");
        menu2.add(annuler);
        JMenuItem copier = new JMenuItem("Copier");
        menu2.add(copier);
        JMenuItem coller = new JMenuItem("Coller");
        menu2.add(coller);
        /* Ajouter le menu2 à la bar du menu */
        menuBar.add(menu2);
        /* Créer le menu */

        /* Ajouter le menuAdmin */
        JMenu menuAdmin = new JMenu("Administration");
        /* Ajouter les composants du menuAdmin */
        JMenuItem userList = new JMenuItem("Liste des Utilisateurs");
        menuAdmin.add(userList);
        JMenuItem shopList = new JMenuItem("Liste des Magasins");
        menuAdmin.add(shopList);
        /* Ajouter le menuAdmin à la bar du menu */
        menuBar.add(menuAdmin);
        /* Ajouter la bar du menu à la frame */
        frame.setJMenuBar(menuBar);
        /* Cacher le menu */
        menuAdmin.setName("menuAdmin");
        menuAdmin.setVisible(false);
        menuBar.setVisible(false);

        /* Ajouter un bouton de connexion qui appel la fonction de connexion */
        JButton connectionButton = new JButton("Connexion");
        container.add(connectionButton);
        connectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new func().connection(frame);
            }
        });

        /* Ajouter un bouton d'inscription qui appel la fonction d'inscription' */
        JButton registrationButton = new JButton("Inscription");
        container.add(registrationButton);
        registrationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new func().registration(frame);
            }
        });

        fin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                container.removeAll(); // Supprimer tous les composants actuels du conteneur
                JLabel testText = new JLabel("Je suis une fleur");
                container.add(testText);
                frame.revalidate(); // Redessiner la frame pour refléter les modifications
            }
        });

        annuler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // cacher le menu admin
                menuAdmin.setVisible(false);
            }
        });

        frame.setSize(1440, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Cliquer sur le bouton de connexion 
        connectionButton.doClick();
    }
}
