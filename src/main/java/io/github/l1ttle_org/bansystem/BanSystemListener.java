package io.github.l1ttle_org.bansystem;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class BanSystemListener implements Listener {
    private final BanSystem banSystem;

    public BanSystemListener(BanSystem banSystem) {
        this.banSystem = banSystem;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final BanList bansIP = Bukkit.getBanList(BanList.Type.IP);
        final Player player = event.getPlayer();
        final FileConfiguration config = banSystem.getConfig();
        final FileConfiguration dataConfig = banSystem.getDataConfig();
        final String playerUUID = player.getUniqueId().toString();
        if (player.isBanned()) {
            final String reason = dataConfig.getString(playerUUID + ".bans.bannedReason");
            final int banID = dataConfig.getInt("lastBanID") + 1;
            event.setKickMessage(ChatColor.RED + "You are permanently" + ChatColor.DARK_RED + " banned " + ChatColor.RED + "from this server!\n\n" + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + reason + ChatColor.GRAY + "\nFind out more: " + ChatColor.AQUA + ChatColor.UNDERLINE + config.getString("website") + ChatColor.GRAY + "\n\nBan ID:" + ChatColor.WHITE + " GG-" + banID + ChatColor.GRAY + "\nSharing your Ban ID may affect the processing of your appeal!");
        }
        if (dataConfig.getBoolean(playerUUID + ".blacklists.blacklisted") || bansIP.getBanEntry(event.getAddress().getHostAddress()) != null) {
            final String reason = dataConfig.getString(playerUUID + ".blacklists.blacklistedReason");
            final int blacklistID = dataConfig.getInt("lastBlacklistID") + 1;
            player.kickPlayer(ChatColor.RED + "You are permanently" + ChatColor.DARK_RED + " blacklisted " + ChatColor.RED + "from this server!\n\n" + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + reason + ChatColor.GRAY + "\nFind out more: " + ChatColor.AQUA + ChatColor.UNDERLINE + config.getString("websiteBlacklisted") + ChatColor.GRAY + "\n\nBlacklist ID:" + ChatColor.WHITE + " GG-" + blacklistID + ChatColor.GRAY + "\nSharing your Blacklist ID may affect the processing of your appeal!");
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        final FileConfiguration dataConfig = banSystem.getDataConfig();
        final Player player = event.getPlayer();
        final String playerUUID = player.getUniqueId().toString();
        if (dataConfig.getBoolean(playerUUID + ".mutes.muted")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are currently muted for " + dataConfig.getString(playerUUID + ".mutes.mutedReason"));
        }
    }
}
