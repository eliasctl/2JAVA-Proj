import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnexionApp {

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ConnexionApp().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Écran de Connexion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Se Connecter");

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel()); // Placeholder for spacing
        frame.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                // Vérifier les informations d'identification (à des fins de démonstration, les
                // données sont codées en dur)
                if (checkCredentials(username, new String(password))) {
                    showProfilePage();
                } else {
                    JOptionPane.showMessageDialog(frame, "Nom d'utilisateur ou mot de passe incorrect",
                            "Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean checkCredentials(String username, String password) {
        // À des fins de démonstration, les données sont codées en dur
        return username.equals("utilisateur") && password.equals("motdepasse");
    }

    private void showProfilePage() {

        // Fermer la fenêtre de connexion
        frame.dispose();

        // Afficher la page de profil (à vous de l'implémenter)
        // Vous pouvez créer une nouvelle fenêtre ou changer le contenu de la fenêtre
        // actuelle.
        JFrame profileFrame = new JFrame("Page de Profil");
        profileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        profileFrame.setSize(400, 300);

        // Ajouter des composants à la page de profil ici...

        profileFrame.setVisible(true);
    }
}
