package io.github.l1ttle_org.bansystem.commands;

import io.github.l1ttle_org.bansystem.BanSystem;
import java.util.logging.Level;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandUnblacklist implements CommandExecutor {
    private final BanSystem banSystem;

    public CommandUnblacklist(BanSystem banSystem) {
        this.banSystem = banSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            final BanList bans = Bukkit.getBanList(BanList.Type.NAME);
            final BanList bansIP = Bukkit.getBanList(BanList.Type.IP);
            final FileConfiguration dataConfig = banSystem.getDataConfig();
            final Player senderPlayer;
            final String senderName;
            final String reason;
            final String playerName;
            final String playerUUID;
            final String playerIP;
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
            playerIP = dataConfig.getString(playerUUID + ".blacklists.IP");
            try {
                bans.pardon(playerName);
                bansIP.pardon(playerIP);
                banSystem.getLogger().log(Level.INFO, "Attempting to unblacklist IP " + playerIP);
            } catch (NullPointerException | IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "No player matching " + ChatColor.YELLOW + playerName + ChatColor.RED + " was banned or blacklisted from this server");
                return true;
            }
            dataConfig.set(playerIP + ".blacklists.blacklisted", false);
            dataConfig.set(playerIP + ".blacklists.unBlacklistedReason", reason);
            if (sender instanceof Player) {
                senderPlayer = (Player) sender;
                dataConfig.set(playerUUID + ".blacklists.unBlacklistedBy", senderPlayer.getUniqueId().toString());
                senderName = sender.getName();
            } else {
                dataConfig.set(playerUUID + ".blacklists.unBlacklistedBy", "Console");
                senderName = "Console";
            }
            dataConfig.set(playerIP + ".blacklists.unBlacklistedOn", System.currentTimeMillis());
            dataConfig.set(playerIP + ".blacklists.UnBlacklistedSilently", isSilent);
            banSystem.saveDataConfig(dataConfig);
            if (!isSilent) {
                Bukkit.broadcastMessage(ChatColor.RED + senderName + ChatColor.GREEN + " has unblacklisted " + ChatColor.RED + playerName);
            } else {
                Bukkit.broadcast(ChatColor.GRAY + "[Silent] " + ChatColor.RED + senderName + ChatColor.GREEN + " has unblacklisted " + ChatColor.RED + playerName, "bansystem.notify");
            }
            return true;
        }
        return false;
    }
}
