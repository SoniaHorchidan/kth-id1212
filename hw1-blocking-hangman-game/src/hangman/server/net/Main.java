package hangman.server.net;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("Server started...\n");
        server.start();
    }
}
