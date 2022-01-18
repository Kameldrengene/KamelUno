package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.BootstrapButton;
import com.kamelboyz.kameluno.Model.HeaderText;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
public class LobbyCard {
    private final Pane pane = new Pane();
    private Map<String, Button> buttons = new HashMap<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private Map<String, Button> players = new HashMap<>();
    private Text text = new Text();
    private Button joinLobby;
    private Label lobbyLabel = new Label();
    private int lobbyId;

    public LobbyCard(int lobbyId) throws IOException {
        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        this.lobbyId = lobbyId;
        joinLobby = BootstrapButton.makeBootstrapButton("Join","btn-info");
        Rectangle lobbyBox = new Rectangle();
        double rWidth = 800;
        double rHeight = 50;
        this.lobbyLabel.setText(lobbyId+"");
        joinLobby.setLayoutX(700);
        joinLobby.setLayoutY(10);
        lobbyBox.setHeight(rHeight);
        lobbyBox.setWidth(rWidth);
        lobbyBox.setFill(Color.rgb(255,255,255,0.0));
        pane.getChildren().add(lobbyBox);
        pane.getChildren().add(joinLobby);
        initializeLabels();
        stage.setScene(scene);
        stage.show();
        onJoinClick();
    }
    private void initializeLabels(){
        lobbyLabel.setFont(new Font("Arial",24));
        lobbyLabel.setTextFill(Color.rgb(0,0,0,1));
        lobbyLabel.setLayoutY(10);
        lobbyLabel.setLayoutX(50);
        pane.getChildren().add(lobbyLabel);
    }
    private void onJoinClick(){
        joinLobby.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                LobbyView lobbyView = new LobbyView(lobbyId);
                ScreenController.getInstance().addScreen("lobby",lobbyView.getPane());
                ScreenController.getInstance().activate("lobby");
            }
        });
    }
}
