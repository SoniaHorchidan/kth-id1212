package hangman.server.net;

import hangman.server.controller.Controller;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private boolean connected;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private Controller controller;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        controller = new Controller();
        connected = true;
    }

    @Override
    public void run() {
        try {
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        while (connected) {
            try {
                String clientInput = fromClient.readLine();
                // TODO
                /**
                 * No computation done; send command to controller which has access to the model
                 */
                if (clientInput != null) {
                    String serverOutput = controller.parseInput(clientInput);
                    toClient.println(serverOutput);
                    toClient.flush();
                }
            } catch (IOException e) {
                disconnectClient();
                e.printStackTrace();
            }
        }
    }

    public void disconnectClient() {
        try {
            clientSocket.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
