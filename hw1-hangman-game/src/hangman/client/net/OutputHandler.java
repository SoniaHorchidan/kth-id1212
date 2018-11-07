package hangman.client.net;

import hangman.common.Message;

public interface OutputHandler {
    void handleMessage(Message msg);
}
