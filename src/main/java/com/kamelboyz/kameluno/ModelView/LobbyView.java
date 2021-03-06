package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import org.jspace.ActualField;
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
    private VBox playerBox = new VBox();
    private VBox headerBox = new VBox();
    private Button startButton;
    private boolean inLobby;
    private Space space = new SequentialSpace();
    private ChatView chatView;


    public LobbyView(int lobbyId) throws IOException {
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(bgFill));
        inLobby = true;
        startButton = BootstrapButton.makeBootstrapButton("0/4", "btn-info");
        text.setText("Lobby");
        this.lobbyId = lobbyId;
        text = HeaderText.setTextProperties(text);
        pane.getChildren().add(text);
        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        stage.setScene(scene);
        stage.show();
        onLobbyStartClick(this);
        headerBox.setLayoutX(bounds.getWidth()/4);
        headerBox.setLayoutY(bounds.getHeight()/3);
        headerBox.setSpacing(10);
        playerBox.setSpacing(10);
        headerBox.getChildren().add(startButton);
        headerBox.getChildren().add(playerBox);
        pane.getChildren().add(headerBox);
        new Thread(new PlayerUpdater(this)).start();
        System.out.println("WE ARE HERE!");
        new Thread(new WaitForGame(space,this)).start();
        try{
            chatView = new ChatView(Player.getInstance().getName(), lobbyId);
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
    private boolean gameStarting = false;
    private void onLobbyStartClick(LobbyView lobbyView){
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                gameStarting = true;
                int attempts = 0;
                while (gameStarting && attempts < 1){
                    new Thread(new StartGame(space,lobbyView.getLobbyId())).start();
                    attempts++;
                }
            }
        });
    }

    public void loadGame(){
        Platform.runLater(()->{
            inLobby = false;
            try{
                GamePlay gamePlay = new GamePlay(lobbyId, chatView);
                ScreenController.getInstance().addScreen("game",gamePlay.getGameBoard().getPane());
                ScreenController.getInstance().activate("game");
            }catch (IOException | InterruptedException e){
                e.printStackTrace();
            }

        });
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
                if(players.size() < 4){
                    startButton.setText(players.size()+"/4");
                } else {
                    startButton.setText("Start");
                }
            }
        });

    }

    private void alignPlayers() {
        playerBox.getChildren().clear();
        for (var entry : players.entrySet()) {
            entry.getValue().setMaxSize(bounds.getWidth(), bounds.getHeight() / 20);
            entry.getValue().setMinSize(bounds.getWidth() / 2, bounds.getHeight() / 20);
            entry.getValue().setPrefWidth(bounds.getWidth() / 2);
            playerBox.getChildren().add(entry.getValue());
        }
    }
}
@AllArgsConstructor
class WaitForGame implements Runnable{
    private Space space;
    private LobbyView lobbyView;
    @SneakyThrows
    @Override
    public void run() {
        new Thread(new WaitForGameStart(space, lobbyView.getLobbyId())).start();
        space.get(new ActualField("go!"));
        lobbyView.setGameStarting(false);
        System.out.println("Starting game");
        lobbyView.loadGame();
        System.out.println("Game started");
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
        Thread.sleep(500);
        LobbyPlayerList lobbyPlayerList = new LobbyPlayerList(space, lobbyView.getLobbyId());
        while (lobbyView.isInLobby()){
            lobbyPlayerList.loadPlayers();
            System.out.println(lobbyView.isInLobby());
            String temp = space.get(new FormalField(String.class))[0]+"";
//            System.out.println("Players in LobbyView!: " + temp);
            temp = temp.replaceAll("\\[", "").replaceAll("\\]","");
            lobbyView.setPlayers(new ArrayList<>(List.of(temp.split("\\s*,\\s*"))));
            Thread.sleep(5000);
        }
    }
}
