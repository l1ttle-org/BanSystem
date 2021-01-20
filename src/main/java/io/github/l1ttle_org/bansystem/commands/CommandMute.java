package io.github.l1ttle_org.bansystem.commands;

import io.github.l1ttle_org.bansystem.BanSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Date;

public class CommandMute implements CommandExecutor {
    private final BanSystem banSystem;

    public CommandMute(BanSystem banSystem) {
        this.banSystem = banSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            final FileConfiguration dataConfig = banSystem.getDataConfig();
            final Player senderPlayer;
            final String senderName;
            final String reason;
            final Player player;
            final String playerName;
            final String playerUUID;
            final Date date = null; /* TODO: Add durations */
            final boolean isSilent;
            if (!args[0].equalsIgnoreCase("-s")) {
                player = Bukkit.getPlayer(args[0]);
                playerName = args[0];
                if (!args[1].equalsIgnoreCase("-s")) {
                    isSilent = false;
                    reason = args[1].replace("_", " ");
                } else {
                    isSilent = true;
                    try {
                        reason = args[2].replace("_", " ");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return false;
                    }
                }
            } else {
                isSilent = true;
                player = Bukkit.getPlayer(args[1]);
                playerName = args[1];
                try {
                    reason = args[2].replace("_", " ");
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }
            if (player != null) {
                playerUUID = player.getUniqueId().toString();
                player.sendMessage(ChatColor.RED + "You are now permanently muted for " + reason);
            } else {
                playerUUID = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString(); // There's no other easy way to get UUID of an OfflinePlayer
            }
            dataConfig.set(playerUUID + ".mutes.muted", true);
            dataConfig.set(playerUUID + ".mutes.mutedReason", reason);
            if (sender instanceof Player) {
                senderPlayer = (Player) sender;
                dataConfig.set(playerUUID + ".mutes.mutedBy", senderPlayer.getUniqueId().toString());
                senderName = sender.getName();
            } else {
                dataConfig.set(playerUUID + ".mutes.mutedBy", "Console");
                senderName = "Console";
            }
            dataConfig.set(playerUUID + ".mutes.mutedOn", System.currentTimeMillis());
            dataConfig.set(playerUUID + ".mutes.mutedFor", date); /* TODO: Add durations */
            dataConfig.set(playerUUID + ".mutes.mutedSilently", isSilent);
            banSystem.saveDataConfig(dataConfig);
            if (!isSilent) {
                Bukkit.broadcastMessage(ChatColor.RED + senderName + ChatColor.GREEN + " has permanently muted " + ChatColor.RED + playerName);
            } else {
                Bukkit.broadcast(ChatColor.GRAY + "[Silent] " + ChatColor.RED + senderName + ChatColor.GREEN + " has permanently muted " + ChatColor.RED + playerName + ChatColor.GREEN + " for " + ChatColor.GRAY + reason, "bansystem.notify");
            }
            return true;
        }
        return false;
    }
}
