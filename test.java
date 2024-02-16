import java.net.*;

public class test {
    public static void main(String[] args) {
        try {
            URI uri = new URI("http", null, "localhost", 3002, "/login", null, null);
            URL url = uri.toURL();
            System.out.println(url);
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
