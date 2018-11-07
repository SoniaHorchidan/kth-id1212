package hangman.server.model;

import hangman.common.Message;
import hangman.common.MessageType;
import hangman.file.WordPicker;

public class Game {
    private String currentWord;
    private String guessedWordSoFar;
    private int attemptsLeft;
    private MessageType outcome;
    private WordPicker wordPicker = new WordPicker();

    public void init() {
        currentWord = chooseWord();
        int wordLen = currentWord.length();
        guessedWordSoFar = generateHiddenWord(wordLen);
        attemptsLeft = wordLen;
        outcome = MessageType.IN_PROGRESS;
    }

    private String chooseWord() {
        return wordPicker.next();
    }

    private String generateHiddenWord(int wordLen) {
        String result = "";
        for (int i = 0; i < wordLen; i++)
            result += "*";
        return result;
    }

    public void replaceCharacters(char letter) {
        String newHiddenWord = "";
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == letter)
                newHiddenWord += letter;
            else
                newHiddenWord += guessedWordSoFar.charAt(i);
        }
        guessedWordSoFar = newHiddenWord;
    }

    public void checkLetter(char letter) {
        if (isGuessedLetter(letter))
            replaceCharacters(letter);
        else
            attemptsLeft--;
        checkStatus();
    }

    public boolean isGuessedLetter(char letter) {
        return currentWord.indexOf(letter) >= 0;
    }

    public void checkWord(String word) {
        if (isEqual(word))
            guessedWordSoFar = word;
        else
            attemptsLeft--;
        checkStatus();
    }

    public boolean isEqual(String word) {
        return word.equals(currentWord);
    }

    private void checkStatus() {
        if (attemptsLeft == 0)
            outcome = MessageType.LOST;
        else if (currentWord.equals(guessedWordSoFar))
            outcome = MessageType.WON;
    }

    public Message getStatus() {
        return new Message(outcome, guessedWordSoFar, attemptsLeft);
    }

}
