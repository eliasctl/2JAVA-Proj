import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import func;

public class test {
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
        /* Ajouter des composants du menu1 */
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
        /* Ajouter des composants du menu2 */
        JMenuItem annuler = new JMenuItem("Annuler");
        menu2.add(annuler);
        JMenuItem copier = new JMenuItem("Copier");
        menu2.add(copier);
        JMenuItem coller = new JMenuItem("Coller");
        menu2.add(coller);
        /* Ajouter le menu2 à la bar du menu */
        menuBar.add(menu2);
        /* Ajouter la bar du menu à la frame */
        frame.setJMenuBar(menuBar);
        /* Cacher le menu */
        menuBar.setVisible(false);

        /* Ajouter un bouton de connexion dans le conteneur qui quand on clique dessus appel la connection dans func*/
        JButton connexionButton = new JButton("Connexion");

        container.add(connexionButton);

        /* Clic sur le choix Démarrer du menu fichier */
        demarrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Bonjour");
            }
        });
        /* Clic sur le choix copier */
        copier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Nom d'utilisateur ou mot de passe incorrect", "Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
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

        connexionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField usernameField = new JTextField(10);
                JPasswordField passwordField = new JPasswordField(10);
                passwordField.setEchoChar('*'); // Masque le texte saisi dans le champ de mot de passe

                // appel de la fonction roleToInt
                int role = new func().roleToInt("admin");
                System.out.println("Role: " + role);

                Object[] message = {
                        "Username:", usernameField,
                        "Password:", passwordField
                };
                int option = JOptionPane.showOptionDialog(frame, message, "Login", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new String[] { "Login", "Inscription", "Code oublié" },
                        null);
                if (option == JOptionPane.OK_OPTION) {
                    String username = usernameField.getText();
                    char[] password = passwordField.getPassword();

                    // Ici vous pouvez traiter le nom d'utilisateur et le mot de passe saisis
                    System.out.println("Username: " + username);
                    System.out.println("Password: " + new String(password));
                    menuBar.setVisible(true);
                    container.removeAll();
                } else if (option == 1) {
                    // Gérer l'inscription
                    System.out.println("Inscription");
                } else if (option == 2) {
                    // Gérer le code oublié
                    System.out.println("Code oublié");
                } else {
                    System.out.println("Login canceled");
                }
            }
        });

        frame.setSize(1440, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}