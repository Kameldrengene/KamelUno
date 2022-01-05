package com.kamelboyz.kameluno;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        Text text = new Text();
        text.setText("Hello World!");
        text.setX(50);
        text.setY(50);
        Group root = new Group(text);
        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        exitOnEsc(scene);

        stage.setFullScreen(true);
        stage.setTitle("Kamel Ludo!");
        stage.setScene(scene);
        stage.show();

    }

    private void exitOnEsc(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()== KeyCode.ESCAPE){
                    Stage sb = (Stage)scene.getWindow();
                    sb.close();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}