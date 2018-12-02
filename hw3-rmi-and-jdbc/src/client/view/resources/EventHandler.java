package client.view.resources;

import client.view.CommandsInterpreter;
import common.Client;
import common.FileDTO;
import common.Notification;
import common.NotificationType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import server.model.File;

import javax.security.auth.login.AccountException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class EventHandler {
    private CommandsInterpreter commandsInterpreter;
    private Client myRemoteObject;
    private File selectedFile;
    private String loggedInUser;
    @FXML
    private AnchorPane loginPane;
    @FXML
    private AnchorPane filesPane;
    @FXML
    private Label registerMessageLabel;
    @FXML
    private TextField regUsernameTextField;
    @FXML
    private TextField regPasswordTextField;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private TextField loginPasswordTextField;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField fileNameField;
    @FXML
    private TextField sizeField;
    @FXML
    private CheckBox readPermissionCheckbox;
    @FXML
    private CheckBox writePermissionCheckbox;
    @FXML
    private Label uploadMessage;
    @FXML
    private ListView filesList;
    @FXML
    private AnchorPane updatePane;
    @FXML
    private AnchorPane readPane;
    @FXML
    private TextField newSizeField;
    @FXML
    private Label updateMessage;
    @FXML
    private Label readMessage;
    @FXML
    private Label notificationLabel;

    @FXML
    private void initialize() {
        initializeRemoteObject();
        addListenerOnListView();
        clearLoginMessageFields();
        loginPane.setVisible(true);
        filesPane.setVisible(false);
    }

    private void addListenerOnListView() {
        filesList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<File>) (observable, oldValue, newValue) -> {
            clearFilesPageFields();
            selectedFile = newValue;
            if (selectedFile == null) return;
            boolean isOwner = commandsInterpreter.checkIdentity(selectedFile.getOwner());
            boolean readPerm = selectedFile.hasReadPermission();
            readPane.setDisable(!(readPerm || isOwner));
            boolean writePerm = selectedFile.hasWritePermission();
            updatePane.setDisable(!(writePerm || isOwner));
        });
    }

    private void initializeRemoteObject() {
        try {
            myRemoteObject = new NotificationOutput();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createAccount() {
        clearLoginMessageFields();
        String username = regUsernameTextField.getText();
        String pass = regPasswordTextField.getText();
        try {
            commandsInterpreter.createAccount(username, pass);
            registerMessageLabel.setText("The account has been created. Please log in.");
            clearTextFields();
        } catch (RemoteException e) {
            registerMessageLabel.setText("Could not create; please try again.");
            return;
        } catch (AccountException e) {
            registerMessageLabel.setText("Could not create; username already exists");
            return;
        }
    }

    @FXML
    private void login() {
        clearLoginMessageFields();
        clearUploadFilesFields();
        String username = loginUsernameTextField.getText();
        String pass = loginPasswordTextField.getText();
        try {
            commandsInterpreter.login(username, pass, myRemoteObject);
            loggedInUser = username;
            clearTextFields();
            redirectToFilesPage();
        } catch (RemoteException e) {
            loginMessageLabel.setText("Could not login; please try again.");
        } catch (AccountException e) {
            loginMessageLabel.setText("Could not login; invalid credentials");
        }
    }

    @FXML
    private void logout() {
        try {
            commandsInterpreter.logout();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        redirectToLoginPage();
    }

    @FXML
    private void uploadFile() {
        String fileName = fileNameField.getText();
        String sizeString = sizeField.getText();
        if (fileName.length() == 0 || sizeString.length() == 0) return;
        int size = Integer.parseInt(sizeString);
        boolean readPermission = false;
        if (readPermissionCheckbox.isSelected())
            readPermission = true;
        boolean writePermission = false;
        if (writePermissionCheckbox.isSelected())
            writePermission = true;
        try {
            String message = commandsInterpreter.upload(fileName, size, readPermission, writePermission);
            uploadMessage.setText(message);
            loadFiles();
            clearUploadFilesFields();
        } catch (RemoteException e) {
            uploadMessage.setText(e.getMessage());
        }
    }

    @FXML
    private void updateFile() {
        try {
            int newSize = Integer.parseInt(newSizeField.getText());
            commandsInterpreter.update(selectedFile, newSize);
            String message = commandsInterpreter.update(selectedFile, newSize);
            updateMessage.setText(message);
            loadFiles();
        } catch (RemoteException e) {
            updateMessage.setText(e.getMessage());
        }
    }

    @FXML
    private void deleteFile() {
        try {
            String message = commandsInterpreter.deleteFile(selectedFile);
            updateMessage.setText(message);
            loadFiles();
        } catch (RemoteException e) {
            updateMessage.setText(e.getMessage());
        }
    }

    @FXML
    private void readFile() {
        try {
            String message = commandsInterpreter.readFile(selectedFile);
            readMessage.setText(message);
        } catch (RemoteException e) {
            readMessage.setText(e.getMessage());
        }
    }


    private void redirectToLoginPage() {
        loginPane.setVisible(true);
        filesPane.setVisible(false);
    }

    private void redirectToFilesPage() {
        loginPane.setVisible(false);
        filesPane.setVisible(true);
        usernameLabel.setText(loggedInUser);
        loadFiles();
    }

    private void loadFiles() {
        List<? extends FileDTO> list = null;
        filesList.getItems().clear();
        try {
            list = commandsInterpreter.getAllFiles();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (FileDTO file : list) {
            filesList.getItems().add(file);
        }
    }

    private void clearLoginMessageFields() {
        loginMessageLabel.setText("");
        registerMessageLabel.setText("");
    }

    private void clearTextFields() {
        clearLoginPageFields();
        clearFilesPageFields();
    }

    private void clearFilesPageFields() {
        uploadMessage.setText("");
        updatePane.setDisable(true);
        readPane.setDisable(true);
        updateMessage.setText("");
        newSizeField.setText("");
        readMessage.setText("");
        notificationLabel.setText("");
    }

    private void clearLoginPageFields() {
        regUsernameTextField.clear();
        regPasswordTextField.clear();
        loginPasswordTextField.clear();
        loginUsernameTextField.clear();
    }

    private void clearUploadFilesFields() {
        fileNameField.clear();
        sizeField.clear();
        readPermissionCheckbox.setSelected(false);
        writePermissionCheckbox.setSelected(false);
    }

    public void setCommandsInterpreter(CommandsInterpreter interpreter) {
        this.commandsInterpreter = interpreter;
    }

    public void shutdown() {
        try {
            commandsInterpreter.logout();
            System.exit(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private class NotificationOutput extends UnicastRemoteObject implements Client {

        public NotificationOutput() throws RemoteException {
        }

        @Override
        public void notify(Notification notification) {
            String notificationMessage = constructNotificationMessage(notification);
            Platform.runLater(() -> {
                notificationLabel.setText(notificationMessage);
                loadFiles();
            });
        }

        private String constructNotificationMessage(Notification notification) {
            String operation = "";
            if (notification.getNotificationType() == NotificationType.WRITE)
                operation = "updated/deleted";
            else if (notification.getNotificationType() == NotificationType.READ)
                operation = "read";
            return "The file named " + notification.getFileName() +
                    " has been " + operation + " by " + notification.getSender() +
                    " at " + notification.getDate();
        }
    }
}
