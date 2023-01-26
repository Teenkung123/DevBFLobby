package com.teenkung.devbflobby;

import com.teenkung.devbflobby.Commands.unlockCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import com.iridium.iridiumcolorapi.IridiumColorAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DevBFLobby extends JavaPlugin {

    private static DevBFLobby instance;
    @Override
    public void onEnable() {

        instance = this;
        ConfigLoader.generateConfigFile();
        ConfigLoader.loadConfig();

        //Register Events
        Bukkit.getPluginManager().registerEvents(new AFKTimeOut(), this);
        Bukkit.getPluginManager().registerEvents(new AdminPassword(), this);

        getCommand("unlock").setExecutor(new unlockCommand());

        AdminPassword.startChecker();

    }

    @Override
    public void onDisable() {

    }

    public static DevBFLobby getInstance() {
        return instance;
    }

    public static String colorize(String message) {
        if (message == null) {
            message = "";
        }
        return IridiumColorAPI.process(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static List<String> colorizeList(List<String> list) {
        List<String> ret = new ArrayList<>();
        for (String s : list) {
            ret.add(colorize(s));
        }
        return ret;
    }


}
