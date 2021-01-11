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

import static org.bukkit.Bukkit.getServer;

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
            final boolean isBanSilent;
            if (!args[0].equalsIgnoreCase("-s")) {
                player = Bukkit.getPlayer(args[0]);
                playerName = args[0];
                if (!args[1].equalsIgnoreCase("-s")) {
                    isBanSilent = false;
                    reason = args[1];
                    bans.addBan(playerName, reason, date, senderName);
                    if (player != null) {
                        player.kickPlayer(ChatColor.RED + "You are permanently banned from this server!\n\n" + "Reason: " + ChatColor.WHITE + reason);
                    }
                    getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "A player has been banned from this server for breaking the rules.");
                } else {
                    isBanSilent = true;
                    try {
                        reason = args[2];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return false;
                    }
                    bans.addBan(playerName, reason, date, senderName);
                    if (player != null) {
                        player.kickPlayer(ChatColor.RED + "You are permanently banned from this server!\n\n" + "Reason: " + ChatColor.WHITE + reason);
                    }
                }
            } else {
                isBanSilent = true;
                player = Bukkit.getPlayer(args[1]);
                playerName = args[1];
                try {
                    reason = args[2];
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
                bans.addBan(playerName, reason, date, senderName);
                if (player != null) {
                    player.kickPlayer(ChatColor.RED + "You are permanently banned from this server!\n\n" + "Reason: " + ChatColor.WHITE + reason);
                }
            }
            Bukkit.broadcast("Ban entry added. Banned player: " + playerName + ". Reason: " + reason + ". Duration: " + date + ". Staff: " + senderName + ". Silent: " + isBanSilent, "bansystem.notify");
            return true;
        }
        return false;
    }
}
