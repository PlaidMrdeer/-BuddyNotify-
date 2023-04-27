package com.plaidmrdeer.bn.config.data_config;

import com.plaidmrdeer.bn.BuddyNotify;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * @author PlaidMrdeer
 */
public class DataManager {
    private final BuddyNotify instance;
    private FileConfiguration data;
    private File dataFile;
    private LocalDate today;

    public DataManager(BuddyNotify instance) {
        this.instance = instance;
    }

    /**
     * 生成存储@全体玩家剩余次数的文件
     */
    public void loadData() {
        dataFile = new File(instance.getDataFolder(), "player_data.yml");

        if (!dataFile.exists()) {
            instance.saveResource("player_data.yml", false);
        }

        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    /**
     * 将配置文件内存数据存储到硬盘
     */
    private void save() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 每@全体玩家一次减一次次数
     */
    public void removeNumber(Player player, int number) {
        if (!data.contains("number." + player.getName())) {
            int maxNumber = player.isOp() ? 10 : 3;
            data.set("number." + player.getName(), maxNumber);
        }

        data.set("number." + player.getName(), getNumber(player) - number);
        save();
    }

    public int getNumber(Player player) {
        return data.getInt("number." + player.getName());
    }

    /**
     * 刷新@全体玩家的每日次数
     */
    public void refreshNumber() {
        today = LocalDate.now();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (today.isEqual(LocalDate.now())) {
                    return;
                }

                for (String playerName : data.getConfigurationSection("number").getKeys(false)) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
                    int maxNumber = player.isOp() ? 10 : 3;
                    data.set("number." + playerName, maxNumber);
                }
                save();

                today = LocalDate.now();
            }
        }.runTaskTimerAsynchronously(instance, 144000L, 144000L);
    }
}
