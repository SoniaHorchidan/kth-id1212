package hangman.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private int portNumber = 8080;
    private static final int LINGER_TIME = 5000;

    public void start() {
        try {
            ServerSocket listeningSocket = new ServerSocket(portNumber);
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                startClientHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Failed to start server.");
        }
    }

    public void startClientHandler(Socket clientSocket) throws SocketException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        Thread clientThread = new Thread(clientHandler);
        clientThread.setPriority(Thread.MAX_PRIORITY);
        clientThread.start();
    }

}
