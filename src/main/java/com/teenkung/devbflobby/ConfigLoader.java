package com.teenkung.devbflobby;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigLoader {

    private static String masterPassword;
    private static int timeout;
    private static boolean title;
    private static FileConfiguration config;

    public static int getTimeout() { return timeout; }

    public static boolean getTitle() { return title; }
    public static FileConfiguration getConfig() {
        return config;
    }
    public static String getMasterPassword() {
        return masterPassword;
    }
    public static void generateConfigFile() {
        DevBFLobby.getInstance().getConfig().options().copyDefaults(true);
        DevBFLobby.getInstance().saveDefaultConfig();
    }
    public static void loadConfig() {
        DevBFLobby instance = DevBFLobby.getInstance();
        FileConfiguration config = instance.getConfig();
        ConfigLoader.config = config;
        masterPassword = config.getString("options.master-password", "masterpassword");
        timeout = config.getInt("options.afk-timeout", 150);
        title = config.getBoolean("options.title", true);
    }


}
