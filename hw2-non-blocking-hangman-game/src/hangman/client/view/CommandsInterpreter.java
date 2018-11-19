package hangman.client.view;

import hangman.client.controller.Controller;
import hangman.client.net.OutputHandler;
import hangman.common.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
        clearMessageText();
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
        clearMessageText();
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
        messagesText.setText("");
    }

    @FXML
    private void enterGuessedLetter(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER"))
            guessLetter();
    }

    @FXML
    private void enterGuessedWord(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER"))
            guessWholeWord();
    }

    @FXML
    private void enterToRestart(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER"))
            playAgain();
    }

    private void clearMessageText() {
        messagesText.setText("");
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
        public void handleMessage(Message msg) {
            Platform.runLater(() -> {
                switch (msg.getType()) {
                    case WON: {
                        endGame("You won!");
                        break;
                    }
                    case LOST: {
                        endGame("You lost. Try again!");
                        break;
                    }
                    case IN_PROGRESS: {
                        break;
                    }
                    default: {
                        messagesText.setText("Unknown command");
                        break;
                    }
                }
                wordToGuess.setText(msg.getWord());
                attempts.setText(String.valueOf(msg.getAttempts()));
                score.setText(String.valueOf(msg.getScore()));
            });
        }
    }

}
