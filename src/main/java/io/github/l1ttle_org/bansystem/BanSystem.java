package io.github.l1ttle_org.bansystem;

import io.github.l1ttle_org.bansystem.commands.CommandBan;
import io.github.l1ttle_org.bansystem.commands.CommandBlacklist;
import io.github.l1ttle_org.bansystem.commands.CommandKick;
import io.github.l1ttle_org.bansystem.commands.CommandMute;
import io.github.l1ttle_org.bansystem.commands.CommandUnban;
import io.github.l1ttle_org.bansystem.commands.CommandUnblacklist;
import io.github.l1ttle_org.bansystem.commands.CommandUnmute;
import io.papermc.lib.PaperLib;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class BanSystem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (PaperLib.isSpigot()) {
            PaperLib.suggestPaper(this);
        }
        saveDefaultConfig();
        FileConfiguration dataConfig = getDataConfig();
        if (dataConfig.get("lastBanID") == null) {
            dataConfig.set("lastBanID", 1);
            saveDataConfig(dataConfig);
        }
        if (dataConfig.get("lastBlacklistID") == null) {
            dataConfig = getDataConfig();
            dataConfig.set("lastBlacklistID", 1);
            saveDataConfig(dataConfig);
        }
        getCommand("ban").setExecutor(new CommandBan(this));
        getCommand("blacklist").setExecutor(new CommandBlacklist(this));
        getCommand("kick").setExecutor(new CommandKick());
        getCommand("mute").setExecutor(new CommandMute(this));
        getCommand("unban").setExecutor(new CommandUnban(this));
        getCommand("unblacklist").setExecutor(new CommandUnblacklist(this));
        getCommand("unmute").setExecutor(new CommandUnmute(this));
        getServer().getPluginManager().registerEvents(new BanSystemListener(this), this);
    }

    @Override
    public void onDisable() {
    }

    public FileConfiguration getDataConfig() {
        return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));
    }

    public void saveDataConfig(FileConfiguration dataConfig) {  
        try {
            dataConfig.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save data to data.yml!");
            e.printStackTrace();
        }
    }
}
