package com.kamelboyz.kameluno;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {

    private Map<String, Button> buttons = new HashMap<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();

    @Override
    public void start(Stage stage) throws IOException {


        //Creating objects
        Text text = new Text();
        buttons.put("PlayButton", makeBootstrapButton("Play", "btn-success"));
        buttons.put("SettingsButton", makeBootstrapButton("Settings", "btn-info"));
        buttons.put("_QuitButton", makeBootstrapButton("Quit", "btn-danger")); //_QuitButton to make it appear last when adding to MAP


        //Setting the text to be added.
        text.setText("Kamel UNO");
        text = setTextProperties(text);
        alignButtonsInMiddle();

        //Creating a Group object
        Group root = new Group();
        root.getChildren().add(text);
        for (var entry : buttons.entrySet()) {
            root.getChildren().add(entry.getValue());
        }
        /*for (Button b : buttons) {
            root.getChildren().add(b);
        }*/
        //Creating a scene object
        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        RadialGradient lg1 = new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK));
        scene.setFill(lg1);
        exitOnEsc(scene);
        onExitClick(scene);
        initializeSettingsWindow();
        onSettingsClick(scene, stage, root);
        onSettingsButtonExit(scene, stage, root);
        //Setting title to the Stage
        stage.setTitle("Kamel Ludo");
        stage.setFullScreen(true);
        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
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
        Button quitButton = buttons.get("_QuitButton");
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage sb = (Stage) scene.getWindow();
                sb.close();
            }
        });
    }

    private Text setTextProperties(Text text) {
        text.setFont(new Font(172));

        text.setTextAlignment(TextAlignment.CENTER);
        text.setX(bounds.getWidth() / 2 - text.getLayoutBounds().getWidth() / 2);
        text.setY(bounds.getHeight() / 7);
        text.setFill(Color.rgb(255, 255, 255, 0.7));
        Reflection reflection = new Reflection();
        reflection.setTopOffset(-33);
        reflection.setTopOpacity(0.75);
        reflection.setBottomOpacity(0.0);
        reflection.setFraction(0.7);
        text.setEffect(reflection);
        return text;
    }

    private void alignButtonsInMiddle() {
        int i = buttons.size();
        for (var entry : buttons.entrySet()) {
            entry.getValue().setMaxSize(bounds.getWidth(), bounds.getHeight() / 20);
            entry.getValue().setMinSize(bounds.getWidth() / 2, bounds.getHeight() / 20);
            entry.getValue().setPrefWidth(bounds.getWidth() / 2);
            entry.getValue().setLayoutY(((bounds.getHeight() / (20)) + (i * 80)) + bounds.getHeight() / 3);
            entry.getValue().setLayoutX((bounds.getWidth() / 4));
            i--;
        }
    }

    private Button makeBootstrapButton(String text, String buttonColor) {
        Button button = new Button(text);
        button.getStyleClass().setAll("btn", buttonColor);
        button.setStyle("-fx-alignment: CENTER;");
        return button;
    }

    private void exitOnEsc(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    Stage sb = (Stage) scene.getWindow();
                    sb.close();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}