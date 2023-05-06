package com.plaidmrdeer.bn.events;

import com.plaidmrdeer.bn.BuddyNotify;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author PlaidMrdeer
 */
public final class HintListener implements Listener {
    private final BuddyNotify instance;

    public HintListener(BuddyNotify instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onHint(AsyncPlayerChatEvent event) {
        Player rawPlayer = event.getPlayer();
        String[] message = ChatColor.stripColor(event.getMessage()).split(" ");

        String at = "@";
        if (!at.equals(message[0])) {
            return;
        }

        event.setCancelled(true);

        if (message.length != 2) {
            instance.sendMessage("format_error", rawPlayer);
            return;
        }

        String allPlayer = "all";
        if (allPlayer.equalsIgnoreCase(message[1])) {
            instance.getDataManager().removeNumber(
                    rawPlayer, 1
            );

            if (instance.getDataManager().getNumber(rawPlayer) <= 0) {
                instance.sendMessage("mention_quota", rawPlayer);
                return;
            }

            String allPrompt = instance.getConfig().getString("prompt.all_message");
            instance.addPlaceholder("%all_player%", rawPlayer.getName());
            Bukkit.broadcastMessage(instance.setStyle(allPrompt));

            for (Player player : Bukkit.getOnlinePlayers()) {
                playSound(player);
            }
            return;
        }

        Player target = Bukkit.getPlayerExact(message[1]);
        if (target == null) {
            instance.sendMessage("cannot_found_player", rawPlayer);
            return;
        }

        String prompt = instance.getConfig().getString("prompt.message");
        instance.addPlaceholder("%player%", rawPlayer.getName());
        instance.addPlaceholder("%target_player%", target.getName());
        Bukkit.broadcastMessage(instance.setStyle(prompt));

        playSound(target);
    }

    private void playSound(Player player) {
        String soundOf = instance.getConfig().getString("prompt.sound.id").toUpperCase();
        Sound sound = Sound.valueOf(soundOf);
        float volume = (float) instance.getConfig().getDouble("prompt.sound.volume");
        float pitch = (float) instance.getConfig().getDouble("prompt.sound.pitch");
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
