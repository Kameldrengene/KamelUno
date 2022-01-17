package com.kamelboyz.kameluno.Model;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jspace.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class Chat implements Runnable {
    private TextField input;
    private String uri = "tcp://127.0.0.1:9001/chat0?keep";
    private RemoteSpace chat;
    private String name;
    private Space clientChat;
    public Chat(TextField input, Space clientChat, String name) throws IOException {
        this.input = input;
        this.chat = new RemoteSpace(uri);
        this.name = name;
        this.clientChat = clientChat;
    }

    @Override
    public void run() {
        try {


            // Set the URI of the chat space
            // Default value
            // Default value



            // Connect to the remote chat space



            // Keep sending whatever the user types
            System.out.println("Start chatting...");
            new Thread(new getChatMessages(chat, name, clientChat)).start();
            chat.put(name);



        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage() throws InterruptedException {
        String message = input.getText();
        chat.put(name, message);
    }
}

class getChatMessages implements Runnable {
    RemoteSpace chat;
    Space localChat;
    String me;

    public getChatMessages(RemoteSpace chat, String me, Space localChat) {
        this.chat = chat;
        this.me = me;
        this.localChat = localChat;
    }

    @Override
    public void run() {
        System.out.println("Starting readChat thread");
        while (true) {
            Object[] t = new Object[0];
            try {
                t = chat.get(new ActualField(me), new FormalField(String.class), new FormalField(String.class));
                System.out.println("Got new message!");
                localChat.put(t[1] + ": " + t[2]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t[1] + ":" + t[2]);
        }
    }
}