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
        final Player player = event.getPlayer();
        if (player.isBanned()) {
            final String playerName = event.getPlayer().getName();
            final String reason = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(playerName).getReason();
            event.setKickMessage(ChatColor.RED + "You are permanently banned from this server!\n\n" + "Reason: " + ChatColor.WHITE + reason);
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
