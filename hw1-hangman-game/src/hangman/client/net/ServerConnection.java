package hangman.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerConnection {
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private volatile boolean connected;

    public void connect(String host, int port, OutputHandler handler) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        boolean autoFlush = true;
        toServer = new PrintWriter(socket.getOutputStream(), autoFlush);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener(handler)).start();

    }

    public void disconnect() throws IOException {
        socket.close();
        socket = null;
        connected = false;
    }

    public void startGame() {
        sendMessage("start");
    }

    public void guessLetter(String letter) {
        sendMessage("letter " + letter);
    }

    public void guessWord(String word) {
        sendMessage("word " + word);
    }

    private void sendMessage(String msg) {
        toServer.println(msg);
        toServer.flush();
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
                    outputHandler.handleMessage(fromServer.readLine());
                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    outputHandler.handleMessage("Lost connection.");
                }
            }
        }
    }

}
