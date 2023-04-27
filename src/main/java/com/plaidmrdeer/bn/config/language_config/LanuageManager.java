package com.plaidmrdeer.bn.config.language_config;

import com.plaidmrdeer.bn.BuddyNotify;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author PlaidMrdeer
 */
public class LanuageManager {
    private final BuddyNotify instance;

    public FileConfiguration language;
    public LanuageManager(BuddyNotify instance) {
        this.instance = instance;
    }

    /**
     * 生成默认语言文件并加载语言文件
     */
    public void loadLang() {
        File langDir = new File(instance.getDataFolder(), "language");
        if (!langDir.exists()) {
            langDir.mkdir();
        }
        List<String> languageList = Arrays.asList(
                "english.yml", "chinese.yml", "german.yml"
        );

        for (String language : languageList) {
            File file = new File(langDir, language);
            if (!file.exists()) {
                instance.saveResource("language" + File.separator + language, false);
            }
        }

        File file = new File(langDir, instance.getConfig().getString("language") + ".yml");
        if (!file.exists()) {
            instance.getPluginLogger().warning("Unknown language file! The plugin will load the default language \"English\"");
            file = new File(langDir, "english.yml");
            language = YamlConfiguration.loadConfiguration(file);
            return;
        }

        language = YamlConfiguration.loadConfiguration(file);
    }
}
