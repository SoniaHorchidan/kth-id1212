package hangman.client.controller;

import hangman.client.net.OutputHandler;
import hangman.client.net.ServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private ServerConnection serverConnection = new ServerConnection();
    private static String host = "localhost";
    private static int port = 8080;

    public void connect(OutputHandler outputHandler) {
        CompletableFuture.runAsync(() -> {
            try {
                serverConnection.connect(host, port, outputHandler);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        });
    }

    public void disconnect() {
        try {
            serverConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        CompletableFuture.runAsync(() -> serverConnection.startGame());
    }

    public void guessLetter(String letter) {
        serverConnection.guessLetter(letter);
    }

    public void guessWord(String word) {
        serverConnection.guessWord(word);
    }
}
