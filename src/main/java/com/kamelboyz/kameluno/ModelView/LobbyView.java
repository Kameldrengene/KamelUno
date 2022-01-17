package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.BootstrapButton;
import com.kamelboyz.kameluno.Model.Chat;
import com.kamelboyz.kameluno.Model.HeaderText;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
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

import static com.kamelboyz.kameluno.Model.BootstrapButton.makeBootstrapButton;

@Data
public class LobbyView {
    private final Pane pane = new Pane();
    private Map<String, Button> buttons = new HashMap<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private Map<String, Button> players = new HashMap<>();
    private Text text = new Text();

    public LobbyView() throws IOException {
        buttons.put("test", BootstrapButton.makeBootstrapButton("Test", "btn-danger"));
        pane.getChildren().add(buttons.get("test"));
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(bgFill));
        text.setText("Lobby");
        text = HeaderText.setTextProperties(text);
        pane.getChildren().add(text);
        setPlayers();
        initializeChatWindow();
        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        pane.getChildren().add(chatWindow);
        stage.setScene(scene);
        stage.show();
        shortcutChat(scene);
    }

    //Should load players from tuple space in a thread
    private void setPlayers() {
        List<String> tempPlayers = new ArrayList<>(List.of("Mark", "Volkan", "Mikkel", "Talha"));
        addPlayerButtons(tempPlayers);
    }

    private void addPlayerButtons(List<String> players) {
        this.players.clear();
        for (String p : players) {
            this.players.put(p, BootstrapButton.makeBootstrapButton(p, "btn-success"));
        }
        alignPlayers();
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


    private void shortcutChat(Scene scene) {
        inputChat.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @SneakyThrows
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    chat.sendMessage();
                    chatWindow.requestFocus();
                    inputChat.setText("");
                    enableChat();
                }
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.T) {
                    keyEvent.consume();
                    enableChat();
                }
            }
        });
    }

    private void enableChat() {
        if (!showChat) {
            inputChat.setEditable(true);
            inputChat.requestFocus();
            showChat = !showChat;
            return;
        }
        inputChat.setEditable(false);
        showChat = !showChat;
    }

    private boolean showChat = false;
    private VBox chatWindow;
    private TextField inputChat = new TextField();
    private Chat chat;
    private TextArea chatMessages = new TextArea();
    private Space clientChat = new SequentialSpace();

    private void initializeChatWindow() throws IOException {
        chat = new Chat(inputChat, clientChat);
        new Thread(chat).start();
        chatWindow = new VBox();
        chatWindow.setLayoutY(bounds.getHeight() - chatWindow.getLayoutBounds().getHeight() - bounds.getHeight() / 10);
        chatMessages.setEditable(false);
        inputChat.setEditable(true);
        chatMessages.setMaxHeight(bounds.getHeight() / 10);
        chatMessages.setMinHeight(bounds.getHeight() / 10);
        chatMessages.setPrefHeight(bounds.getHeight() / 10);
        chatMessages.setText("Hej Test \nNew Line???");
        chatWindow.getChildren().add(chatMessages);
        chatWindow.getChildren().add(inputChat);
        new Thread(new ChatUpdater(chatMessages,clientChat)).start();
    }

}

class ChatUpdater implements Runnable {
    private TextArea chatMessages;
    private Space clientChat;
    private ArrayList<String> messages = new ArrayList<>();

    public ChatUpdater(TextArea chatMessages, Space clientChat) {
        this.chatMessages = chatMessages;
        this.clientChat = clientChat;
    }

    private void updateChat(String message) {
        messages.add(message);
        String finalMessage = "";
        if(messages.size() < 8){
            for(int i = 0; i < messages.size();i++){
                finalMessage += messages.get(i)+"\n";
            }
        } else{
            for(int i = messages.size()-8; i < messages.size(); i++){
                finalMessage += messages.get(i)+"\n";
            }
        }
        chatMessages.setText(finalMessage);
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for new msg");
                Object[] t = clientChat.get(new FormalField(String.class));
                updateChat(t[0]+"");
                System.out.println(t[0]+"");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
