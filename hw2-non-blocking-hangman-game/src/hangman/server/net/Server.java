package hangman.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private static final int portNumber = 8081;

    public void start() {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(portNumber));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server running...");

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                SelectionKey key;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        startHandler(key);
                    } else if (key.isReadable()) {
                        handleReceivedMessages(key);
                    } else if (key.isWritable()) {
                        handleSendingMessages(key);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startHandler(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);
        ClientHandler handler = new ClientHandler(this, socketChannel);
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, handler);
    }

    private void handleReceivedMessages(SelectionKey selectionKey) {
        ClientHandler handler = (ClientHandler) selectionKey.attachment();
        try {
            handler.receiveMessage();
        } catch (Exception ex) {
            // if any exception is being thrown on receiving, disconnect client
            handler.disconnectClient();
            selectionKey.cancel();
        }
    }

    private void handleSendingMessages(SelectionKey selectionKey) throws IOException {
        ClientHandler clientHandler = (ClientHandler) selectionKey.attachment();
        clientHandler.sendMessage();
        selectionKey.interestOps(SelectionKey.OP_READ);
    }


    public void prepareToSend(SocketChannel client) {
        client.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
    }


}
