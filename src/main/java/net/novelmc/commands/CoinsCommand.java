package net.novelmc.commands;

import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import org.bukkit.command.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoinsCommand extends ConverseBase implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("You can't do that!");
            return true;
        }

        Player p = (Player) cs;
        int tCoins = plugin.playerDataManager.getPlayerData(p).getCoins();
        if (tCoins == 0) {
            p.sendMessage(ChatColor.GRAY + "You don't have any coins.");
            return true;
        }
        p.sendMessage(ChatColor.GRAY + "You have " + tCoins + " coins.");
        return true;
    }
}
