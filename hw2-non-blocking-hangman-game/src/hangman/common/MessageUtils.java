package hangman.common;

import java.io.*;

public class MessageUtils {

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

}
