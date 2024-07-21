package org.lulunoel2016.arkaitems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.lulunoel2016.arkaitems.ArkaItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemUsageListener implements Listener {

    private ArkaItems plugin;

    public ItemUsageListener(ArkaItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if (item != null && item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey("mine")) {
                int mineCount = nbtItem.getInteger("mine");
                mineCount++;
                nbtItem.setInteger("mine", mineCount);

                updateLore(item, nbtItem);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            ItemStack item = player.getInventory().getItemInHand();

            if (item != null && item.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(item);
                if (nbtItem.hasKey("hunt")) {
                    int huntCount = nbtItem.getInteger("hunt");
                    huntCount++;
                    nbtItem.setInteger("hunt", huntCount);

                    updateLore(item, nbtItem);
                }
            }
        }
    }

    private void updateLore(ItemStack item, NBTItem nbtItem) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = meta.getLore();
            List<String> newLore = new ArrayList<>();
            String owner = nbtItem.getString("owner");
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            int mineCount = nbtItem.getInteger("mine");
            int huntCount = nbtItem.getInteger("hunt");

            for (String line : lore) {
                line = line.replace("{owner}", owner)
                        .replace("{date}", date)
                        .replace("{mine}", String.valueOf(mineCount))
                        .replace("{hunt}", String.valueOf(huntCount));
                newLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }

            meta.setLore(newLore);
            item.setItemMeta(meta);
        }
    }
}
