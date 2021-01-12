package io.github.l1ttle_org.bansystem.commands;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CommandUnban implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            final BanList bans = Bukkit.getBanList(BanList.Type.NAME);
            final String playerName;
            final String senderName;
            boolean isSilent;
            if (!(sender instanceof ConsoleCommandSender)) {
                senderName = sender.getName();
            } else {
                senderName = "Console";
            }
            if (!args[0].equalsIgnoreCase("-s")) {
                playerName = args[0];
                try {
                    isSilent = args[1].equalsIgnoreCase("-s");
                } catch (ArrayIndexOutOfBoundsException e) {
                    isSilent = false;
                }
            } else {
                isSilent = true;
                playerName = args[1];
            }
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
