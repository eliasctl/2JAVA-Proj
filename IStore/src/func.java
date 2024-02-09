import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.mindrot.jbcrypt.BCrypt;

public class func {
    public int roleToInt(String role) {
        if (role.equals("admin")) {
            return 1;
        } else if (role.equals("user")) {
            return 2;
        } else if (role.equals("guest")) {
            return 3;
        } else {
            return 0;
        }
    }

    public String intToRole(int role) {
        if (role == 1) {
            return "admin";
        } else if (role == 2) {
            return "user";
        } else if (role == 3) {
            return "guest";
        } else {
            return "unknown";
        }
    }

    public void connection(Object frame) {
        JTextField eMailField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        passwordField.setEchoChar('●'); // Masque le texte saisi dans le champ de mot de passe

        Object[] message = { "E-Mail:", eMailField, "Code:", passwordField };
        int option = JOptionPane.showOptionDialog(null, message, "Connexion", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new String[] { "Connexion", "Inscription", "Code oublié" }, null);
        if (option == JOptionPane.OK_OPTION) {
            // ((javax.swing.JFrame) frame).getJMenuBar().setVisible(true);

            String eMail = eMailField.getText();
            char[] password = passwordField.getPassword();

            // Ici vous pouvez traiter le nom d'utilisateur et le mot de passe saisis
            System.out.println("E-Mail: " + eMail);
            System.out.println("Password: " + new String(password));

            // Hacher le mot de passe
            String hashedPassword = BCrypt.hashpw(new String(password), "$2a$10$VM/GfVScMMgdLVtHwABv6u");
            System.out.println("Hashed Password: " + hashedPassword);

        } else if (option == 1) {
            // Gérer l'inscription
            registration(frame);
            System.out.println("Inscription");
        } else if (option == 2) {
            // Gérer le code oublié
            JOptionPane.showMessageDialog(null, "Veuillez contacter un Administrateur", "Code oublié",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println("Login canceled");
        }
    }

    public void registration(Object frame) {
        JTextField eMailField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JPasswordField confirmPasswordField = new JPasswordField(10);
        passwordField.setEchoChar('●'); // Masque le texte saisi dans le champ de mot de passe
        confirmPasswordField.setEchoChar('●'); // Masque le texte saisi dans le champ de mot de passe

        Object[] message = { "E-Mail:", eMailField, "Code:", passwordField, "Confirmer le code:",
                confirmPasswordField };
        int option = JOptionPane.showOptionDialog(null, message, "Inscription", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new String[] { "Inscription", "Connexion" }, null);
        if (option == JOptionPane.OK_OPTION) {

            String eMail = eMailField.getText();
            char[] password = passwordField.getPassword();
            char[] confirmPassword = confirmPasswordField.getPassword();

            // Traiter les données d'inscription
            System.out.println("E-Mail: " + eMail);
            System.out.println("Password: " + new String(password));
            System.out.println("Confirm Password: " + new String(confirmPassword));

            // Vérifier si les mots de passe sont identiques
            if (new String(password).equals(new String(confirmPassword))) {
                // Hacher le mot de passe
                String hashedPassword = BCrypt.hashpw(new String(password), BCrypt.gensalt());
                System.out.println("Hashed Password: " + hashedPassword);
            } else {
                JOptionPane.showMessageDialog(null, "Les mots de passe ne correspondent pas", "Erreur d'Inscription",
                        JOptionPane.ERROR_MESSAGE);
            }

        } else if (option == 1) {
            // Gérer la connexion
            connection(frame);
        } else {
            System.out.println("Registration canceled");
        }
    }
}