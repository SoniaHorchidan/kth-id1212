package hangman.client.view;

import hangman.client.controller.Controller;
import hangman.client.net.OutputHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class CommandsInterpreter {
    private Controller controller;
    private static final String TOO_MANY_LETTERS = "Please insert only one letter";
    private static final String NO_INPUT_PROVIDED = "Please insert a letter or the whole word";

    @FXML
    private AnchorPane startPane;
    @FXML
    private AnchorPane buttonsPane;
    @FXML
    private GridPane appPane;
    @FXML
    private Label attempts;
    @FXML
    private Label score;
    @FXML
    private Label wordToGuess;
    @FXML
    private Label messagesText;
    @FXML
    private TextField letterProposed;
    @FXML
    private TextField wordProposed;

    @FXML
    public void initialize() {
        controller = new Controller();
        UIOutput outputHandler = new UIOutput();
        controller.connect(outputHandler);

        messagesText.setVisible(true);
        messagesText.setText("");
    }

    @FXML
    private void startGame() {
        startPane.setVisible(false);
        appPane.setVisible(true);
        controller.startGame();
    }

    @FXML
    private void guessLetter() {
        String letter = letterProposed.getText();
        if (letter.length() > 1)
            messagesText.setText(TOO_MANY_LETTERS);
        else if (letter.length() == 0)
            messagesText.setText(NO_INPUT_PROVIDED);
        else
            controller.guessLetter(letter);
        clearFields();
    }

    @FXML
    private void guessWholeWord() {
        String word = wordProposed.getText();
        if (word.length() == 0)
            messagesText.setText(NO_INPUT_PROVIDED);
        else
            controller.guessWord(word);
        clearFields();
    }

    @FXML
    private void playAgain() {
        controller.startGame();
        buttonsPane.setDisable(false);
    }

    private void clearFields() {
        letterProposed.clear();
        wordProposed.clear();
    }

    private void endGame(String s) {
        messagesText.setText(s);
        buttonsPane.setDisable(true);
    }

    public void shutdown() {
        controller.disconnect();
    }

    private class UIOutput implements OutputHandler {
        @Override
        public void handleMessage(String msg) {
            Platform.runLater(() -> {
                String[] elements = msg.split(" ");
                switch (elements[0]) {
                    case "WON": {
                        endGame("You won!");
                        break;
                    }
                    case "LOST": {
                        endGame("You lost. Try again!");
                        break;
                    }
                    default: {
                        break;
                    }
                }
                wordToGuess.setText(elements[1]);
                attempts.setText(elements[2]);
                score.setText(elements[3]);
            });
        }
    }

}
