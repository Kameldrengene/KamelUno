package com.kamelboyz.kameluno.KeyPress;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.Optional;

public class OnEscape {
    public static void exitOnEsc(Scene scene, Stage stage) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
//                    Stage sb = (Stage) scene.getWindow();
//                    sb.close();
//                    System.exit(0);
                    Alert nalert = new Alert(Alert.AlertType.NONE,"Exit Game ?", ButtonType.YES, ButtonType.NO);
                    nalert.setTitle("Kamel Uno");
                    Optional<ButtonType> option = nalert.showAndWait();

                    if(option.get()==ButtonType.YES){
                        Stage sb = (Stage) scene.getWindow();
                        sb.close();
                        System.exit(0);
                    } else{
                        stage.setFullScreen(true);
                    }
                }
            }
        });
    }
}