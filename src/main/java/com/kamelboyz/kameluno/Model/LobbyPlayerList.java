package com.kamelboyz.kameluno.Model;

import com.kamelboyz.kameluno.Settings.Settings;
import lombok.SneakyThrows;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;
import java.util.ArrayList;

public class LobbyPlayerList {
    private RemoteSpace lobbySpace;
    private Space localSpace;
    private int id;

    public LobbyPlayerList(Space localSpace, int id) throws IOException {
        lobbySpace = new RemoteSpace("tcp://"+ Settings.getInstance().getServerIp() +"/lobby" + id + "?keep");

        this.localSpace = localSpace;
        this.id = id;
    }

    public void loadPlayers() throws InterruptedException {
        lobbySpace.put("getPlayers", "");
        Object[] t = lobbySpace.get(new FormalField(String.class));
        localSpace.put(t[0]);
        System.out.println("Players: " + t[0]);

    }

}
