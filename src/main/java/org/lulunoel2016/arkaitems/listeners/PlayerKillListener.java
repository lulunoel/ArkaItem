package org.lulunoel2016.arkaitems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerKillListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deceased = event.getEntity();
        Player killer = deceased.getKiller();

        if (killer != null) {
            ItemStack weapon = killer.getItemInHand(); // Use getItemInHand() for 1.8
            NBTItem nbtItem = new NBTItem(weapon);

            if (nbtItem.hasKey("drophead")) {
                if (nbtItem.getBoolean("drophead")) {
                    String command = "give " + killer.getName() + " skull 1 3 {SkullOwner:\"" + deceased.getName() + "\"}";
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    killer.sendMessage("Vous avez récupéré la tête de " + deceased.getName() + "!");
                    deceased.sendMessage(killer.getName() + " vient de récupéré t'as tête!");
                } else {
                }
            } else {
            }
        } else {
            if (deceased != null) {
            }
        }

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
                    if (lore.get(i).contains("Joueurs tués: ")) {
                        // Extract the current number of players killed
                        String loreLine = lore.get(i);

                        // Pattern to match the number in the lore line
                        Pattern pattern = Pattern.compile("Joueurs tués: (\\d+)");
                        Matcher matcher = pattern.matcher(ChatColor.stripColor(loreLine));

                        int playersKilled = 0;
                        if (matcher.find()) {
                            playersKilled = Integer.parseInt(matcher.group(1));
                        }

                        // Increment the count
                        playersKilled++;

                        // Update the lore with color code
                        String newLoreLine = ChatColor.translateAlternateColorCodes('&', "&7Joueurs tués: " + playersKilled);
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
