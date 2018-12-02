package common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification implements Serializable {
    private String receiver;
    private String sender;
    private String fileName;
    private NotificationType notificationType;
    private String date;

    public Notification(String receiver, String sender, String fileName, NotificationType notificationType) {
        this.sender = sender;
        this.receiver = receiver;
        this.fileName = fileName;
        this.notificationType = notificationType;
        getTime();
    }

    private void getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        date = dtf.format(LocalDateTime.now());
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getFileName() {
        return fileName;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getDate() {
        return date;
    }
}
