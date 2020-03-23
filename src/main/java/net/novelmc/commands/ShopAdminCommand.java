package net.novelmc.commands;

import net.novelmc.shop.ShopIndex;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class ShopAdminCommand implements CommandExecutor {
    private final String PLAYER_NOT_FOUND = ChatColor.GRAY + "That player cannot be found!";
    private final String INVALID_NUMBER = ChatColor.GRAY + "That is not a valid integer!";
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        
        if (args.length == 0) {
            return false;
        }
        
        if (args.length > 0 && !sender.hasPermission("converse.staff")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }
        
        if (args.length == 3) {
            int x;
            Player p;
            switch (args[0]) {
                case "set":
                    try {
                        p = Bukkit.getPlayer(args[1]);
                        x = Integer.parseInt(args[2]);
                        ShopIndex.setCoins(p, x);
                        sender.sendMessage(ChatColor.GRAY + p.getName() + " now has " + x + " coins!");
                        p.sendMessage(ChatColor.YELLOW + "You now have " + x + " coins!");
                    } catch (NullPointerException ex) {
                        sender.sendMessage(INVALID_NUMBER);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(PLAYER_NOT_FOUND);
                    } 
                    break;
                case "add":
                    try { 
                        p = Bukkit.getPlayer(args[1]);
                        x = Integer.parseInt(args[2]);
                        ShopIndex.addCoins(p, x);
                        sender.sendMessage(p.getName() + " now has " + x + " coins!");
                        p.sendMessage(ChatColor.YELLOW + "You now have " + x + " coins!");
                    } catch (NullPointerException ex) {
                        sender.sendMessage(PLAYER_NOT_FOUND);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(INVALID_NUMBER);
                    }
                    break;
                case "del":
                    try {
                        p = Bukkit.getPlayer(args[1]);
                        x = Integer.parseInt(args[2]);
                        if (!ShopIndex.coinMap.containsKey(p.getUniqueId())) {
                            sender.sendMessage("That player doesn't have any coins!");
                            break;
                        }
                        ShopIndex.removeCoins(p, x);
                        int index = ShopIndex.getCoins(p);
                        if (index == 0) {
                            sender.sendMessage(ChatColor.YELLOW + "This player no longer has any coins!");
                            break;
                        }
                        sender.sendMessage(ChatColor.GRAY + "You have removed " + x + " amount of coins from " + p.getName() + "\n" 
                        + ChatColor.GRAY + "This player now has " + index + " coins!");
                        p.sendMessage(ChatColor.YELLOW + "Your coin balance has been updated. It is now " + index + " coins.");
                    } catch (NullPointerException ex) {
                        sender.sendMessage(PLAYER_NOT_FOUND);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(INVALID_NUMBER);
                    }
                    break;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get")) {
                Player player;
                try {
                    player = Bukkit.getPlayer(args[1]);
                    int coins = ShopIndex.getCoins(player);
                    if (coins == 0) {
                        sender.sendMessage(ChatColor.GRAY + "There are no entries for that player, or their balance is 0.");
                        return true;
                    }
                    sender.sendMessage(ChatColor.GRAY + player.getName() + " has " + coins + " coins.");

                } catch (NullPointerException ex) {
                    sender.sendMessage(PLAYER_NOT_FOUND);
                }
            }
        }
        
        return true;
    }
}