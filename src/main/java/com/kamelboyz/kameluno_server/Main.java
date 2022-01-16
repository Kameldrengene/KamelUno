package com.kamelboyz.kameluno_server;

import com.kamelboyz.kameluno_server.chat.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Server.getInstance().addChatRoom();
        Server.getInstance().addChatRoom();
        System.out.println("Hello from bottom");
        //TestRunner testRunner = new TestRunner();
        //testRunner.run();
    }
}
