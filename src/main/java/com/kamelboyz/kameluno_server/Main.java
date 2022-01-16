package com.kamelboyz.kameluno_server;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        //Server.getInstance().addChatRoom();
        TestRunner testRunner = new TestRunner();
        testRunner.run();
    }
    static class TestRunner implements Runnable{
        public TestRunner(){}
        @Override
        public void run() {
            System.out.println("Hello from thread!");
        }
    }
}
