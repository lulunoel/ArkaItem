package org.lulunoel2016.arkaitems;

import org.bukkit.Bukkit;

public class Utils {

    public static void logInfo(String message, String color) {
        String colorCode;
        switch (color.toLowerCase()) {
            case "red":
                colorCode = "[ArkaItems] \u001B[31m";
                break;
            case "green":
                colorCode = "[ArkaItems] \u001B[32m";
                break;
            case "yellow":
                colorCode = "[ArkaItems] \u001B[33m";
                break;
            case "blue":
                colorCode = "[ArkaItems] \u001B[34m";
                break;
            case "purple":
                colorCode = "[ArkaItems] \u001B[35m";
                break;
            case "cyan":
                colorCode = "[ArkaItems] \u001B[36m";
                break;
            case "white":
                colorCode = "[ArkaItems] \u001B[37m";
                break;
            default:
                colorCode = "[ArkaItems]\u001B[0m"; // Reset
                break;
        }
        Bukkit.getConsoleSender().sendMessage(colorCode + message + "\u001B[0m");
    }
}
