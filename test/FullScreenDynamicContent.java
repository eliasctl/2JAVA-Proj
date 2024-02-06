import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FullScreenDynamicContent {

    private JFrame frame;
    private JPanel contentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new FullScreenDynamicContent().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Page en Plein Écran");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Obtenir la configuration de l'écran principal
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Définir la taille de la fenêtre en fonction de la taille de l'écran
        frame.setSize(gc.getBounds().width, gc.getBounds().height);

        // Créer un panneau pour le contenu modifiable
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Ajouter un bouton pour modifier le contenu
        JButton updateContentButton = new JButton("Modifier le Contenu");
        updateContentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Nom d'utilisateur ou mot de passe incorrect",
                        "Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
                updateContent(); // Appel de la méthode pour mettre à jour le contenu
            }
        });

        // Ajouter le bouton au panneau
        contentPanel.add(updateContentButton, BorderLayout.CENTER);

        // Ajouter le panneau de contenu à la fenêtre
        frame.add(contentPanel);

        // Rendre la fenêtre visible
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre
        frame.setUndecorated(true); // Supprimer la décoration de la fenêtre (barre de titre, etc.)
        frame.setVisible(true);
    }

    private void updateContent() {
        // Méthode appelée lorsqu'on veut mettre à jour le contenu du panneau

        // Par exemple, changer le texte dans le panneau
        JLabel updatedLabel = new JLabel("Contenu Modifié");
        updatedLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        updatedLabel.setHorizontalAlignment(JLabel.CENTER);
        updatedLabel.setVerticalAlignment(JLabel.CENTER);

        // Mettre à jour le panneau de contenu
        contentPanel.removeAll();
        contentPanel.add(updatedLabel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
