package org.lulunoel2016.arkaitems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.lulunoel2016.arkaitems.ArmorSetChecker;

public class ArmorSetListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            ArmorSetChecker.applyOrRemoveEffects((Player) event.getWhoClicked());
        }
    }
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        ArmorSetChecker.applyOrRemoveEffects(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ArmorSetChecker.applyOrRemoveEffects(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Optionally remove effects on logout if desired
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        ArmorSetChecker.applyOrRemoveEffects(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        ArmorSetChecker.applyOrRemoveEffects(event.getPlayer());
    }

}
