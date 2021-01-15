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
import java.util.logging.Level;

public class CommandUnban implements CommandExecutor {
    private final BanSystem banSystem;

    public CommandUnban(BanSystem banSystem) {
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
            final String playerName;
            final String playerUUID;
            final boolean isSilent;
            if (!args[0].equalsIgnoreCase("-s")) {
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
                playerName = args[1];
                try {
                    reason = args[2].replace("_", " ");
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }
            playerUUID = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString(); // There's no other easy way to get UUID of an OfflinePlayer
            dataConfig.set(playerUUID + ".bans.banned", false);
            dataConfig.set(playerUUID + ".bans.unBannedReason", reason);
            if (sender instanceof Player) {
                senderPlayer = (Player) sender;
                dataConfig.set(playerUUID + ".bans.unBannedBy", senderPlayer.getUniqueId().toString());
                senderName = sender.getName();
            } else {
                dataConfig.set(playerUUID + ".bans.unBannedBy", "Console");
                senderName = "Console";
            }
            dataConfig.set(playerUUID + ".bans.unBannedOn", System.currentTimeMillis());
            dataConfig.set(playerUUID + ".bans.unBannedSilently", isSilent);
            banSystem.saveDataConfig();
            bans.pardon(playerName);
            if (!isSilent) {
                Bukkit.broadcastMessage(ChatColor.RED + senderName + ChatColor.GREEN + " has unbanned " + ChatColor.RED + playerName);
            } else {
                Bukkit.broadcast(ChatColor.GRAY + "[Silent] " + ChatColor.RED + senderName + ChatColor.GREEN + " has unbanned " + ChatColor.RED + playerName, "bansystem.notify");
            }
            return true;
        }
        return false;
    }
}
