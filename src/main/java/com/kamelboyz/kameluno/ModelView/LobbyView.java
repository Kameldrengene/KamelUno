package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.KeyPress.OnEscape;
import com.kamelboyz.kameluno.MainApp;
import com.kamelboyz.kameluno.Model.BootstrapButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Data
public class LobbyView {
    private final Pane pane = new Pane();
    private Map<String, Button> buttons = new HashMap<>();

    public LobbyView() {
        buttons.put("test",BootstrapButton.makeBootstrapButton("Test", "btn-danger"));
        pane.getChildren().add(buttons.get("test"));
    }

    public void initializeButtonClicks(Scene scene, Stage stage) {
        onTestClick();
    }

    private void onTestClick() {
        buttons.get("test").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScreenController.getInstance().activate("main");
            }
        });
    }
}
