package org.lulunoel2016.arkaitems.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Location;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockTravelListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        // Calculer la distance parcourue en blocs
        double distance = from.distance(to);

        // Vérifier si la distance est suffisante pour être comptabilisée (au moins 1 bloc)
        if (distance < 1) {
            return;
        }

        ItemStack item = player.getItemInHand(); // Utilisation de getItemInHand pour les versions 1.8 et antérieures

        // Vérifier si l'item est nul ou est de l'air
        if (item == null || item.getType() == Material.AIR || item.getAmount() == 0) {
            player.sendMessage("Item is null, air, or amount is zero.");
            return;
        }

        player.sendMessage("Item is valid: " + item.getType());

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasLore()) {
                List<String> lore = meta.getLore();
                player.sendMessage("Item has lore: " + lore.toString());
                for (int i = 0; i < lore.size(); i++) {
                    if (lore.get(i).contains("Blocs parcourus: ")) {
                        // Extract the current number of blocks traveled
                        String loreLine = lore.get(i);
                        player.sendMessage("Lore line before update: " + loreLine);

                        // Pattern to match the number in the lore line
                        Pattern pattern = Pattern.compile("Blocs parcourus: (\\d+)");
                        Matcher matcher = pattern.matcher(ChatColor.stripColor(loreLine));

                        int blocksTraveled = 0;
                        if (matcher.find()) {
                            blocksTraveled = Integer.parseInt(matcher.group(1));
                        }

                        // Increment the count by the distance traveled
                        blocksTraveled += distance;

                        // Update the lore with color code
                        String newLoreLine = ChatColor.translateAlternateColorCodes('&', "&7Blocs parcourus: " + blocksTraveled);
                        lore.set(i, newLoreLine);
                        meta.setLore(lore);
                        item.setItemMeta(meta);

                        player.sendMessage("Lore line after update: " + newLoreLine);
                        break;
                    }
                }
            } else {
                player.sendMessage("Item does not have lore.");
            }
        } else {
            player.sendMessage("Item does not have meta.");
        }
    }
}
