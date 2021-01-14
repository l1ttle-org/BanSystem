package io.github.l1ttle_org.bansystem;

import io.github.l1ttle_org.bansystem.commands.CommandBan;
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

public final class BanSystem extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (PaperLib.isSpigot()) {
            PaperLib.suggestPaper(this);
        }
        getCommand("ban").setExecutor(new CommandBan(this));
        getCommand("unban").setExecutor(new CommandUnban(this));
        getCommand("kick").setExecutor(new CommandKick());
        getCommand("mute").setExecutor(new CommandMute(this));
        getCommand("unmute").setExecutor(new CommandUnmute(this));
        getServer().getPluginManager().registerEvents(new BanSystemListener(this), this);
    }

    @Override
    public void onDisable() {
    }

    public FileConfiguration getDataConfig() {
        return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));
    }
}
