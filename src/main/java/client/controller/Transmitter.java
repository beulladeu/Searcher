package client.controller;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.network.TCPClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Transmitter {

    public static TextField TFResultStatic;
    public static List<HBox> hBoxesListStatic;
    public static VBox vBoxStatic;

    @FXML
    private ScrollPane ScrollPRes;

    @FXML
    private TextField TFResult;

    @FXML
    private Button BResearch;

    @FXML
    private VBox VBResult;

    @FXML
    private ImageView aboutAlert;


    @FXML
    void initialize() {
        ScrollPRes.setFitToWidth(true);
        hBoxesListStatic = new ArrayList<>();
        vBoxStatic = VBResult;
        vBoxStatic.setSpacing(10);
        vBoxStatic.setPadding(new Insets(10,0, 0,0));
        TFResultStatic = TFResult;
        BResearch.setOnAction(actionEvent -> {
            TCPClient.send(Transmitter.TFResultStatic.getText());
        });

        aboutAlert.setOnMouseClicked(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Project -- Searcher");
            alert.setTitle("About");
            alert.setContentText("Created by beulladeu\nemail: vverkhonina@mail.ru\ngroup 5812, SUAI\n2020");
            alert.show();
        });
    }

    public static void showMessage(String[][] arr) throws IOException {
        vBoxStatic.getChildren().removeAll(vBoxStatic.getChildren());
        hBoxesListStatic.clear();
        for(int i = 0; i < arr.length; i++){
            String uri = arr[i][0];
            String title = arr[i][1];
            Hyperlink hyperlink = new Hyperlink(title);
            hyperlink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(new URI(uri));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            hyperlink.setMaxSize(450,100);
            hyperlink.setMinSize(450, 100);
            hyperlink.setStyle("-fx-border-color: #ffff33; -fx-border-width: 3,3,3,3;");
            Label label = new Label("" + (i+1));
            label.setStyle("-fx-font-weight: bold");
            int spacing = 15;
            if(i > 8) spacing = 5;
            HBox root = new HBox(spacing, label, hyperlink);
            hBoxesListStatic.add(root);
        }
        vBoxStatic.getChildren().addAll(hBoxesListStatic);
    }

    public static void showLabel(String text){
        vBoxStatic.getChildren().removeAll(hBoxesListStatic);
        hBoxesListStatic.clear();
        Label label = new Label(text);
        label.setMaxSize(450,120);
        label.setMinSize(450, 120);
        label.setStyle("-fx-border-color: #ffff33; -fx-border-width: 3,3,3,3;");
        vBoxStatic.getChildren().add(label);
    }
}
