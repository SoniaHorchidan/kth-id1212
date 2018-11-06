package hangman.server.controller;

import hangman.server.model.Game;

public class Controller {
    private Game game = new Game();
    private int overAllScore = 0;

    public String parseInput(String clientInput) {
        if (clientInput.equals("start"))
            game.init();
        else {
            String[] input = clientInput.split(" ");
            switch (input[0]) {
                case "word": {
                    game.checkWord(input[1]);
                    break;
                }
                case "letter": {
                    game.checkLetter(input[1].charAt(0));
                    break;
                }
                default: {
                    return "Unknown command";
                }
            }
        }
        return getUpdatedStatus();
    }

    private String getUpdatedStatus() {
        String result = "";
        String gameStatus = game.getStatus();
        String[] elems = gameStatus.split(" ");
        switch (elems[0]) {
            case "WON": {
                overAllScore++;
                break;
            }
            case "LOST": {
                overAllScore--;
                break;
            }
            default: {
                break;
            }
        }

        result = gameStatus + " " + overAllScore;
        return result;
    }
}
