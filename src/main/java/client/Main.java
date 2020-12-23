package client;

import client.network.TCPClient;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.nio.file.Paths;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //URL url = Paths.get("./src/main/java/client/view/window.fxml").toUri().toURL();
        //Parent root = FXMLLoader.load(url);
        Parent root = FXMLLoader.load(getClass().getResource("/window.fxml"));
        primaryStage.setTitle("Searcher");
        primaryStage.setScene(new Scene(root, 777, 700));
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                TCPClient.send("_bye_");
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        TCPClient client = new TCPClient(32000, "localhost");
        client.work();
        launch(args);
    }
}
