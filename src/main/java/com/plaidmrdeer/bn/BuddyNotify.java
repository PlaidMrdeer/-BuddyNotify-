package com.plaidmrdeer.bn;

import com.plaidmrdeer.bn.commands.ReloadCommand;
import com.plaidmrdeer.bn.config.data_config.DataManager;
import com.plaidmrdeer.bn.config.language_config.LanuageManager;
import com.plaidmrdeer.bn.events.HintListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

/**
 * @author PlaidMrdeer
 */
public final class BuddyNotify extends JavaPlugin {
    /**
     * 存储@全体玩家的次数的管理类
     */
    private DataManager dataManager;

    /**
     * 操作插件语言的管理类
     */
    private LanuageManager lanuageManager;

    public DataManager getDataManager() {
        return dataManager;
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(
                new HintListener(this), this
        );
    }

    private void registerCommands() {
        getCommand("bnreload").setExecutor(new ReloadCommand(this));
    }

    /**
     * 设置传进来的字符串的格式（颜色，占位符等）
     */
    public String setStyle(String message, String... player) {
        return setColor(setPlaceholder(message, player));
    }

    private String setColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String setPlaceholder(String message, String... player) {
        if (player.length == 0) {
            return message;
        }
        message = message.replace("%player%", player[0]);

        if (player.length == 1) {
            return message;
        }
        message = message.replace("%target_player%", player[1]);

        return message;
    }

    public Logger getPluginLogger() {
        return getLogger();
    }

    /**
     * 代替Bukkit原来的sendMessage方法，让文本输出更简单
     */
    public void sendMessage(String path, CommandSender sender) {
        String message = setStyle(lanuageManager.language.getString(path));

        sender.sendMessage(message);
    }

    public void reloadPlugin() {
        reloadConfig();
        lanuageManager.loadLang();
    }

    private void logo() {
        String logo1 = "&b  ____            _     _       _   _       _   _  __       ";
        String logo2 = "&b | __ ) _   _  __| | __| |_   _| \\ | | ___ | |_(_)/ _|_   _ ";
        String logo3 = "&b |  _ \\| | | |/ _` |/ _` | | | |  \\| |/ _ \\| __| | |_| | | |";
        String logo4 = "&b | |_) | |_| | (_| | (_| | |_| | |\\  | (_) | |_| |  _| |_| |";
        String logo5 = "&b |____/ \\__,_|\\__,_|\\__,_|\\__, |_| \\_|\\___/ \\__|_|_|  \\__, |";
        String logo6 = "&b                          |___/                       |___/ ";
        CommandSender sender = Bukkit.getConsoleSender();

        sender.sendMessage(setColor(logo1));
        sender.sendMessage(setColor(logo2));
        sender.sendMessage(setColor(logo3));
        sender.sendMessage(setColor(logo4));
        sender.sendMessage(setColor(logo5));
        sender.sendMessage(setColor(logo6));
    }

    @Override
    public void onLoad() {
        dataManager = new DataManager(this);
        lanuageManager = new LanuageManager(this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (getConfig().getBoolean("logo")) {
            logo();
        }

        lanuageManager.loadLang();

        dataManager.loadData();

        registerEvents();

        registerCommands();

        dataManager.refreshNumber();

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
