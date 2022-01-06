package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.KeyPress.OnEscape;
import com.kamelboyz.kameluno.MainApp;
import com.kamelboyz.kameluno.Model.BootstrapButton;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.*;

import java.net.URL;
import java.util.ResourceBundle;

@Data
public class LobbyView {
    private final Pane pane = new Pane();
    public LobbyView(){
        pane.getChildren().add(BootstrapButton.makeBootstrapButton("Test", "btn-danger"));
    }

}
