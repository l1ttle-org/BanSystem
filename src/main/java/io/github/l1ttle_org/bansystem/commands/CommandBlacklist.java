package io.github.l1ttle_org.bansystem.commands;

import java.util.logging.Level;
import io.github.l1ttle_org.bansystem.BanSystem;
import java.util.Date;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandBlacklist implements CommandExecutor {
    private final BanSystem banSystem;

    public CommandBlacklist(BanSystem banSystem) {
        this.banSystem = banSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            final BanList bans = Bukkit.getBanList(BanList.Type.NAME);
            final BanList bansIP = Bukkit.getBanList(BanList.Type.IP);
            final FileConfiguration dataConfig = banSystem.getDataConfig();
            final FileConfiguration config = banSystem.getConfig();
            final Player senderPlayer;
            final String senderName;
            final String reason;
            final Player player;
            final String playerName;
            final String playerUUID;
            final String playerIP;
            final Date date = null; /* TODO: Add durations */
            final boolean isSilent;
            final int blacklistID = dataConfig.getInt("lastBlacklistID") + 1;
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
                banSystem.getLogger().log(Level.INFO, "Blacklisting UUID " + playerUUID);
                playerIP = player.getAddress().getHostString();
                banSystem.getLogger().log(Level.INFO, "Blacklisting IP " + playerIP);				
                dataConfig.set(playerIP + ".blacklists.blacklisted", true);
                dataConfig.set(playerIP + ".blacklists.blacklistedReason", reason);
                if (sender instanceof Player) {
                    senderPlayer = (Player) sender;
                    dataConfig.set(playerIP + ".blacklists.blacklistedBy", senderPlayer.getUniqueId().toString());
                    senderName = sender.getName();
                } else {
                    dataConfig.set(playerUUID + ".blacklists.blacklistedBy", "Console");
                    senderName = "Console";
                }
                dataConfig.set(playerIP + ".blacklists.blacklistedOn", System.currentTimeMillis());
                dataConfig.set(playerIP + ".blacklists.blacklistedFor", date); /* TODO: Add durations */
                dataConfig.set(playerIP + ".blacklists.blacklistedSilently", isSilent);
                dataConfig.set(playerIP + ".blacklists.blacklistID", blacklistID);
                dataConfig.set(playerUUID + ".blacklists.IP", playerIP);
                dataConfig.set("lastBlacklistID", blacklistID);
                banSystem.saveDataConfig(dataConfig);
                bans.addBan(playerName, reason, date, senderName);
                bansIP.addBan(playerIP, reason, date, senderName);
                player.kickPlayer(ChatColor.RED + "You are permanently" + ChatColor.DARK_RED + " blacklisted " + ChatColor.RED + "from this server!\n\n" + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + reason + ChatColor.GRAY + "\nFind out more: " + ChatColor.AQUA + ChatColor.UNDERLINE + config.getString("websiteBlacklisted") + ChatColor.GRAY + "\n\nBlacklist ID:" + ChatColor.WHITE + " GG-" + blacklistID + ChatColor.GRAY + "\nSharing your Blacklist ID may affect the processing of your appeal!");
            } else {
                sender.sendMessage(ChatColor.RED + "No player matching " + ChatColor.YELLOW + playerName + ChatColor.RED + " is connected to this server.");
                return true;
            }
            if (!isSilent) {
                Bukkit.broadcastMessage(ChatColor.RED + senderName + ChatColor.GREEN + " has permanently blacklisted " + ChatColor.RED + playerName);
            } else {
                Bukkit.broadcast(ChatColor.GRAY + "[Silent] " + ChatColor.RED + senderName + ChatColor.GREEN + " has permanently blacklisted " + ChatColor.RED + playerName + ChatColor.GREEN + " for " + ChatColor.GRAY + reason, "blacklistsystem.notify");
            }
            return true;
        }
        return false;
    }
}
