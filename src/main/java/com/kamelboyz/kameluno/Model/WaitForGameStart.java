package com.kamelboyz.kameluno.Model;

import com.kamelboyz.kameluno.Settings.Settings;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class WaitForGameStart implements Runnable {
    private Space space;
    private RemoteSpace remoteSpace;

    public WaitForGameStart(Space space, int id) throws IOException {
        this.space=space;
        remoteSpace = new RemoteSpace("tcp://"+ Settings.getInstance().getServerIp() +"/lobby" + id + "?keep");
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Running wait for game start!");
        Object[] t = remoteSpace.get(new ActualField(Player.getInstance().getName()), new ActualField("System"), new FormalField(String.class), new FormalField(String.class));
        String resp = t[1]+"";
        System.out.println("Game response: "+resp);
        space.put("go!");
    }
}
