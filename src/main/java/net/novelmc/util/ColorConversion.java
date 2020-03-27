package net.novelmc.util;

import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ColorConversion {
    private static Map<org.bukkit.ChatColor, ChatColor> colors = new HashMap<>();
    private static Map<ChatColor, org.bukkit.ChatColor> colorMap = new HashMap<>();

    public static ChatColor convert(org.bukkit.ChatColor color) {
        Arrays.stream(org.bukkit.ChatColor.values()).forEachOrdered(translatable -> {
            Arrays.stream(ChatColor.values()).forEachOrdered(value -> colors.put(translatable, value));
        });

        return colors.get(color);
    }

    public static org.bukkit.ChatColor convert(ChatColor color) {
        Arrays.stream(ChatColor.values()).forEachOrdered(translatable -> {
            Arrays.stream(org.bukkit.ChatColor.values()).forEachOrdered(value -> colorMap.put(translatable, value));
        });

        return colorMap.get(color);
    }
}
