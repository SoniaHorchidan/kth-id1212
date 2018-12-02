package common;

import javax.security.auth.login.AccountException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FileCatalog extends Remote {
    String FILE_CATALOG_NAME_IN_REGISTRY = "file_catalog";

    void createAccount(String name, String password) throws RemoteException, AccountException;
    AccountDTO login(String holderName, String password, Client remoteObj) throws RemoteException, AccountException;
    void logout(String holderName) throws RemoteException;
    void uploadFile(String user, String fileName, int size, boolean readPerm, boolean writePerm) throws RemoteException, FileCatalogException;
    List<? extends FileDTO> listFiles() throws RemoteException, FileCatalogException;
    void deleteFile(FileDTO file, String writer) throws RemoteException, FileCatalogException;
    void updateFile(FileDTO file, int size, String writer) throws RemoteException, FileCatalogException;
    void readFile(FileDTO file, String reader) throws RemoteException;
}
