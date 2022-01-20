package com.kamelboyz.kameluno.Settings;


public class Settings {
    private static Settings single_instance;
    private final String serverIp = "localhost:9001";

    public String getServerIp() {
        return serverIp;
    }

    private Settings(){
    }
    public static Settings getInstance(){
        if (single_instance == null)
            single_instance = new Settings();
        return single_instance;
    }
}
