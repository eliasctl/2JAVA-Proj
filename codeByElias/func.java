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

    // fait la fonciton quand on clique sur le bouton connexionButton
    public void connection() {
        System.out.println("Connection");
    }
}