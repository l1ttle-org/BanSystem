package io.github.l1ttle_org.bansystem.commands;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

public class CommandBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            final BanList bans = Bukkit.getBanList(BanList.Type.NAME);
            final String senderName;
            final String reason;
            if (!(sender instanceof ConsoleCommandSender)) {
                senderName = sender.getName();
            } else {
                senderName = "Console";
            }
            final Player player;
            final String playerName;
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
