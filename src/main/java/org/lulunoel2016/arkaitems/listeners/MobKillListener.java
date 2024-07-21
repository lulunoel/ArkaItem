package org.lulunoel2016.arkaitems.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobKillListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Vérifier si le tueur est un joueur
        if (event.getEntity().getKiller() == null || !(event.getEntity().getKiller() instanceof Player)) {
            return;
        }

        Player player = event.getEntity().getKiller();
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
                    if (lore.get(i).contains("Mobs tués: ")) {
                        // Extract the current number of mobs killed
                        String loreLine = lore.get(i);

                        // Pattern to match the number in the lore line
                        Pattern pattern = Pattern.compile("Mobs tués: (\\d+)");
                        Matcher matcher = pattern.matcher(ChatColor.stripColor(loreLine));

                        int mobsKilled = 0;
                        if (matcher.find()) {
                            mobsKilled = Integer.parseInt(matcher.group(1));
                        }

                        // Increment the count
                        mobsKilled++;

                        // Update the lore with color code
                        String newLoreLine = ChatColor.translateAlternateColorCodes('&', "&7Mobs tués: " + mobsKilled);
                        lore.set(i, newLoreLine);
                        meta.setLore(lore);
                        item.setItemMeta(meta);

                        break;
                    }
                }
            } else {
            }
        } else {
        }
    }
}
