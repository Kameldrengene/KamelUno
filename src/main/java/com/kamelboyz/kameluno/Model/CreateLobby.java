package com.kamelboyz.kameluno.Model;

import lombok.SneakyThrows;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class CreateLobby implements Runnable {
    private String uri = "tcp://127.0.0.1:9001/requestSpace?keep";
    private RemoteSpace lobbyListSpace;
    private Space lobbies;

    public CreateLobby(Space lobbies) throws IOException {
        lobbyListSpace = new RemoteSpace(uri);
        this.lobbies = lobbies;
    }


    @SneakyThrows
    @Override
    public void run() {
        lobbyListSpace.put("lobby","createLobby","");
        Object[] resp = lobbyListSpace.get(new FormalField(String.class));
        String the_resp = (String) resp[0];
        lobbies.put("fin");
    }
}
