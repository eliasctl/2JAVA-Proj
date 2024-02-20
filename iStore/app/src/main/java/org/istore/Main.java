package org.istore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

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
        JMenu menu1 = new JMenu("Magasin");
        /* Ajouter les composants du menu1 */
        JMenuItem demarrer = new JMenuItem("Démarrer");
        menu1.add(demarrer);
        JMenuItem fin = new JMenuItem("Fin");
        menu1.add(fin);
        JMenuItem userStore = new JMenuItem("Personnel du magasin");
        menu1.add(userStore);
        /* Ajouer le menu1 à la bar du menu */
        menuBar.add(menu1);

        /* Créer le menu User */
        JMenu menuUser = new JMenu("Utilisateur");
        /* Ajouter les composants du menuUser */
        JMenuItem profile = new JMenuItem("Mon Profil");
        menuUser.add(profile);
        JMenuItem users = new JMenuItem("Liste des Utilisateurs");
        menuUser.add(users);
        JMenuItem disconnection = new JMenuItem("Deconnexion");
        menuUser.add(disconnection);
        /* Ajouter le menuUser à la bar du menu */
        menuBar.add(menuUser);

        /* Ajouter le menuAdmin */
        JMenu menuAdmin = new JMenu("Administration");
        /* Ajouter les composants du menuAdmin */
        JMenuItem whitelistList = new JMenuItem("Liste des Whitelists");
        menuAdmin.add(whitelistList);
        JMenuItem storeList = new JMenuItem("Liste des Magasins");
        menuAdmin.add(storeList);
        JMenuItem changeStore = new JMenuItem("Changer de Magasin");
        menuAdmin.add(changeStore);
        JMenuItem inventoryList = new JMenuItem("Voir les inventaires");
        menuAdmin.add(inventoryList);
        /* Ajouter le menuAdmin à la bar du menu */
        menuBar.add(menuAdmin);
        /* Ajouter la bar du menu à la frame */
        frame.setJMenuBar(menuBar);
        /* Cacher le menu */
        menuAdmin.setVisible(false);
        menuBar.setVisible(false);

        /* Ajouter un bouton de connexion qui appel la fonction de connexion */
        JButton connectionButton = new JButton("Connexion");
        container.add(connectionButton);
        connectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new User().connection(frame);
            }
        });

        /* Ajouter un bouton d'inscription qui appel la fonction d'inscription' */
        JButton registrationButton = new JButton("Inscription");
        container.add(registrationButton);
        registrationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new User().registration(frame);
            }
        });

        /* Ajouter un bouton pour voir le personnel du magasin */
        userStore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Store().userStore(frame);
            }
        });

        profile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //new User().profile(frame);
            }
        });

        disconnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new User().disconnection(frame);
            }
        });

        users.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new User().userList(frame);
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

        whitelistList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Admin().whitelistList(frame);
            }
        });

        storeList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Admin().storeList(frame);
            }
        });

        changeStore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Store().selectStore(frame);
            }
        });

        inventoryList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Admin().inventoryList(frame);
            }
        });

        frame.setSize(1440, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Cliquer sur le bouton de connexion
        new Conf().selectTataBase();
        connectionButton.doClick();
    }
}
