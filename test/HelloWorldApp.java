import javax.swing.*;
import java.awt.*;

public class HelloWorldApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HelloWorldApp().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Hello World App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JLabel helloLabel = new JLabel("Hello World");
        helloLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        // Utiliser un gestionnaire de disposition pour centrer l'étiquette
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.weightx = gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(helloLabel, gbc);

        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        frame.setVisible(true);
    }
}
