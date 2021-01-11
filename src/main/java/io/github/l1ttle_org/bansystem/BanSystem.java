package io.github.l1ttle_org.bansystem;

import io.github.l1ttle_org.bansystem.commands.CommandBan;
import io.github.l1ttle_org.bansystem.commands.CommandUnban;
import io.papermc.lib.PaperLib;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BanSystem extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (PaperLib.isSpigot()) {
            PaperLib.suggestPaper(this);
        }
        getCommand("ban").setExecutor(new CommandBan());
        getCommand("unban").setExecutor(new CommandUnban());
        getServer().getPluginManager().registerEvents(new BanSystemListener(), this);
    }

    @Override
    public void onDisable() {
    }
}
