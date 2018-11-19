package hangman.server.net;

import hangman.common.Message;
import hangman.server.controller.Controller;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private boolean connected;
    private ObjectInputStream fromClient;
    private ObjectOutputStream toClient;
    private Controller controller;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        controller = new Controller();
        connected = true;
    }

    @Override
    public void run() {
        try {
            toClient = new ObjectOutputStream(clientSocket.getOutputStream());
            fromClient = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        while (connected) {
            try {
                //  Message clientInput = (Message) fromClient.readObject();
                Message clientInput = Message.receive(fromClient);
                System.out.println("Received " + clientInput.toString());
                if (clientInput != null) {
                    Message serverOutput = controller.parseInput(clientInput);
                    // toClient.writeObject(serverOutput);
                    Message.send(serverOutput, toClient);
                    toClient.flush();
                    toClient.reset();
                }
            } catch (EOFException e) {
                disconnectClient();
                //do nothing
            } catch (IOException e) {
                disconnectClient();
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
