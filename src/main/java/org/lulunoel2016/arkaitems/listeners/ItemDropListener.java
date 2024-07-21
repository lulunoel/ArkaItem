package org.lulunoel2016.arkaitems.listeners;

import de.tr7zw.nbtapi.NBTItem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDropListener implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasKey("dropable")) {
            boolean dropable = nbtItem.getBoolean("dropable");
            if (!dropable) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("Vous ne pouvez pas drop cet item.");
            }
        }
    }
}
