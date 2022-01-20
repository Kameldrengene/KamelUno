package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.HeaderText;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.kamelboyz.kameluno.Model.BootstrapButton.makeBootstrapButton;

@Data
public class MainView {
    private Map<String, Button> buttons = new HashMap<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private final Group root = new Group();
    public MainView() throws IOException {
        Text text = new Text();
        buttons.put("SettingsButton", makeBootstrapButton("Settings", "btn-info"));
        buttons.put("QuitButton", makeBootstrapButton("Quit", "btn-danger")); //_QuitButton to make it appear last when adding to MAP
        buttons.put("PlayButton", makeBootstrapButton("Play", "btn-success"));



        //Setting the text to be added.
        text.setText("Kamel UNO");
        text = HeaderText.setTextProperties(text);
        VBox btnBox = new VBox();

        root.getChildren().add(text);
        btnBox.getChildren().add(buttons.get("PlayButton"));
        //btnBox.getChildren().add(buttons.get("SettingsButton"));
        btnBox.getChildren().add(buttons.get("QuitButton"));
        setButtonWidth();
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(30);
        btnBox.setLayoutX(bounds.getWidth()/2-bounds.getWidth() / 4);
        btnBox.setLayoutY(bounds.getHeight()/2);
        root.getChildren().add(btnBox);
        initializeSettingsWindow();

    }

    public void initializeButtonClicks(){
        Scene scene = ScreenController.getInstance().getMain();
        Stage stage = ScreenController.getInstance().getStage();
        onExitClick(scene);
        onSettingsClick(scene, stage, root);
        onSettingsButtonExit(scene, stage, root);
        onPlayClick(scene, stage);
    }

    private void onSettingsButtonExit(Scene scene, Stage stage, Group root){
        Button b = (Button) settingsWindow.getChildren().get(1);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                toggleSettingsWindow(scene,stage,root,actionEvent);
            }
        });
    }
    private void onSettingsClick(Scene scene, Stage stage, Group root) {
        Button settingsButton = buttons.get("SettingsButton");
        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                toggleSettingsWindow(scene,stage,root,actionEvent);
            }
        });
    }
    private boolean showSettings = false;
    private Pane settingsWindow;

    private void initializeSettingsWindow() {
        double rWidth = bounds.getWidth()/5;
        double rHeight = bounds.getHeight()/5;
        settingsWindow = new Pane();
        Rectangle r = new Rectangle();
        r.setHeight(rHeight);
        r.setWidth(rWidth);
        r.setFill(Color.rgb(255,255,255,0.7));
        Button b = makeBootstrapButton("Close Menu","btn-danger");
        b.setPrefWidth(rWidth/2);
        b.setPrefHeight(rHeight/2);
        b.setLayoutX(rWidth/4);
        b.setLayoutY(rHeight/4);
        settingsWindow.setLayoutX(bounds.getWidth() / 2 - rWidth / 2);
        settingsWindow.setLayoutY(bounds.getHeight()/2-rHeight/2);
        settingsWindow.getChildren().add(r);
        settingsWindow.getChildren().add(b);
    }

    private void toggleSettingsWindow(Scene scene, Stage stage, Group root, ActionEvent actionEvent){
        if (!showSettings) {
            actionEvent.consume();
            root.getChildren().add(settingsWindow);
            stage.setScene(scene);
            stage.show();
            showSettings = !showSettings;
            return;
        }
        root.getChildren().remove(settingsWindow);
        stage.setScene(scene);
        stage.show();
        showSettings = !showSettings;
    }
    private void onExitClick(Scene scene) {
        Button quitButton = buttons.get("QuitButton");
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage sb = (Stage) scene.getWindow();
                sb.close();
                System.exit(0);
            }
        });
    }
    private void onPlayClick(Scene scene, Stage stage) {
        Button quitButton = buttons.get("PlayButton");
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {

                PlayerNameView playerNameView = new PlayerNameView();
                ScreenController.getInstance().addScreen("playername",playerNameView.getPane());
                ScreenController.getInstance().activate("playername");

                /*GameView gameView = new GameView();
                ScreenController.getInstance().addScreen("game",gameView.getPane());
                ScreenController.getInstance().activate("game");*/
            }
        });
    }


    private void setButtonWidth() {
        int i = buttons.size();
        for (var entry : buttons.entrySet()) {
            entry.getValue().setMaxSize(bounds.getWidth(), bounds.getHeight() / 20);
            entry.getValue().setMinSize(bounds.getWidth() / 2, bounds.getHeight() / 20);
            entry.getValue().setPrefWidth(bounds.getWidth() / 2);
            i--;
        }
    }
}
