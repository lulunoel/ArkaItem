package org.lulunoel2016.arkaitems;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lulunoel2016.arkaitems.commands.CustomItemCommand;
import org.lulunoel2016.arkaitems.listeners.*;

import java.io.File;
import java.io.IOException;

public final class ArkaItems extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    @Override
    public void onEnable() {
        // Créer la configuration personnalisée
        createCustomConfig();
        getLogger().info("===================================[ Arka Items ]===================================");
        getLogger().info(" ");

        // Enregistrer les commandes
        this.getCommand("givecustomitem").setExecutor(new CustomItemCommand(this, customConfig));
        Utils.logInfo("Chargement de givecustomitem!", "blue");

        // Enregistrer les listeners
        this.getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
        Utils.logInfo("Chargement de ItemDropListener!", "green");

        this.getServer().getPluginManager().registerEvents(new PlayerKillListener(), this);
        Utils.logInfo("Chargement de PlayerKillListener!", "green");

        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        Utils.logInfo("Chargement de BlockBreakListener!", "green");

        this.getServer().getPluginManager().registerEvents(new MobKillListener(), this);
        Utils.logInfo("Chargement de MobKillListener!", "green");

        this.getServer().getPluginManager().registerEvents(new ItemEffectListener(this), this);
        Utils.logInfo("Chargement de ItemEffectListener!", "green");

        this.getServer().getPluginManager().registerEvents(new BlockTravelListener(), this);
        Utils.logInfo("Chargement de BlockTravelListener!", "green");

        this.getServer().getPluginManager().registerEvents(new ItemUsageListener(this), this);
        Utils.logInfo("Chargement de ItemUsageListener!", "green");

        this.getServer().getPluginManager().registerEvents(new ArmorSetListener(), this);
        Utils.logInfo("Chargement de ArmorSetListener!", "green");

        CustomItemCreator itemCreator = new CustomItemCreator(this, customConfig);
        Utils.logInfo("Chargement de CustomItemCreator!", "green");

        // Créer la configuration par défaut
        saveDefaultConfig();
        Utils.logInfo("ArkaItems has been enabled!", "green");
        getLogger().info(" ");
        getLogger().info("====================================================================================");


    }

    @Override
    public void onDisable() {
        Utils.logInfo("ArkaItems has been disabled.", "red");
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "items.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("items.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    public void saveCustomConfig() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
