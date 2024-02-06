import javax.swing.*;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Ajouter une barre de navigation à gauche
        JPanel navbarPanel = createNavbar(contentPanel);
        frame.add(navbarPanel, BorderLayout.WEST);

        // Ajouter le panneau de contenu à la fenêtre
        frame.add(contentPanel, BorderLayout.CENTER);

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }
}
