package com.kamelboyz.kameluno.Model;

import com.kamelboyz.kameluno.Settings.Settings;
import lombok.SneakyThrows;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class LobbyListHandler implements Runnable {
    private String uri = "tcp://"+ Settings.getInstance().getServerIp() +"/requestSpace?keep";
    private RemoteSpace lobbyListSpace;
    private Space lobbies;

    public LobbyListHandler(Space lobbies) throws IOException {
        lobbyListSpace = new RemoteSpace(uri);
        this.lobbies = lobbies;
    }


    @SneakyThrows
    @Override
    public void run() {
        Object[] resp;
        lobbyListSpace.put("lobby", "getLobbies", "all");
        resp = lobbyListSpace.get(new ActualField("getLobbies"), new FormalField(String[].class));
        String[] list = (String[]) resp[1];
        System.out.println("Retrieving lobbies");
        for (String s : list) {
            lobbies.put(s);
        }
        System.out.println("All lobbies retrieved");
        lobbies.put("fin");
    }
}


