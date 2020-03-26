package net.novelmc.commands;

import net.novelmc.Converse;
import net.novelmc.permban.Permban;
import net.novelmc.util.CoinIndex;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopAdminCommand implements CommandExecutor {
    private final String PLAYER_NOT_FOUND = ChatColor.GRAY + "That player cannot be found!";
    private final String INVALID_NUMBER = ChatColor.GRAY + "That is not a valid integer!";
    private CoinIndex coins = Converse.plugin.coinIndex;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, @NotNull String[] args) {

        if (args.length == 0) {
            return false;
        }

        if (!sender.hasPermission("converse.shop.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 3) {
            Player p = Bukkit.getPlayer(args[1]);

            int x;
            try {
                x = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(INVALID_NUMBER);
                return true;
            }

            if (p != null) {
                switch (args[0].toLowerCase()) {
                    case "set":
                        coins.setCoins(p, x);
                        sender.sendMessage(ChatColor.GRAY + p.getName() + " now has " + x + " coins!");
                        p.sendMessage(ChatColor.YELLOW + "You now have " + x + " coins!");
                        break;
                    case "add":
                        coins.addCoins(p, x);
                        sender.sendMessage(p.getName() + " now has " + x + " coins!");
                        p.sendMessage(ChatColor.YELLOW + "You now have " + x + " coins!");
                        break;
                    case "del":

                        if (!coins.coinMap.containsKey(p.getUniqueId())) {
                            sender.sendMessage("That player doesn't have any coins!");
                            break;
                        }
                        coins.removeCoins(p, x);
                        int index = coins.getCoins(p);
                        if (index == 0) {
                            sender.sendMessage(ChatColor.YELLOW + "This player no longer has any coins!");
                            break;
                        }
                        sender.sendMessage(
                                ChatColor.GRAY + "You have removed " + x + " amount of coins from " + p.getName() + "\n"
                                        + ChatColor.GRAY + "This player now has " + index + " coins!");
                        p.sendMessage(ChatColor.YELLOW + "Your coin balance has been updated. It is now " + index +
                                " coins.");
                        break;
                }
            } else {
                sender.sendMessage(PLAYER_NOT_FOUND);
            }

            return true;

        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get")) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    int tCoins = coins.getCoins(player);
                    if (tCoins == 0) {
                        sender.sendMessage(
                                ChatColor.GRAY + "There are no entries for that player, or their balance is 0.");
                        return true;
                    }
                    sender.sendMessage(ChatColor.GRAY + player.getName() + " has " + tCoins + " coins.");
                } else {
                    sender.sendMessage(PLAYER_NOT_FOUND);
                    return true;
                }
            }
        }

        return true;
    }


}