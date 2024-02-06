import javax.swing.*;
import java.awt.*;

public class FullScreenHelloWorld {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new FullScreenHelloWorld().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Hello World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Obtenir la configuration de l'écran principal
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Définir la taille de la fenêtre en fonction de la taille de l'écran
        frame.setSize(gc.getBounds().width, gc.getBounds().height);

        // Créer une étiquette "Hello World" centrée
        JLabel helloLabel = new JLabel("Hello World");
        helloLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        helloLabel.setHorizontalAlignment(JLabel.CENTER);
        helloLabel.setVerticalAlignment(JLabel.CENTER);

        // Ajouter l'étiquette à la fenêtre
        frame.add(helloLabel);

        // Rendre la fenêtre visible
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre
        frame.setUndecorated(true); // Supprimer la décoration de la fenêtre (barre de titre, etc.)
        frame.setVisible(true);
    }
}
