package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.*;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import java.io.IOException;
import java.util.*;

@Data
public class LobbyView {
    private final Pane pane = new Pane();
    private Map<String, Button> buttons = new HashMap<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private Map<String, Button> players = new HashMap<>();
    private Text text = new Text();
    private int lobbyId;

    public LobbyView(int lobbyId) throws IOException {
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(bgFill));
        text.setText("Lobby");
        this.lobbyId = lobbyId;
        text = HeaderText.setTextProperties(text);
        pane.getChildren().add(text);
        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        stage.setScene(scene);
        stage.show();
        onLobbyClick();
        new Thread(new PlayerUpdater(this)).start();
        try{
            ChatView chatView = new ChatView(Player.getInstance().getName(), lobbyId);
            pane.getChildren().add(chatView.getChatWindow());
            chatView.getChatWindow().requestFocus();
        } catch (Exception e){
            System.out.println("Could not connect to chat server");
            e.printStackTrace();
        }

    }


    //Should load players from tuple space in a thread
    public void setPlayers(List<String> tempPlayers) {
        addPlayerButtons(tempPlayers);
    }

    private void onLobbyClick(){
//        players.get("Mark").setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                GameView gameView = new GameView();
//                ScreenController.getInstance().addScreen("game",gameView.getPane());
//                ScreenController.getInstance().activate("game");
//            }
//        });
    }
    private void addPlayerButtons(List<String> players) {
        this.players.clear();
        for (String p : players) {
            this.players.put(p, BootstrapButton.makeBootstrapButton(p, "btn-success"));
        }
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                alignPlayers();
            }
        });

    }

    private void alignPlayers() {
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
}

class PlayerUpdater implements Runnable{
    private LobbyView lobbyView;
    private Space space = new SequentialSpace();
    public PlayerUpdater(LobbyView lobbyView){
        this.lobbyView = lobbyView;
    }
    @SneakyThrows
    @Override
    public void run() {
        while (true){
            Thread.sleep(500);
            LobbyPlayerList lobbyPlayerList = new LobbyPlayerList(space, lobbyView.getLobbyId());
            lobbyPlayerList.loadPlayers();
            String temp = space.get(new FormalField(String.class))[0]+"";
            System.out.println("Players in LobbyView!: " + temp);
            temp = temp.replaceAll("\\[", "").replaceAll("\\]","");
            lobbyView.setPlayers(new ArrayList<>(List.of(temp.split("\\s*,\\s*"))));
        }
    }
}
