package com.kamelboyz.kameluno.Model;

import lombok.SneakyThrows;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class LobbyListHandler implements Runnable {
    private String uri = "tcp://127.0.0.1:9001/requestSpace?keep";
    private RemoteSpace lobbyListSpace;
    private Space lobbies;

    public LobbyListHandler(Space lobbies) throws IOException {
        lobbyListSpace = new RemoteSpace(uri);
        this.lobbies = lobbies;
    }


    @SneakyThrows
    @Override
    public void run() {
        lobbyListSpace.put("lobby","createLobby","1");
        Object[] resp = lobbyListSpace.get(new FormalField(String.class));
        String the_resp = (String) resp[0];
        lobbyListSpace.put("lobby", "getLobbies", "all");
        resp = lobbyListSpace.get(new ActualField("getLobbies"), new FormalField(String[].class));
        String[] list = (String[]) resp[1];
        for (String s : list) {
            lobbies.put(s);
        }
        lobbies.put("fin");
    }
}
