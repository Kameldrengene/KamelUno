package com.kamelboyz.kameluno.Controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class ScreenController {
    private HashMap<String, Object> screenMap = new HashMap<>();
    private Scene main;
    private static ScreenController single_instance = null;

    private ScreenController() {}

    public static ScreenController getInstance() {
        if (single_instance == null)
            single_instance = new ScreenController();
        return single_instance;
    }

    public void init(Scene scene) {
        if (single_instance == null) {
            getInstance();
        }
        this.main = scene;
    }

    public void addScreen(String name, Object pane) {
        screenMap.put(name, pane);
    }

    public void removeScreen(String name) {
        screenMap.remove(name);
    }

    public void activate(String name) {
        main.setRoot((Parent) screenMap.get(name));
    }
}