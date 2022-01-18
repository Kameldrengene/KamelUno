package com.kamelboyz.kameluno.Model;

import lombok.SneakyThrows;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class LobbyJoin implements Runnable{
    private String uri = "tcp://127.0.0.1:9001/requestSpace?keep";
    private RemoteSpace lobbyListSpace;
    private RemoteSpace lobbySpace;
    private Space localSpace;
    private int id;

    public LobbyJoin(Space localSpace, int id) throws IOException {
        lobbyListSpace = new RemoteSpace(uri);
        lobbySpace = new RemoteSpace("tcp://127.0.0.1:9001/lobby" + id + "?keep");

        this.localSpace = localSpace;
        this.id = id;
    }


    @SneakyThrows
    @Override
    public void run() {
        lobbyListSpace.put("lobby","joinLobby",id+"");
        Object[] resp = lobbyListSpace.get(new FormalField(String.class));
        String the_resp = (String) resp[0];
        if (the_resp.equals("oklobby")){
            lobbySpace.put("joined",Player.getInstance().getName());
            localSpace.put("joined");
        }
    }
}
