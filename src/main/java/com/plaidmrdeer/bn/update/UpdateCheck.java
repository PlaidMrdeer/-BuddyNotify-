package com.plaidmrdeer.bn.update;

import com.plaidmrdeer.bn.BuddyNotify;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author PlaidMrdeer
 */
public class UpdateCheck {
    private final BuddyNotify instance;

    private String ver;

    public UpdateCheck(BuddyNotify instance) {
        this.instance = instance;
    }

    public void updateCheck() {
        if (!isUpDate()) {
            return;
        }

        instance.addPlaceholder("%version%", instance.getDescription().getVersion());
        instance.sendMessage("current_version", Bukkit.getConsoleSender());
        instance.sendMessage("inspect_new_version", Bukkit.getConsoleSender());

        new BukkitRunnable() {
            @Override
            public void run() {
                versionCheck();
            }
        }.runTaskTimerAsynchronously(instance, 0L, 144000L);
    }

    private void versionCheck() {
        boolean isNewVersion = isLatestVersion();

        if (isNewVersion) {
            instance.sendMessage("no_new_version", Bukkit.getConsoleSender());
            return;
        }

        instance.addPlaceholder("%new_version%", ver);
        instance.sendMessage("detected_new_version", Bukkit.getConsoleSender());
        instance.sendMessage("new_version_website", Bukkit.getConsoleSender());
    }

    private String getLatestVersion() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URL("https://api.spigotmc.org/legacy/update.php?resource=109511")
                        .openStream(), StandardCharsets.UTF_8))) {
            ver = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ver;
    }

    private boolean isLatestVersion() {
        String latest = getLatestVersion();
        String current = instance.getDescription().getVersion();
        return latest.equalsIgnoreCase(current);
    }

    private boolean isUpDate() {
        return instance.getConfig().getBoolean("update");
    }
}
