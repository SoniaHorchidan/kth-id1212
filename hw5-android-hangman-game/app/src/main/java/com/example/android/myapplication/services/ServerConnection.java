package com.example.android.myapplication.services;

import com.example.android.myapplication.common.Message;
import com.example.android.myapplication.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerConnection {
    private static String host = "10.0.2.2";
    private static int port = 8081;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private volatile boolean connected;

    public void connect(OutputHandler handler) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        toServer = new ObjectOutputStream(socket.getOutputStream());
        fromServer = new ObjectInputStream(socket.getInputStream());
        new Thread(new Listener(handler)).start();
    }

    public void disconnect() throws IOException {
        socket.close();
        socket = null;
        connected = false;
    }

    public void startGame() {
        sendMessage(new Message(MessageType.START));
    }

    public void sendMessage(Message msg) {
        try {
            Message.send(msg, toServer);
            toServer.flush();
            toServer.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Listener implements Runnable {
        private final OutputHandler outputHandler;

        public Listener(OutputHandler outputHandler) {
            this.outputHandler = outputHandler;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Message msg = Message.receive(fromServer);
                    outputHandler.handleMessage(msg);
                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    outputHandler.handleMessage(new Message(MessageType.INFORM, "Lost connection."));
                }
            }
        }
    }

}
