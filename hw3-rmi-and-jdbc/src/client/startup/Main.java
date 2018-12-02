package client.startup;

import client.view.CommandsInterpreter;
import client.view.resources.EventHandler;
import common.FileCatalog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/resources/clientGUI.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("File Catalog");
        primaryStage.setScene(new Scene(root, 1100, 800));
        EventHandler controller = loader.getController();
        CommandsInterpreter interpreter = null;
        try {
            Object fileCatalog = Naming.lookup(FileCatalog.FILE_CATALOG_NAME_IN_REGISTRY);
            FileCatalog server = (FileCatalog) fileCatalog;
            interpreter = new CommandsInterpreter(server);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println("Could not find the server...");
            e.printStackTrace();
        }
        controller.setCommandsInterpreter(interpreter);
        primaryStage.setOnHidden(e -> controller.shutdown());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
