package io.github.l1ttle_org.bansystem.commands;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CommandUnban implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            final BanList bans = Bukkit.getBanList(BanList.Type.NAME);
            final String playerName = args[0];
            final String senderName;
            if (!(sender instanceof ConsoleCommandSender)) {
                senderName = sender.getName();
            } else {
                senderName = "Console";
            }
            bans.pardon(playerName);
            Bukkit.broadcast("Ban entry removed. Unbanned player: " + playerName + ". Staff member: " + senderName, "bansystem.notify");
            return true;
        }
        return false;
    }
}
