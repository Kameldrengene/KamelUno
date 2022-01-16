package com.kamelboyz.kameluno_server.chat.chat2;

import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws InterruptedException {
        // create repository
        SpaceRepository repository = new SpaceRepository();

// Create a local space for the chat messages
        SequentialSpace chat = new SequentialSpace();

// Add the space to the repository
        repository.add("chat", chat);

// Open a gate
        repository.addGate("tcp://server:9001/?keep");

        ArrayList<String> chatters = new ArrayList<>();
        new Thread(new userHandler(chatters,chat)).start();

// Keep reading chat messages and printing them
        while (true) {
            Object[] t = chat.get(new FormalField(String.class), new FormalField(String.class));
            System.out.println(t[0] + ":" + t[1]);
            for(String u : chatters){
                chat.put(u, t[0], t[1]);
            }
        }
    }
}

class userHandler implements Runnable{
    ArrayList<String> users;
    SequentialSpace space;
    public userHandler(ArrayList<String> users, SequentialSpace space){
        this.users = users;
        this.space = space;
    }

    @Override
    public void run() {
        while (true){
            try {
                Object[] T = space.get(new FormalField(String.class));
                if(!users.contains((String)T[0]))
                {
                    System.out.println(T[0] + " has joined the chat!");
                    space.put("Server", T[0] + " has joined the chat!");
                    users.add((String)T[0]);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
