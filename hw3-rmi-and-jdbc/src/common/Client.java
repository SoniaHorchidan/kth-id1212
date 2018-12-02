package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void notify(Notification notification) throws RemoteException;
}
