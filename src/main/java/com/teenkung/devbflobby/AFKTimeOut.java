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

public class AFKTimeOut  implements Listener {

    private final Map<UUID, Integer> taskID = new HashMap<>();
    private final Map<UUID, Integer> counter = new HashMap<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Initialize the counter for the player to 0
        counter.putIfAbsent(uuid, 0);

        // Start the timer for the player
        int taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(DevBFLobby.getInstance(), () -> {
            int count = counter.get(uuid) + 1;
            counter.put(uuid, count);
            //check if player is a op or not
            if (player.isOp()) {
                Bukkit.getScheduler().cancelTask(taskID.get(uuid));
                taskID.remove(uuid);
                counter.remove(uuid);
            } else {
                if (count % 10 == 0) {
                    player.sendMessage(colorize(ConfigLoader.getConfig().getString("language.how-to-play")));
                }

                if (ConfigLoader.getTitle()) {
                    player.sendTitle(
                            colorize(ConfigLoader.getConfig().getString("language.title.title")),
                            colorize(ConfigLoader.getConfig().getString("language.title.subtitle")),
                            0,
                            25,
                            0
                    );
                }
                // If the counter exceeds 150 seconds, kick the player and stop the timer
                if (count >= ConfigLoader.getTimeout()) {
                    Bukkit.getScheduler().runTask(DevBFLobby.getInstance(), () -> {
                        player.kickPlayer(colorize(ConfigLoader.getConfig().getString("language.kick-message")));
                    });
                }
            }
        }, 0, 20).getTaskId();

        // Store the task ID for the player
        taskID.put(uuid, taskId);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Stop the timer for the player and remove it from the map
        if (taskID.containsKey(uuid)) {
            Bukkit.getScheduler().cancelTask(taskID.get(uuid));
            taskID.remove(uuid);
        }
        counter.remove(uuid);
    }

}