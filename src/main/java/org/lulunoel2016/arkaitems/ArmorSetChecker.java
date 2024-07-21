package org.lulunoel2016.arkaitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import de.tr7zw.nbtapi.NBTItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorSetChecker {

    private static JavaPlugin plugin;
    private static Map<Player, ItemStack[]> previousArmorItems = new HashMap<>();

    public static void init(JavaPlugin plugin) {
        ArmorSetChecker.plugin = plugin;
    }

    public static boolean hasFullSet(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        String fullSetTag = null;

        for (ItemStack item : armor) {
            if (item == null || item.getType() == Material.AIR) {
                return false;
            }
            NBTItem nbtItem = new NBTItem(item);
            String fullSet = nbtItem.getString("fullset");
            if (fullSet == null || fullSet.isEmpty()) {
                return false;
            }
            if (fullSetTag == null) {
                fullSetTag = fullSet;
            } else if (!fullSetTag.equals(fullSet)) {
                return false;
            }
        }

        return true;
    }

    public static void applyOrRemoveEffects(Player player) {
        if (hasFullSet(player)) {
            Map<String, Integer> newEffects = new HashMap<>();

            for (ItemStack item : player.getInventory().getArmorContents()) {
                if (item == null || item.getType() == Material.AIR) continue;

                NBTItem nbtItem = new NBTItem(item);
                String effectsJson = nbtItem.getString("effetfullset");
                if (effectsJson != null && !effectsJson.isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                    List<Map<String, Object>> effects = gson.fromJson(effectsJson, listType);
                    for (Map<String, Object> effect : effects) {
                        String effectType = (String) effect.get("effect");
                        int level = ((Double) effect.get("level")).intValue();
                        newEffects.put(effectType.toUpperCase(), level);
                    }
                }
            }

            applyNewEffects(player, newEffects);

            // Mettre à jour l'armure précédente
            previousArmorItems.put(player, player.getInventory().getArmorContents().clone());

        } else {
            // Vérifiez l'armure précédente avant de supprimer les effets
            ItemStack[] previousArmor = previousArmorItems.get(player);
            boolean hadEffectFullSet = false;

            if (previousArmor != null) {
                for (ItemStack item : previousArmor) {
                    if (item != null && item.getType() != Material.AIR) {
                        NBTItem nbtItem = new NBTItem(item);
                        if (nbtItem.hasKey("effetfullset")) {
                            hadEffectFullSet = true;
                            break;
                        }
                    }
                }
            }

            if (hadEffectFullSet) {
                removeEffects(player);
            }
        }
    }

    private static void applyNewEffects(Player player, Map<String, Integer> newEffects) {
        for (Map.Entry<String, Integer> entry : newEffects.entrySet()) {
            PotionEffectType potionEffectType = PotionEffectType.getByName(entry.getKey());
            if (potionEffectType != null) {
                boolean shouldAddEffect = true;

                for (PotionEffect currentEffect : player.getActivePotionEffects()) {
                    if (currentEffect.getType().equals(potionEffectType)) {
                        if (currentEffect.getAmplifier() >= entry.getValue()) {
                            shouldAddEffect = false;
                        }
                        break;
                    }
                }

                if (shouldAddEffect) {
                    player.addPotionEffect(new PotionEffect(potionEffectType, Integer.MAX_VALUE, entry.getValue(), true, false));
                }
            }
        }
    }

    private static void removeEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}
