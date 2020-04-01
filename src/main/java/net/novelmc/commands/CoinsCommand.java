package net.novelmc.commands;

import net.novelmc.Converse;
import org.bukkit.command.*;
import net.novelmc.util.CoinIndex;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CoinsCommand implements CommandExecutor {
    private final CoinIndex index = Converse.plugin.coinIndex;

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("You can't do that!");
            return true;
        }

        Player p = (Player) cs;
        UUID uuid = p.getUniqueId();
        int tCoins = index.load(uuid);
        if (tCoins == 0) {
            p.sendMessage(ChatColor.GRAY + "You don't have any coins.");
            return true;
        }
        p.sendMessage(ChatColor.GRAY + "You have " + tCoins + " coins.");

        return true;
    }
}
