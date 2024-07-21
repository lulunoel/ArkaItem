package org.lulunoel2016.arkaitems.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.lulunoel2016.arkaitems.ArkaItems;
import org.lulunoel2016.arkaitems.CustomItemCreator;

public class CustomItemCommand implements CommandExecutor {

    private ArkaItems plugin;
    private FileConfiguration customConfig;
    private CustomItemCreator itemCreator;

    public CustomItemCommand(ArkaItems plugin, FileConfiguration customConfig) {
        this.plugin = plugin;
        this.customConfig = customConfig;
        this.itemCreator = new CustomItemCreator(plugin, customConfig);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args.length > 2) {
            sender.sendMessage("Usage: /givecustomitem <item> [pseudo]");
            return true;
        }

        String itemName = args[0];
        Player targetPlayer = null;
        String targetPlayerName;

        if (args.length == 2) {
            targetPlayerName = args[1];
            targetPlayer = Bukkit.getPlayer(targetPlayerName);
        } else if (sender instanceof Player) {
            targetPlayer = (Player) sender;
            targetPlayerName = targetPlayer.getName();
        } else {
            sender.sendMessage("Vous devez spécifier un joueur si vous voulez faire cette commande depuis la console.");
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("customitem.give")) {
                player.sendMessage("Tu n'as pas la permission de touché à ça.");
                return true;
            }
        }

        if (targetPlayer == null) {
            sender.sendMessage("Joueur non trouvé: " + targetPlayerName);
            return true;
        }

        if (!customConfig.contains("Items." + itemName)) {
            sender.sendMessage("Item non trouvé: " + itemName);
            return true;
        }

        ItemStack item = itemCreator.createCustomItem(itemName, targetPlayerName);
        targetPlayer.getInventory().addItem(item);
        sender.sendMessage("Don de " + itemName + " à " + targetPlayerName);
        if (!sender.equals(targetPlayer)) {
            targetPlayer.sendMessage("Tu as reçu: " + itemName);
        }

        return true;
    }
}
