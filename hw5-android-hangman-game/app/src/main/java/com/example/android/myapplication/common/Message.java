package com.example.android.myapplication.common;

import java.io.*;

public class Message implements Serializable {
    private MessageType type;
    private String word;
    private int attempts;
    private int score;
    private static final long serialVersionUID = 1L;

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

    public static int getSize(Message msg) throws IOException {
        byte[] data = serialize(msg);
        return data.length;
    }

    public static Message deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (Message) is.readObject();
    }

    public static byte[] serialize(Message obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        os.flush();
        os.close();
        return out.toByteArray();
    }

    public static void send(Message msg, ObjectOutputStream to) throws IOException {
        int msgSize = getSize(msg);
        to.writeInt(msgSize);
        byte[] object = Message.serialize(msg);
        to.write(object);
    }

    public static Message receive(ObjectInputStream from) throws IOException, ClassNotFoundException {
        int msgSize = from.readInt();
        byte[] object = new byte[msgSize];
        int counter = 0;
        while (counter < msgSize)
            object[counter++] = from.readByte();
        Message msg = Message.deserialize(object);
        return msg;
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
