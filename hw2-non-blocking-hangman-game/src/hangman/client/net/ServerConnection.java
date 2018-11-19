package hangman.client.net;

import hangman.common.Message;
import hangman.common.MessageType;
import hangman.common.MessageUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;


public class ServerConnection implements Runnable {
    private final Queue<Message> sendingQueue = new ArrayDeque<>();
    private final Queue<Message> readingQueue = new ArrayDeque<>();
    private final ByteBuffer serverMessage = ByteBuffer.allocateDirect(4096);
    private InetSocketAddress serverAddress;
    private Selector selector;
    private SocketChannel socketChannel;
    private OutputHandler outputHandler;
    private boolean connected = false;
    private volatile boolean shouldSend = false;

    public void connect(String host, int port, OutputHandler outputHandler) {
        this.serverAddress = new InetSocketAddress(host, port);
        this.outputHandler = outputHandler;
        new Thread(this).start();
    }

    public void startGame() {
        sendMessage(new Message(MessageType.START));
    }

    public void guessLetter(String letter) {
        sendMessage(new Message(MessageType.LETTER, letter));
    }

    public void guessWord(String word) {
        sendMessage(new Message(MessageType.WORD, word));
    }

    public void disconnect() throws IOException {
        this.connected = false;
        this.socketChannel.close();
        this.socketChannel.keyFor(selector).cancel();
    }

    public void sendMessage(Message message) {
        synchronized (sendingQueue) {
            sendingQueue.add(message);
        }
        this.shouldSend = true;
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(serverAddress);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            connected = true;

            while (connected) {
                if (shouldSend) {
                    socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    shouldSend = false;
                }
                this.selector.select();
                for (SelectionKey key : this.selector.selectedKeys()) {
                    if (!key.isValid()) continue;
                    if (key.isConnectable()) establishConnection(key);
                    else if (key.isReadable()) handleReading();
                    else if (key.isWritable()) handleWriting(key);
                    selector.selectedKeys().remove(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void establishConnection(SelectionKey key) throws IOException {
        this.socketChannel.finishConnect();
        key.interestOps(SelectionKey.OP_READ);
    }

    private void handleReading() throws IOException, ClassNotFoundException {
        serverMessage.clear();
        int numOfReadBytes = socketChannel.read(serverMessage);
        if (numOfReadBytes == -1) throw new IOException("Lost connection...");
        byte[] messageContent = readMessageFromBuffer();
        readingQueue.add(MessageUtils.deserialize(messageContent));
        while (readingQueue.size() > 0) {
            Message message = readingQueue.poll();
            outputHandler.handleMessage(message);
        }
    }

    private void handleWriting(SelectionKey key) throws IOException {
        synchronized (sendingQueue) {
            while (sendingQueue.size() > 0) {
                Message message = sendingQueue.poll();
                byte[] messageContent = MessageUtils.serialize(message);
                ByteBuffer readyMessage = ByteBuffer.wrap(messageContent);
                socketChannel.write(readyMessage);
                if (readyMessage.hasRemaining()) return;
            }
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    private byte[] readMessageFromBuffer() {
        serverMessage.flip();
        byte[] bytes = new byte[serverMessage.remaining()];
        serverMessage.get(bytes);
        return bytes;
    }
}
