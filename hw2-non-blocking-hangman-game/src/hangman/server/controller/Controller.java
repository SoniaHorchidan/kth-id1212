package hangman.server.controller;

import hangman.common.Message;
import hangman.common.MessageType;
import hangman.server.model.Game;

public class Controller {
    private Game game = new Game();
    private int overAllScore = 0;

    public Message startGame() {
        game.init();
        return getUpdatedStatus();
    }

    public Message parseInput(Message clientInput) {
        MessageType messageType = clientInput.getType();
        switch (messageType) {
            case WORD: {
                game.checkWord(clientInput.getWord());
                break;
            }
            case LETTER: {
                game.checkLetter(clientInput.getWord().charAt(0));
                break;
            }
            default: {
                return new Message(MessageType.INFORM, "Unknown command");
            }
        }
        return getUpdatedStatus();
    }

    private Message getUpdatedStatus() {
        Message gameStatus = game.getStatus();
        MessageType type = gameStatus.getType();
        switch (type) {
            case WON: {
                overAllScore++;
                break;
            }
            case LOST: {
                overAllScore--;
                break;
            }
            default: {
                break;
            }
        }
        gameStatus.setScore(overAllScore);
        return gameStatus;
    }
}
