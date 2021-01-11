package io.github.l1ttle_org.bansystem;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Objects;

public class BanSystemListener implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (player.isBanned()) {
            final String playerName = event.getPlayer().getName();
            final String reason = Objects.requireNonNull(Bukkit.getBanList(BanList.Type.NAME).getBanEntry(playerName)).getReason();
            event.setKickMessage(ChatColor.RED + "You are permanently banned from this server!\n\n" + "Reason: " + ChatColor.WHITE + reason);
        }
    }
}
