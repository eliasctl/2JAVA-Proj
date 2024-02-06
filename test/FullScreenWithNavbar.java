import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FullScreenWithNavbar {

    private JFrame frame;
    private JPanel contentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new FullScreenWithNavbar().createAndShowGUI();
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

        // Ajouter une barre de navigation à gauche
        JPanel navbarPanel = createNavbar();
        frame.add(navbarPanel, BorderLayout.WEST);

        // Ajouter le panneau de contenu à la fenêtre
        frame.add(contentPanel, BorderLayout.CENTER);

        // Rendre la fenêtre visible
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre
        frame.setUndecorated(true); // Supprimer la décoration de la fenêtre (barre de titre, etc.)
        frame.setVisible(true);
    }

    private JPanel createNavbar() {
        JPanel navbarPanel = new JPanel();
        navbarPanel.setLayout(new BoxLayout(navbarPanel, BoxLayout.Y_AXIS));

        JButton btn1 = createNavbarButton("Btn 1");
        JButton btn2 = createNavbarButton("Btn 2");
        JButton btn3 = createNavbarButton("Btn 3");

        // Ajouter des boutons à la barre de navigation
        navbarPanel.add(btn1);
        navbarPanel.add(btn2);
        navbarPanel.add(btn3);

        return navbarPanel;
    }

    private JButton createNavbarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateContent();
            }
        });
        return button;
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
