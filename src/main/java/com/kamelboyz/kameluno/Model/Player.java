package com.kamelboyz.kameluno.Model;

import com.kamelboyz.kameluno.Controller.ScreenController;

public class Player {
    private static Player single_instance;
    private String name;
    private Player(){
    }
    public static Player getInstance(){
        if (single_instance == null)
            single_instance = new Player();
        return single_instance;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
