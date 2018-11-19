package hangman.common;

import java.io.*;

public class Message implements Serializable {
    private MessageType type;
    private String word;
    private int attempts;
    private int score;

    public Message(MessageType type) {
        this.type = type;
    }

    public Message(MessageType type, String word) {
        this.type = type;
        this.word = word;
    }

    public Message(MessageType type, String word, int attempts) {
        this.type = type;
        this.word = word;
        this.attempts = attempts;
    }

    public MessageType getType() {
        return type;
    }

    public String getWord() {
        return word;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", word='" + word + '\'' +
                ", attempts=" + attempts +
                ", score=" + score +
                '}';
    }
}
