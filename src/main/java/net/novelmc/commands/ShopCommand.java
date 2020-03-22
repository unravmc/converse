package net.novelmc.commands;

import net.novelmc.Converse;
import net.novelmc.shop.ShopIndex;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor 
{
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage("This command can only be run by players.");
            return true;
        }
        
        if (args.length == 0) {
            Player p = (Player) sender;
            Converse.plugin.shop.open(p);
            return true;
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
                    p = Bukkit.getPlayer(args[1]);
                    if (p == null) {
                        sender.sendMessage(ChatColor.GRAY + "That player cannot be found!");
                        break;
                    }
                    try {
                        x = Integer.parseInt(args[2]);
                        ShopIndex.setCoins(p, x);
                        sender.sendMessage(ChatColor.GRAY + p.getName() + " now has " + x + " coins!");
                        p.sendMessage(ChatColor.YELLOW + "You now have " + x + " coins!");
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.GRAY + "That is not a valid integer!");
                    }
                    break;
                case "add":
                    p = Bukkit.getPlayer(args[1]);
                    if (p == null) {
                        sender.sendMessage(ChatColor.GRAY + "That player cannot be found!");
                        break;
                    }
                    try { 
                        x = Integer.parseInt(args[2]);
                        ShopIndex.addCoins(p, x);
                        sender.sendMessage(p.getName() + " now has " + x + " coins!");
                        p.sendMessage(ChatColor.YELLOW + "You now have " + x + " coins!");
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.GRAY + "That is not a valid integer!");
                    }
                    break;
                case "del":
                    p = Bukkit.getPlayer(args[1]);
                    if (p == null) {
                        sender.sendMessage(ChatColor.GRAY + "That player cannot be found!");
                        break;
                    }
                    try {
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
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.GRAY + "That is not a valid integer!");
                    }
                    break;
            }
            return true;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get")) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.GRAY + "That player cannot be found!");
                    return true;
                }
                int coins = ShopIndex.getCoins(player);
                if (coins == 0) {
                    sender.sendMessage(ChatColor.GRAY + "There are no entries for that player, or their balance is 0.");
                    return true;
                }
                sender.sendMessage(ChatColor.GRAY + player.getName() + " has " + coins + " coins.");
            }
        }
        //in case it doesn't already return true???
        return true;
    }
}