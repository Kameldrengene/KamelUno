package com.kamelboyz.kameluno.Model;

import com.kamelboyz.kameluno.Settings.Settings;
import lombok.SneakyThrows;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class StartGame implements Runnable{
    private Space space;
    private int id;
    private RemoteSpace serverSpace = new RemoteSpace("tcp://"+ Settings.getInstance().getServerIp() +"/lobby" + id + "?keep");
    public StartGame(Space space, int id) throws IOException {
        this.space = space;
        this.id = id;
    }

    @SneakyThrows
    @Override
    public void run() {
        serverSpace.put("initGame","");
        Object[] t = serverSpace.get(new ActualField("initGame"), new FormalField(String.class));
        String resp = t[1]+"";
        System.out.println("Game response: "+resp);
        space.put("go!");
    }
}
