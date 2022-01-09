package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.KeyPress.OnEscape;
import com.kamelboyz.kameluno.MainApp;
import com.kamelboyz.kameluno.Model.BootstrapButton;
import com.kamelboyz.kameluno.Model.HeaderText;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.*;

import java.net.URL;
import java.util.*;

@Data
public class LobbyView {
    private final Pane pane = new Pane();
    private Map<String, Button> buttons = new HashMap<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private Map<String,Button> players = new HashMap<>();
    private Text text = new Text();
    public LobbyView() {
        buttons.put("test",BootstrapButton.makeBootstrapButton("Test", "btn-danger"));
        pane.getChildren().add(buttons.get("test"));
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(bgFill));
        text.setText("Lobby");
        text = HeaderText.setTextProperties(text);
        pane.getChildren().add(text);
        setPlayers();
    }
    //Should load players from tuple space in a thread
    private void setPlayers(){
        List<String> tempPlayers = new ArrayList<>(List.of("Mark","Volkan","Mikkel","Talha"));
        addPlayerButtons(tempPlayers);
    }

    private void addPlayerButtons(List<String> players){
        this.players.clear();
        for(String p : players){
            this.players.put(p,BootstrapButton.makeBootstrapButton(p,"btn-success"));
        }
        alignPlayers();
    }

    private void alignPlayers(){
        int i = 0;
        for (var entry : players.entrySet()) {
            entry.getValue().setMaxSize(bounds.getWidth(), bounds.getHeight() / 20);
            entry.getValue().setMinSize(bounds.getWidth() / 2, bounds.getHeight() / 20);
            entry.getValue().setPrefWidth(bounds.getWidth() / 2);
            entry.getValue().setLayoutY(((bounds.getHeight() / (20)) + (i * 80)) + bounds.getHeight() / 2);
            entry.getValue().setLayoutX((bounds.getWidth() / 4));
            i--;
            pane.getChildren().add(entry.getValue());
        }
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
