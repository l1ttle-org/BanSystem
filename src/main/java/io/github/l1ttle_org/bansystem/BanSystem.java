package io.github.l1ttle_org.bansystem;

import io.github.l1ttle_org.bansystem.commands.CommandBan;
import io.github.l1ttle_org.bansystem.commands.CommandBlacklist;
import io.github.l1ttle_org.bansystem.commands.CommandKick;
import io.github.l1ttle_org.bansystem.commands.CommandMute;
import io.github.l1ttle_org.bansystem.commands.CommandUnban;
import io.github.l1ttle_org.bansystem.commands.CommandUnmute;
import io.papermc.lib.PaperLib;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class BanSystem extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (PaperLib.isSpigot()) {
            PaperLib.suggestPaper(this);
        }
        FileConfiguration dataConfig = getDataConfig();
        if (dataConfig.get("lastBanID") == null) {
            dataConfig.set("lastBanID", 1);
            saveDataConfig();
        }
        if (dataConfig.get("lastBlacklistID") == null) {
            dataConfig = getDataConfig();
            dataConfig.set("lastBlacklistID", 1);
        }
        getCommand("ban").setExecutor(new CommandBan(this));
        getCommand("unban").setExecutor(new CommandUnban(this));
        getCommand("kick").setExecutor(new CommandKick());
        getCommand("mute").setExecutor(new CommandMute(this));
        getCommand("unmute").setExecutor(new CommandUnmute(this));
        getCommand("blacklist").setExecutor(new CommandBlacklist(this));
        getServer().getPluginManager().registerEvents(new BanSystemListener(this), this);
    }

    @Override
    public void onDisable() {
    }

    public FileConfiguration getDataConfig() {
        return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));
    }
    public void saveDataConfig() {
        final FileConfiguration dataConfig = getDataConfig();
        try {
            dataConfig.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save data to data.yml!");
            e.printStackTrace();
        }
    }
}
