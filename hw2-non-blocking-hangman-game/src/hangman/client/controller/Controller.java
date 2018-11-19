package hangman.client.controller;

import hangman.client.net.OutputHandler;
import hangman.client.net.ServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private ServerConnection serverConnection = new ServerConnection();
    private static final String host = "localhost";
    private static final int port = 8081;

    public void connect(OutputHandler outputHandler) {
        CompletableFuture.runAsync(() -> {
            serverConnection.connect(host, port, outputHandler);
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
        CompletableFuture.runAsync(() -> serverConnection.guessLetter(letter));
    }

    public void guessWord(String word) {
        CompletableFuture.runAsync(() -> serverConnection.guessWord(word));
    }
}
