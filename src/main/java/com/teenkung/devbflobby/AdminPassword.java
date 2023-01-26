package com.teenkung.devbflobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.teenkung.devbflobby.DevBFLobby.colorize;

public class AdminPassword implements Listener {

    private static final Map<UUID, Boolean> whiteList = new HashMap<>();
    //Variable that will contain whitelist of player who already get op unlocked.
    // - Will remove after they quit the server
    // - Will set as true when player type the master password correct
    private static final Map<UUID, Boolean> isOp = new HashMap<>();
    //Variable that will contain list of all player who has op

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        //check if player has op or not
        if (player.isOp()) {
            //if player has op: deop them and add to isOp variable: true
            player.setOp(false);
            isOp.put(uuid, true);
            System.out.println();
        } else {
            //if not: add to isOp variable: false
            isOp.put(uuid, false);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        //check if player has isOp variable true or not
        if (isOp.getOrDefault(uuid, false)) {
            //if true: set op to true remove from whitelist remove from isOp
            whiteList.remove(uuid);
            isOp.remove(uuid);
            player.setOp(true);
        } else {
            player.setOp(false);
        }
    }

    public static Boolean getIsOp(Player player) {
        return isOp.getOrDefault(player.getUniqueId(), false);
    }
    public static void addToWhitelist(Player player) {
        UUID uuid = player.getUniqueId();
        whiteList.put(uuid, true);
        player.setOp(true);
    }

    public static void removeFromWhitelist(Player player) {
        UUID uuid = player.getUniqueId();
        whiteList.remove(uuid);
        player.setOp(false);
    }
    public static void startChecker() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(DevBFLobby.getInstance(), () -> {
            //loop all players
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                //check if player is op or not if yes check if in whitelist or not if not in whitelist set op to false and make them need master password
                if (player.isOp()) {
                    if (!whiteList.getOrDefault(uuid, false)) {
                        player.sendMessage(colorize(ConfigLoader.getConfig().getString("language.unlock.get-op")));
                        synchronized (DevBFLobby.getInstance()) {
                            player.setOp(false);
                            isOp.put(uuid, true);
                        }


                    }
                } else {
                    //if player don't have op and isOp Variable have the value: true
                    if (isOp.getOrDefault(uuid, false)) {
                        //if whitelist variable is true
                        if (whiteList.getOrDefault(uuid, false)) {
                            //remove player from op and whitelist and isOp variable
                            synchronized (DevBFLobby.getInstance()) {
                                removeFromWhitelist(player);
                                isOp.remove(uuid);
                            }
                        }
                    }
                }
            }
        }, 0, 10);
    }
}