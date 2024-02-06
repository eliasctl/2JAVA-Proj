import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FenetreBtn {
    public static void main(String[] arg) {private void createAndShowGUI() {
    // Si la fenêtre n'a pas encore été créée
    if (frame == null) {
        frame = new JFrame("Écran de Connexion");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));
    }
    else {
        // Supprimer tous les composants de la fenêtre
        frame.getContentPane().removeAll();
        // Changer le titre de la fenêtre
        frame.setTitle("Nouveau Titre");
    }

    // Créer les composants de la fenêtre
    JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
    usernameField = new JTextField();
    JLabel passwordLabel = new JLabel("Mot de passe:");
    passwordField = new JPasswordField();
    JButton loginButton = new JButton("Se Connecter");

    // Ajouter les composants à la fenêtre
    frame.add(usernameLabel);
    frame.add(usernameField);
    frame.add(passwordLabel);
    frame.add(passwordField);
    frame.add(loginButton);

    // Rafraîchir l'affichage de la fenêtre avec les nouveaux composants
    frame.validate();
    frame.repaint();
}
        /* Création des composants */
        JFrame frame = new JFrame("IStore");
        final JButton clic = new JButton("Cliquer"); // peut être suppr
        JPanel panel1 = new JPanel(); // peut être suppr
        JLabel label = new JLabel("Hello World"); // Ajout d'un label initial 
        JPanel centerPanel = new JPanel(new BorderLayout()); // Panel pour centrer les composants
        // créer un contenaire choix entre Flow or grid
        JPanel contenaire = new JPanel(new FlowLayout());

        /* Bar de menu */
        JMenuBar menuBar = new JMenuBar();
        /* différents menus */
        JMenu menu1 = new JMenu("Fichier");
        JMenu menu2 = new JMenu("Edition");
        /* differents choix de chaque menu */
        JMenuItem demarrer = new JMenuItem("Démarrer");
        JMenuItem fin = new JMenuItem("Fin");
        JMenuItem annuler = new JMenuItem("Annuler");
        JMenuItem copier = new JMenuItem("Copier");
        JMenuItem coller = new JMenuItem("Coller");

        /* Ajout de composants aux conteneurs */
        clic.setEnabled(false); // peut être suppr
        panel1.add(clic); // peut être suppr
        frame.getContentPane().add(panel1, BorderLayout.SOUTH); // peut être suppr
        
        centerPanel.add(label, BorderLayout.NORTH); // Ajout du label au centre en haut
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER); // Ajout du panel central
        frame.getContentPane().add(contenaire);
        
        /* Ajouter les choix au menu */
        menu1.add(demarrer);
        menu1.add(fin);
        menu2.add(annuler);
        menu2.add(copier);
        menu2.add(coller);
        /* Ajouter les menu sur la bar de menu */
        menuBar.add(menu1);
        menuBar.add(menu2);
        /* Ajouter la bar du menu à la frame */
        frame.setJMenuBar(menuBar);

        /* Action réalisée par l'IHM */
        /* Clic sur le bouton clic */
        clic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("1 clic");
            }
        });
        /* Clic sur le choix Démarrer du menu fichier */
        demarrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Bonjour");
            }
        });
        /* Clic sur le choix copier */
        copier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Nom d'utilisateur ou mot de passe incorrect",
                        "Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
            }
        });
        /* Clic sur le choix Fin du menu fichier */
        fin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JLabel testText = new JLabel("Je suis une fleur");
                contenaire.add(testText);
                contenaire.setVisible(true);
            }
        });

        frame.setSize(1440, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
