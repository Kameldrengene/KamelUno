package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.BootstrapButton;
import com.kamelboyz.kameluno.Model.HeaderText;
import com.kamelboyz.kameluno.Model.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PlayerNameView {
    private final Pane pane = new Pane();
    private Map<String, Button> buttons = new HashMap<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private Map<String, Button> players = new HashMap<>();
    private Text text = new Text();
    private TextField playerName = new TextField();

    public PlayerNameView() throws IOException {
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(bgFill));
        text.setText("Enter Name");
        text = HeaderText.setTextProperties(text);
        pane.getChildren().add(text);
        playerName.setPrefWidth(bounds.getWidth()/5);
        playerName.setLayoutX(bounds.getWidth()/2-playerName.getPrefWidth()/2);
        playerName.setLayoutY(bounds.getHeight()/2);
        pane.getChildren().add(playerName);
        playerName.requestFocus();
        playerName.setEditable(true);

        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        onEnter();
        stage.setScene(scene);
        stage.show();
    }

    public Pane getPane() {
        return pane;
    }

    private void onEnter(){
        playerName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @SneakyThrows
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER && playerName.getText().length() >= 1) {
                    Player.getInstance().setName(playerName.getText());
                    LobbyListView lobbyListView = new LobbyListView();
                    ScreenController.getInstance().addScreen("lobbylist",lobbyListView.getPane());
                    ScreenController.getInstance().activate("lobbylist");
                }
            }
        });
    }
}

