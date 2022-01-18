package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.BootstrapButton;
import com.kamelboyz.kameluno.Model.HeaderText;
import com.kamelboyz.kameluno.Model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class LobbyListView {
    private final Pane pane = new Pane();
    private Map<String, Button> buttons = new HashMap<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private Map<String, Button> players = new HashMap<>();
    private Text text = new Text();

    public LobbyListView() throws IOException {
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(bgFill));
        text.setText("Lobby");
        text = HeaderText.setTextProperties(text);
        pane.getChildren().add(text);
        Rectangle lobbyBox = new Rectangle();
        double rWidth = 800;
        double rHeight = 600;
        lobbyBox.setHeight(rHeight);
        lobbyBox.setWidth(rWidth);
        lobbyBox.setFill(Color.rgb(255,255,255,0.3));
        lobbyBox.setLayoutX(bounds.getWidth()/2-rWidth/2);
        lobbyBox.setLayoutY(bounds.getHeight()/3);
        pane.getChildren().add(lobbyBox);
        Label leaderName = new Label("Leader");
        Label gameName = new Label("Gamename");
        Label participants = new Label("Participants");
        leaderName.setFont(new Font("Arial",32));
        gameName.setFont(new Font("Arial",32));
        participants.setFont(new Font("Arial",32));
        leaderName.setTextFill(Color.rgb(0,0,0,1));
        gameName.setTextFill(Color.rgb(0,0,0,1));
        participants.setTextFill(Color.rgb(0,0,0,1));


        LobbyCard lobbyCard = new LobbyCard("Mark", "Marks seje spil", "3");
        LobbyCard lobbyCard2 = new LobbyCard("Mark2", "Marks2 seje spil", "3");
        LobbyCard lobbyCard3 = new LobbyCard("Mark3", "Marks2 seje spil", "3");
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        hBox.getChildren().add(leaderName);
        hBox.getChildren().add(gameName);
        hBox.getChildren().add(new Label());
        hBox.getChildren().add(new Label());
        hBox.getChildren().add(new Label());
        hBox.getChildren().add(participants);
        hBox.setSpacing(40);
        hBox.setPadding(new Insets(0, 0, 0, 5));
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(lobbyCard.getPane());
        vBox.getChildren().add(lobbyCard2.getPane());
        vBox.getChildren().add(lobbyCard3.getPane());
        vBox.setLayoutY(bounds.getHeight()/3+30);
        vBox.setLayoutX(bounds.getWidth()/2-rWidth/2);
        pane.getChildren().add(vBox);
        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        stage.setScene(scene);
        stage.show();
    }


}
