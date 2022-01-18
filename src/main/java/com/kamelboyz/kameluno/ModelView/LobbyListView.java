package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import lombok.SneakyThrows;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

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
    private VBox vBox = new VBox();
    private VBox vBoxHeader = new VBox();
    private Button createLobby;
    public LobbyListView() throws IOException {
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(bgFill));
        createLobby = BootstrapButton.makeBootstrapButton("Create Lobby", "btn-success");
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
        Label lobbyLabel = new Label("LobbyID");
        lobbyLabel.setFont(new Font("Arial",32));
        lobbyLabel.setTextFill(Color.rgb(0,0,0,1));
        HBox hBox = new HBox();
        new Thread(new LobbyUpdater(this)).start();
        hBox.getChildren().add(lobbyLabel);
        hBox.setSpacing(40);
        hBox.setPadding(new Insets(0, 0, 0, 50));
        hBox.getChildren().add(createLobby);
        vBoxHeader.getChildren().add(hBox);
        vBox.setLayoutY(bounds.getHeight()/3+30);
        vBox.setLayoutX(bounds.getWidth()/2-rWidth/2);
        vBoxHeader.setLayoutY(bounds.getHeight()/3+30);
        vBoxHeader.setLayoutX(bounds.getWidth()/2-rWidth/2);
        vBoxHeader.getChildren().add(vBox);
        pane.getChildren().add(vBoxHeader);
        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        stage.setScene(scene);
        stage.show();
        createLobby(this);
    }
    private void createLobby(LobbyListView lobbyListView){
        createLobby.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new Thread(new LobbyCreator(lobbyListView)).start();
            }
        });
    }

    public void updateVBox(ArrayList<String> lobbies) throws IOException {
        Platform.runLater(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                vBox.getChildren().clear();
                for (String lb : lobbies){
                    vBox.getChildren().add(new LobbyCard(Integer.parseInt(lb)).getPane());
                }
            }
        });

    }
}

class LobbyCreator implements Runnable{
    private Space lobbies = new SequentialSpace();
    LobbyListView lobbyListView;
    public LobbyCreator(LobbyListView lobbyListView){
        this.lobbyListView = lobbyListView;
    }

    @SneakyThrows
    @Override
    public void run() {
        new Thread(new CreateLobby(lobbies)).start();

    }
}



class LobbyUpdater implements Runnable{
    private Space lobbies = new SequentialSpace();
    private ArrayList<String> lobbyList = new ArrayList<>();
    private LobbyListView lobbyListView;
    public LobbyUpdater(LobbyListView lobbyListView){
        this.lobbyListView = lobbyListView;
    }
    @SneakyThrows
    @Override
    public void run() {
        new Thread(new LobbyListHandler(lobbies)).start();
        while (true){
            Object[] t = lobbies.get(new FormalField(String.class));
            String s = t[0]+"";
            if(s.equals("fin")){
                lobbyListView.updateVBox(lobbyList);
                System.out.println("Got all lobbies, stopping");
                return;
            }
            lobbyList.add(s);
        }
    }
}
