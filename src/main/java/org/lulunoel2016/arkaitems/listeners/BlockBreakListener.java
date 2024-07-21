package org.lulunoel2016.arkaitems.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand(); // Utilisation de getItemInHand pour les versions 1.8 et antérieures

        // Vérifier si l'item est nul ou est de l'air
        if (item == null || item.getType() == Material.AIR || item.getAmount() == 0) {
            return;
        }
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasLore()) {
                List<String> lore = meta.getLore();
                for (int i = 0; i < lore.size(); i++) {
                    if (lore.get(i).contains("Blocs minés: ")) {
                        // Extract the current number of mined blocks
                        String loreLine = lore.get(i);

                        // Pattern to match the number in the lore line
                        Pattern pattern = Pattern.compile("Blocs minés: (\\d+)");
                        Matcher matcher = pattern.matcher(ChatColor.stripColor(loreLine));

                        int blocksMined = 0;
                        if (matcher.find()) {
                            blocksMined = Integer.parseInt(matcher.group(1));
                        }

                        // Increment the count
                        blocksMined++;

                        // Update the lore with color code
                        String newLoreLine = ChatColor.translateAlternateColorCodes('&', "&7Blocs minés: " + blocksMined);
                        lore.set(i, newLoreLine);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                    }
                    if (lore.get(i).contains("Bûche minés: ")) {
                        if (event.getBlock().getType() != Material.OAK_LOG &&
                                event.getBlock().getType() != Material.SPRUCE_LOG &&
                                event.getBlock().getType() != Material.BIRCH_LOG &&
                                event.getBlock().getType() != Material.JUNGLE_LOG &&
                                event.getBlock().getType() != Material.ACACIA_LOG &&
                                event.getBlock().getType() != Material.DARK_OAK_LOG) {
                            // Retourne si le bloc miné n'est pas une bûche
                            return;
                        }
                        // Extract the current number of mined blocks
                        String loreLine = lore.get(i);

                        // Pattern to match the number in the lore line
                        Pattern pattern = Pattern.compile("Bûche minés: (\\d+)");
                        Matcher matcher = pattern.matcher(ChatColor.stripColor(loreLine));

                        int blocksMined = 0;
                        if (matcher.find()) {
                            blocksMined = Integer.parseInt(matcher.group(1));
                        }

                        // Increment the count
                        blocksMined++;

                        // Update the lore with color code
                        String newLoreLine = ChatColor.translateAlternateColorCodes('&', "&7Bûche minés: " + blocksMined);
                        lore.set(i, newLoreLine);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                    }
                }
            } else {
            }
        } else {
        }
    }
}
