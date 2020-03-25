package net.novelmc.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class HoverIndex extends ConverseBase {
    private final Player player;
    private final ChatColor color;
    private final ChatColor color2;
    private final String fuzzy;
    private final String fuzzy2;
    
    public HoverIndex(Player player, ChatColor color, ChatColor color2, String fuzzy, String fuzzy2) {       
        this.player = player;
        this.color = color;
        this.color2 = color2;
        this.fuzzy = fuzzy;
        this.fuzzy2 = fuzzy2;
    }
    
    public void newChat() {
        TextComponent comp1 = new TextComponent(fuzzy);
        comp1.setColor(color);
        comp1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(fuzzy2).color(color2).create()));
        player.spigot().sendMessage(comp1);
    }
}