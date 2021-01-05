package io.github.l1ttle_org.bansystem;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BanSystem extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (!getServer().isStopping()) {
            getLogger().log(Level.SEVERE, "Reloading is bad. If you have any problems with your server, please use /stop");
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[0]);
            final String playerFriendlyName = player.getName();
            if (player.hasPermission(args[1])) {
                sender.sendMessage(ChatColor.GREEN + "" + playerFriendlyName + " has permission " + args[1]);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "" + playerFriendlyName + " does not have permission " + args[1]);
            }
            return true;
        }
        return false;
    }
}
