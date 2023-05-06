package com.plaidmrdeer.bn;

import com.plaidmrdeer.bn.bstats.Metrics;
import com.plaidmrdeer.bn.commands.ReloadCommand;
import com.plaidmrdeer.bn.config.data_config.DataManager;
import com.plaidmrdeer.bn.config.language_config.LanuageManager;
import com.plaidmrdeer.bn.events.HintListener;
import com.plaidmrdeer.bn.update.UpdateCheck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
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

    /**
     * 更新检测
     */
    private UpdateCheck updateCheck;

    /**
     * 占位符存储
     */
    private final Map<String, String> placeholder = new HashMap<>();

    public void addPlaceholder(String place, String var) {
        placeholder.put(place, var);
    }

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
    public String setStyle(String message) {
        return setColor(setPlaceholder(message));
    }

    private String setColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String setPlaceholder(String message) {
        for (Map.Entry<String, String> place : placeholder.entrySet()) {
            message = message.replace(place.getKey(), place.getValue());
        }

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
        updateCheck = new UpdateCheck(this);
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

        int pluginId = 18391;
        Metrics metrics = new Metrics(this, pluginId);

        updateCheck.updateCheck();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
