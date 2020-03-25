package net.novelmc.commands;

import org.bukkit.command.*;
import net.novelmc.util.CoinIndex;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {
    private final CoinIndex index = new CoinIndex();
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("You can't do that!");
            return true;
        }
        
        Player p = (Player) cs;
        int tCoins = index.getCoins(p);
        if (tCoins == 0) {
            p.sendMessage(ChatColor.GRAY + "You don't have any coins!");
            return true;
        }
        p.sendMessage(ChatColor.GRAY + "You have " + tCoins + " coins.");
        
        return true;
    }
}