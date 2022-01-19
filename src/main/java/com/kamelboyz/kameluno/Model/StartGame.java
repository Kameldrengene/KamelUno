package com.kamelboyz.kameluno.Model;

import com.kamelboyz.kameluno.Settings.Settings;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class StartGame implements Runnable{
    private Space space;
    private RemoteSpace serverSpace;
    public StartGame(Space space, int id) throws IOException {
        this.space = space;
        serverSpace = new RemoteSpace("tcp://"+ Settings.getInstance().getServerIp() +"/lobby" + id + "?keep");
    }
    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Putting init game");
        serverSpace.put("initGame","");
    }
}
