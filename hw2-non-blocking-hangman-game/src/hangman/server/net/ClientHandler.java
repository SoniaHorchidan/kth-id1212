package hangman.server.net;

import hangman.common.Message;
import hangman.common.MessageType;
import hangman.common.MessageUtils;
import hangman.server.controller.Controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler { //implements Runnable{
    private final LinkedBlockingQueue<Message> receiveMessagesQueue = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Message> sendMessagesQueue = new LinkedBlockingQueue<>();
    private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);
    private final SocketChannel clientChannel;
    private final Server server;
    private Controller controller;

    ClientHandler(Server server, SocketChannel clientChannel) {
        this.server = server;
        this.clientChannel = clientChannel;
        controller = new Controller();
    }

    //@Override
    public void run() {
        Iterator<Message> iterator = receiveMessagesQueue.iterator();
        while (iterator.hasNext()) {
            Message inputMessage = iterator.next();
            if (inputMessage.getType() == MessageType.START)
                ForkJoinPool.commonPool().execute(() -> {
                    // execute time consuming task
                    Message serverResponse = controller.startGame();
                    sendResponseToClient(serverResponse);
                });
            else {
                Message serverResponse = controller.parseInput(inputMessage);
                sendResponseToClient(serverResponse);
            }

            iterator.remove();
        }
    }

    public void receiveMessage() throws IOException, ClassNotFoundException {
        byteBuffer.clear();
        int numOfReadBytes = clientChannel.read(byteBuffer);
        if (numOfReadBytes == -1) System.out.println("Client disconnected...");
        byte[] messageContent = readFromBuffer();
        receiveMessagesQueue.add(MessageUtils.deserialize(messageContent));
        // ForkJoinPool.commonPool().execute(this);
        run();                      // run on the same thread
    }

    public void sendMessage() throws IOException {
        synchronized (sendMessagesQueue) {
            while (!sendMessagesQueue.isEmpty()) {
                Message message = sendMessagesQueue.poll();
                byte[] messageContent = MessageUtils.serialize(message);
                ByteBuffer messageToWrite = ByteBuffer.wrap(messageContent);
                clientChannel.write(messageToWrite);
            }
        }
    }

    public void disconnectClient() {
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponseToClient(Message message) {
        synchronized (sendMessagesQueue) {
            sendMessagesQueue.add(message);
        }
        server.prepareToSend(clientChannel);
    }

    private byte[] readFromBuffer() {
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return bytes;
    }
}
