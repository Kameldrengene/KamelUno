package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.Model.Chat;
import javafx.application.Platform;
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
    private String name;
    private int id;

    public ChatView(String name, int id) throws IOException {
        Stage stage = ScreenController.getInstance().getStage();
        Scene scene = ScreenController.getInstance().getMain();
        this.name = name;
        this.id = id;
        initializeChatWindow();
        pane.getChildren().add(chatWindow);
        stage.setScene(scene);
        stage.show();
        chatWindow.requestFocus();
        shortcutChat(scene);
    }

    private boolean isTyping = false;
    private void shortcutChat(Scene scene) {
        inputChat.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @SneakyThrows
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    if(inputChat.getText().length() > 0)
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
                if (keyEvent.getCode() == KeyCode.T && !isTyping) {
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
            isTyping = true;
            return;
        }
        isTyping = false;
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

    public TextArea getChatMessages() {
        return chatMessages;
    }

    private void initializeChatWindow() throws IOException {
        chat = new Chat(inputChat, clientChat, name, id);
        new Thread(chat).start();
        chatWindow = new VBox();
        chatWindow.setLayoutY(bounds.getHeight() - chatWindow.getLayoutBounds().getHeight() - bounds.getHeight() / 10);
        chatMessages.setEditable(false);
        chatMessages.setText("");
        inputChat.setEditable(true);
        chatMessages.setMaxHeight(bounds.getHeight() / 10);
        chatMessages.setMinHeight(bounds.getHeight() / 10);
        chatMessages.setPrefHeight(bounds.getHeight() / 10);
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
        Platform.setImplicitExit(false);
        Platform.runLater(()->{
            chatMessages.appendText(message +"\n");
            System.out.println("New message on UI");
        });
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for new msg");
                Object[] t = clientChat.get(new FormalField(String.class));
                updateChat(t[0]+"");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}