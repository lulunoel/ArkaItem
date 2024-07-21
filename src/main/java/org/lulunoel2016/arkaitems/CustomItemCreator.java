package org.lulunoel2016.arkaitems;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import de.tr7zw.nbtapi.NBTItem;

import java.text.SimpleDateFormat;
import java.util.*;

public class CustomItemCreator {

    private JavaPlugin plugin;
    private FileConfiguration customConfig;

    public CustomItemCreator(JavaPlugin plugin, FileConfiguration customConfig) {
        this.plugin = plugin;
        this.customConfig = customConfig;
    }

    public ItemStack createCustomItem(String itemName, String owner) {
        String path = "Items." + itemName;
        String name = customConfig.getString(path + ".name");
        String materialStr = customConfig.getString(path + ".material");
        List<String> lore = customConfig.getStringList(path + ".lore");
        boolean unbreakable = customConfig.getBoolean(path + ".unbreakable");

        // Ensure the material is valid
        Material material = Material.getMaterial(materialStr.toUpperCase());
        if (material == null) {
            throw new IllegalArgumentException("Matériel utilisé incorrect: " + materialStr);
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            throw new IllegalStateException("Erreur avec les MetaData sur: " + materialStr);
        }

        if (name != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }

        if (lore != null) {
            List<String> coloredLore = new ArrayList<>();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            for (String line : lore) {
                line = line.replace("{owner}", owner).replace("{date}", date).replace("{mine}", "0").replace("{hunt}", "0");
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(coloredLore);
        }

        if (customConfig.contains(path + ".enchantments")) {
            Set<String> enchantmentKeys = customConfig.getConfigurationSection(path + ".enchantments").getKeys(false);
            if (enchantmentKeys != null) {
                for (String enchantment : enchantmentKeys) {
                    int level = customConfig.getInt(path + ".enchantments." + enchantment);
                    Enchantment enchant = Enchantment.getByName(enchantment.toUpperCase());
                    if (enchant != null) {
                        meta.addEnchant(enchant, level, true);
                    } else {
                        plugin.getLogger().warning("Invalid enchantment: " + enchantment);
                    }
                }
            }
        }

        item.setItemMeta(meta);

        // Set item as unbreakable using NBT API
        NBTItem nbtItem = new NBTItem(item);
        if (unbreakable) {
            nbtItem.setBoolean("Unbreakable", true);
        }

        // Apply custom NBT tags
        if (customConfig.contains(path + ".nbt")) {
            Set<String> keys = customConfig.getConfigurationSection(path + ".nbt").getKeys(false);
            if (keys != null) {
                for (String key : keys) {
                    Object value = customConfig.get(path + ".nbt." + key);
                    if (value instanceof Boolean) {
                        nbtItem.setBoolean(key, (Boolean) value);
                    } else if (value instanceof Integer) {
                        nbtItem.setInteger(key, (Integer) value);
                    } else if (value instanceof Double) {
                        nbtItem.setDouble(key, (Double) value);
                    } else if (value instanceof String) {
                        nbtItem.setString(key, (String) value);
                    } else if (value instanceof List) {
                        Gson gson = new Gson();
                        String json = gson.toJson(value);
                        nbtItem.setString(key, json);
                    }
                }
            }
        }

        // Set the owner tag
        nbtItem.setString("owner", owner);
        nbtItem.setInteger("mine", 0);
        nbtItem.setInteger("hunt", 0);

        // Set extraAttributes.id
        String id = customConfig.getString(path + ".extraAttributes.id");
        if (id != null) {
            nbtItem.setString("extraAttributes.id", id);
        }

        // Set saveondeath tag
        boolean saveOnDeath = customConfig.getBoolean(path + ".nbt.saveondeath", false);
        nbtItem.setBoolean("saveondeath", saveOnDeath);

        // Set fullset tag
        String fullSet = customConfig.getString(path + ".nbt.fullset");
        if (fullSet != null) {
            nbtItem.setString("fullset", fullSet);
        }

        // Set effetfullset tag
        List<Map<String, Object>> effetFullSet = (List<Map<String, Object>>) customConfig.get(path + ".effetfullset");
        if (effetFullSet != null) {
            Gson gson = new Gson();
            String json = gson.toJson(effetFullSet);
            nbtItem.setString("effetfullset", json);
        }

        item = nbtItem.getItem();

        return item;
    }
}
