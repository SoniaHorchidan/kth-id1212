package client.view;

import common.*;
import server.model.File;

import javax.security.auth.login.AccountException;
import java.rmi.RemoteException;
import java.util.List;

public class CommandsInterpreter {
    private FileCatalog server;
    private AccountDTO user;

    public CommandsInterpreter(FileCatalog server) {
        this.server = server;
    }

    public void createAccount(String username, String password) throws AccountException, RemoteException {
        server.createAccount(username, password);
    }

    public void login(String username, String password, Client myRemoteObject) throws AccountException, RemoteException {
        user = server.login(username, password, myRemoteObject);
    }

    public void logout() throws RemoteException {
        if(user != null)
            server.logout(user.getUsername());
    }

    public List<? extends FileDTO> getAllFiles() throws RemoteException {
        try {
            return server.listFiles();
        } catch (FileCatalogException e) {
            return null;
        }
    }

    public String upload(String fileName, int size, boolean readPermission, boolean writePermission) throws RemoteException {
        try {
            server.uploadFile(user.getUsername(), fileName, size, readPermission, writePermission);
            return "File created";
        } catch (FileCatalogException e) {
            return e.getMessage();
        }
    }

    public String update(File file, int newSize) throws RemoteException {
        try {
            server.updateFile(file, newSize, user.getUsername());
            return "File updated";
        } catch (FileCatalogException e) {
            return e.getMessage();
        }
    }

    public String deleteFile(File file) throws RemoteException {
        try {
            server.deleteFile(file, user.getUsername());
            return "File deleted";
        } catch (FileCatalogException e) {
            return e.getMessage();
        }
    }

    public String readFile(File file) throws RemoteException {
        server.readFile(file, user.getUsername());
        return "Read was successful";
    }

    public boolean checkIdentity(String username) {
        return username.equals(user.getUsername());
    }


}
