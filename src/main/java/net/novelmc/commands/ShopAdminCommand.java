package net.novelmc.commands;

import net.novelmc.Converse;
import net.novelmc.playerdata.PlayerData;
import net.novelmc.util.ConverseBase;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopAdminCommand extends ConverseBase implements CommandExecutor {
    private final String PLAYER_NOT_FOUND = ChatColor.GRAY + "That player cannot be found!";
    private final String INVALID_NUMBER = ChatColor.GRAY + "That is not a valid integer!";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

        if (args.length == 0) {
            return false;
        }

        if (!sender.hasPermission("converse.shop.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 3) {

            int x;
            try {
                x = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(INVALID_NUMBER);
                return true;
            }


            if (args[1].equalsIgnoreCase("all")) {

                switch (args[0].toLowerCase()) {
                    case "set":
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            PlayerData pData = plugin.playerDataManager.getPlayerData(p);
                            pData.setCoins(x);
                            p.sendMessage(ChatColor.YELLOW + "You now have " + x + " coins!");
                        });
                        sender.sendMessage(ChatColor.GRAY + "Everyone now has " + x + " coins!");
                        if (sender instanceof Player) Util.action(sender, "Set everyone's coins to " + x);
                        return true;
                    case "add":
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            PlayerData pData = plugin.playerDataManager.getPlayerData(p);
                            pData.addCoins(x);
                            p.sendMessage(ChatColor.YELLOW + "You now have " + pData.getCoins() + " coins!");
                        });
                        sender.sendMessage("Everyone has been given " + x + " coins!");
                        if (sender instanceof Player)
                            Util.action(sender, "Added " + x + " coins to everyone's balance.");
                        return true;
                    case "del":
                        sender.sendMessage("You cannot remove everyone's coins.");
                        return true;
                }
            }

            Player p = Bukkit.getPlayer(args[1]);
            if (p == null) {
                sender.sendMessage(PLAYER_NOT_FOUND);
                return true;
            }
            PlayerData pData = plugin.playerDataManager.getPlayerData(p);
            switch (args[0].toLowerCase()) {
                case "set":
                    pData.setCoins(x);
                    sender.sendMessage(ChatColor.GRAY + p.getName() + " now has " + x + " coins!");
                    p.sendMessage(ChatColor.YELLOW + "You now have " + x + " coins!");
                    if (sender instanceof Player) Util.action(sender, "Set " + p.getName() + "'s coins to " + x);
                    return true;
                case "add":
                    pData.addCoins(x);
                    sender.sendMessage(p.getName() + " now has " + pData.getCoins() + " coins!");
                    p.sendMessage(ChatColor.YELLOW + "You now have " + pData.getCoins() + " coins!");
                    if (sender instanceof Player)
                        Util.action(sender, "Added " + pData.getCoins() + " coins to " + p.getName() + "'s balance.");
                    return true;
                case "del":
                    pData.removeCoins(x);
                    int index = pData.getCoins();
                    if (index == 0) {
                        sender.sendMessage(ChatColor.YELLOW + "This player no longer has any coins!");
                        return true;
                    }
                    sender.sendMessage(
                            ChatColor.GRAY + "You have removed " + x + " amount of coins from " + p.getName() + "\n"
                                    + ChatColor.GRAY + "This player now has " + index + " coins!");
                    p.sendMessage(ChatColor.YELLOW + "Your coin balance has been updated. It is now " + index +
                            " coins.");
                    return true;
                default:
                    return false;
            }

        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get")) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    PlayerData pData = plugin.playerDataManager.getPlayerData(player);
                    int tCoins = pData.getCoins();
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