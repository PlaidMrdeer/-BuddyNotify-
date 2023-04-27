package com.plaidmrdeer.bn.commands;

import com.plaidmrdeer.bn.BuddyNotify;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author PlaidMrdeer
 */
public class ReloadCommand implements CommandExecutor {
    private final BuddyNotify instance;

    public ReloadCommand(BuddyNotify instance) {
        this.instance = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        String reloadPermissions = "BuddyNotify.command.bnreload";

        if (!sender.hasPermission(reloadPermissions)) {
            instance.sendMessage("no_permissions", sender);
            return true;
        }

        instance.reloadPlugin();

        instance.sendMessage("reload_complete", sender);

        return true;
    }
}
