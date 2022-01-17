package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.Chat;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import java.io.IOException;
import java.util.ArrayList;

public class ChatView {
    private final Pane pane = new Pane();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();

    public ChatView() throws IOException {
        initializeChatWindow();
        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        pane.getChildren().add(chatWindow);
        stage.setScene(scene);
        stage.show();
        shortcutChat(scene);
    }

    private boolean isTyping = false;
    private void shortcutChat(Scene scene) {
        inputChat.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @SneakyThrows
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    chat.sendMessage();
                    chatWindow.requestFocus();
                    inputChat.setText("");
                    isTyping = false;
                    enableChat();
                }
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.T && !isTyping) {
                    keyEvent.consume();
                    enableChat();
                    isTyping = true;
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

    public VBox getChatWindow(){
        return chatWindow;
    }

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