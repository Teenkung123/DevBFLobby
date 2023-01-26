package com.teenkung.devbflobby.Commands;

import com.teenkung.devbflobby.AdminPassword;
import com.teenkung.devbflobby.ConfigLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class unlockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (AdminPassword.getIsOp(player)) {
                if (args.length == 1) {
                    if (args[0].equals(ConfigLoader.getMasterPassword())) {
                        sender.sendMessage(colorize(ConfigLoader.getConfig().getString("language.unlock.correct-password")));
                        AdminPassword.addToWhitelist(player);
                    } else {
                        sender.sendMessage(colorize(ConfigLoader.getConfig().getString("language.unlock.incorrect-password")));
                    }
                }
            } else {
                sender.sendMessage(colorize(ConfigLoader.getConfig().getString("language.unlock.non-op")));
            }
        } else {
            sender.sendMessage(colorize("&cThis command can only be executed by a player"));
        }

        return false;
    }
}
