package server.controller;

import common.*;
import server.integration.FileCatalogDAO;
import server.integration.FileCatalogDBException;
import server.model.Account;
import server.model.File;
import server.model.ParticipantManager;

import javax.security.auth.login.AccountException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Controller extends UnicastRemoteObject implements FileCatalog {
    private FileCatalogDAO fileCatalogDB;
    private ParticipantManager participantManager;

    public Controller() throws RemoteException, FileCatalogDBException {
        super();
        this.fileCatalogDB = new FileCatalogDAO();
        this.participantManager = new ParticipantManager();
    }

    @Override
    public void createAccount(String holderName, String password) throws RemoteException, AccountException {
        String acctExistsMsg = "Account for: " + holderName + " already exists";
        String failureMsg = "Could not create account for: " + holderName;
        try {
            if (fileCatalogDB.findAccountByName(holderName) != null) {
                throw new AccountException(acctExistsMsg);
            }
            fileCatalogDB.createAccount(new Account(holderName, password));
        } catch (FileCatalogDBException ex) {
            throw new AccountException(failureMsg);
        }

    }

    @Override
    public AccountDTO login(String holderName, String password, Client remoteObj) throws RemoteException, AccountException {
        String invalidCred = "Invalid credentials";
        String failureMsg = "Could not login. Please try again";
        try {
            Account account = fileCatalogDB.login(holderName, password);
            if (account == null)
                throw new AccountException(invalidCred);
            participantManager.addParticipant(holderName, remoteObj);
            return account;
        } catch (FileCatalogDBException ex) {
            throw new AccountException(failureMsg);
        }
    }

    @Override
    public void logout(String holderName) throws RemoteException {
        participantManager.removeParticipant(holderName);
    }

    @Override
    public void uploadFile(String user, String fileName, int size, boolean readPerm, boolean writePerm) throws RemoteException, FileCatalogException {
        String invalidNameMsg = "Please enter another name for the file. The name " + fileName + " is already taken";
        File newFile = new File(fileName, user, size, writePerm, readPerm);
        try {
            fileCatalogDB.createFile(newFile);
        } catch (FileCatalogDBException ex) {
            throw new FileCatalogException(invalidNameMsg, ex);
        }
    }

    @Override
    public List<? extends FileDTO> listFiles() throws RemoteException, FileCatalogException {
        String invalidNameMsg = "Could not list files";
        List<File> listOfFiles;
        try {
            listOfFiles = fileCatalogDB.listFiles();
            return listOfFiles;
        } catch (FileCatalogDBException ex) {
            throw new FileCatalogException(invalidNameMsg, ex);
        }
    }

    @Override
    public void deleteFile(FileDTO file, String writer) throws RemoteException, FileCatalogException {
        String failureMsg = "Could not delete the file";
        try {
            fileCatalogDB.deleteFile(file.getName());
            notify(file.getName(), file.getOwner(), writer, NotificationType.WRITE);
        } catch (FileCatalogDBException ex) {
            throw new FileCatalogException(failureMsg, ex);
        }
    }

    @Override
    public void updateFile(FileDTO file, int size, String writer) throws RemoteException, FileCatalogException {
        String invalidNameMsg = "The update was not performed";
        try {
            fileCatalogDB.updateFile(file.getName(), size);
            notify(file.getName(), file.getOwner(), writer, NotificationType.WRITE);
        } catch (FileCatalogDBException ex) {
            throw new FileCatalogException(invalidNameMsg, ex);
        }
    }

    @Override
    public void readFile(FileDTO file, String reader) {
        notify(file.getName(), file.getOwner(), reader, NotificationType.READ);
    }

    private void notify(String fileName, String owner, String notifier, NotificationType type) {
        if (owner.equals(notifier)) return;      //modified own file; do nothing
        Notification notification = new Notification(owner, notifier, fileName, type);
        Client client = participantManager.findUser(owner);
        try {
            if (client != null)
                client.notify(notification);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
