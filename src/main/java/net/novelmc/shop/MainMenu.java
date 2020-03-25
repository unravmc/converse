package net.novelmc.shop;

import net.novelmc.util.ShopIndex;
import net.novelmc.Converse;
import net.novelmc.util.HoverIndex;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends ShopIndex {
    public MainMenu() {
        super(9, "UnraveledMC Shop");
        ItemStack is1 = newItem(Material.NETHER_STAR, "Trails", "Use this to get trails with voter coins!");
        ItemStack is2 = newItem(Material.PLAYER_HEAD, "Players", "Use this to open a list of all players.");
        ItemStack is3 = newItem(Material.FEATHER, "Hover Chat", "Hover chat test. Click this to close the gui and print a hoverable text block!");
        
        
        
        setItem(0, is1, player -> Converse.plugin.trails.open(player));
        
        setItem(1, is2, player -> Converse.plugin.players.open(player));
        
        setItem(2, is3, player -> {
            HoverIndex hover = new HoverIndex(player, ChatColor.RED, ChatColor.GRAY, "Hoverable Text", "This is a test for some hoverable text!!");
            hover.newChat();
            openInventories.remove(player.getUniqueId());
        });
    }
}