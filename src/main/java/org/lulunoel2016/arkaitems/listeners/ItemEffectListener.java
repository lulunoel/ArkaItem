package org.lulunoel2016.arkaitems.listeners;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.lulunoel2016.arkaitems.ArkaItems;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemEffectListener implements Listener {

    private ArkaItems plugin;
    private Map<Player, ItemStack> previousMainHandItems = new HashMap<>();

    public ItemEffectListener(ArkaItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> applyEffects((Player) event.getWhoClicked()), 1L); // Delay to ensure item switch
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        previousMainHandItems.put(player, player.getInventory().getItemInHand());
        Bukkit.getScheduler().runTaskLater(plugin, () -> applyEffects(player), 1L); // Delay to ensure item switch
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand(); // Utilisation de getItemInHand pour les versions 1.8 et antérieures
        // Vérifier si l'item est nul, est de l'air ou a une quantité de zéro
        if (item == null || item.getType() == Material.AIR || item.getAmount() == 0) {
            return;
        }
        applyEffects(player);
    }

    public void applyEffects(Player player) {
        boolean effectApplied = false;

        // Check main hand
        ItemStack mainHandItem = player.getInventory().getItemInHand();
        effectApplied = applyEffectFromItem(player, mainHandItem) || effectApplied;

        // Check armor slots
        for (ItemStack armorItem : player.getInventory().getArmorContents()) {
            effectApplied = applyEffectFromItem(player, armorItem) || effectApplied;
        }

        // Remove effects if no item with effect is held/worn and if previous or current item has NBT effects
        ItemStack previousMainHandItem = previousMainHandItems.get(player);
        if (!effectApplied && (hasNBTEffects(mainHandItem) || hasNBTEffects(previousMainHandItem))) {
            removeEffects(player);
        }

        // Update the previous main hand item
        previousMainHandItems.put(player, mainHandItem);
    }

    private boolean applyEffectFromItem(Player player, ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey("effects")) {
                String json = nbtItem.getString("effects");
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
                List<Map<String, Object>> effects = gson.fromJson(json, type);

                for (Map<String, Object> effectData : effects) {
                    String effectType = (String) effectData.get("effect");
                    int amplifier = ((Double) effectData.get("level")).intValue();
                    int duration = 999999; // 10 minutes to ensure continuous application
                    PotionEffectType potionEffectType = PotionEffectType.getByName(effectType);

                    if (potionEffectType != null) {
                        boolean shouldAddEffect = true;

                        for (PotionEffect currentEffect : player.getActivePotionEffects()) {
                            if (currentEffect.getType().equals(potionEffectType)) {
                                if (currentEffect.getAmplifier() >= amplifier) {
                                    shouldAddEffect = false;
                                }
                                break;
                            }
                        }

                        if (shouldAddEffect) {
                            player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier, true, false));
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void removeEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    private boolean hasNBTEffects(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            return nbtItem.hasKey("effects");
        }
        return false;
    }
}
