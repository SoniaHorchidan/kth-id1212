package hangman.client.startup;

import hangman.client.view.CommandsInterpreter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/resources/game.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("The Hangman Game");
        primaryStage.setScene(new Scene(root, 900, 700));

        CommandsInterpreter controller = loader.getController();
        primaryStage.setOnHidden(e -> controller.shutdown());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
