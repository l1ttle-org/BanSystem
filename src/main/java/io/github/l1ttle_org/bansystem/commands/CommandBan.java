package io.github.l1ttle_org.bansystem.commands;

import io.github.l1ttle_org.bansystem.BanSystem;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

public class CommandBan implements CommandExecutor {
    private final BanSystem banSystem;

    public CommandBan(BanSystem banSystem) {
        this.banSystem = banSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            final BanList bans = Bukkit.getBanList(BanList.Type.NAME);
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
            } else {
                playerUUID = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString(); // There's no other easy way to get UUID of an OfflinePlayer
            }
            dataConfig.set(playerUUID + ".bans.banned", true);
            dataConfig.set(playerUUID + ".bans.bannedReason", reason);
            if (sender instanceof Player) {
                senderPlayer = (Player) sender;
                dataConfig.set(playerUUID + ".bans.bannedBy", senderPlayer.getUniqueId().toString());
                senderName = sender.getName();
            } else {
                dataConfig.set(playerUUID + ".bans.bannedBy", "Console");
                senderName = "Console";
            }
            dataConfig.set(playerUUID + ".bans.bannedOn", System.currentTimeMillis());
            dataConfig.set(playerUUID + ".bans.bannedFor", date); /* TODO: Add durations */
            dataConfig.set(playerUUID + ".bans.bannedSilently", isSilent);
            try {
                dataConfig.save(new File(banSystem.getDataFolder(), "data.yml"));
            } catch (IOException e) {
                banSystem.getLogger().log(Level.SEVERE, "Could not save data to data.yml!");
                e.printStackTrace();
            }
            bans.addBan(playerName, reason, date, senderName);
            if (player != null) {
                player.kickPlayer(ChatColor.RED + "You are permanently banned from this server!\n\n" + "Reason: " + ChatColor.WHITE + reason);
            }
            if (!isSilent) {
                Bukkit.broadcastMessage(ChatColor.RED + senderName + ChatColor.GREEN + " has permanently banned " + ChatColor.RED + playerName);
            } else {
                Bukkit.broadcast(ChatColor.GRAY + "[Silent] " + ChatColor.RED + senderName + ChatColor.GREEN + " has permanently banned " + ChatColor.RED + playerName + ChatColor.GREEN + " for " + ChatColor.GRAY + reason, "bansystem.notify");
            }
            return true;
        }
        return false;
    }
}
