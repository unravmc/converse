package net.novelmc.banning;

import net.novelmc.Converse;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ban
{
    public static Converse plugin = Converse.plugin;

    public static String constructBanMessage(String reason, String banID)
    {
        final StringBuilder banMessage = new StringBuilder(ChatColor.BLUE + "" + ChatColor.BOLD
                + "NOVEL");
        banMessage.append(ChatColor.WHITE + "" + ChatColor.BOLD + "MC\n");
        banMessage.append(ChatColor.DARK_RED + "You are banned from this server!\n");
        if (!(reason.length() == 0))
        {
            banMessage.append(ChatColor.GRAY + "Reason: " + ChatColor.WHITE + reason + "\n");
        }
        banMessage.append(ChatColor.GRAY + "More Info: " + ChatColor.WHITE + plugin.config.getString("banurl") + "\n");
        banMessage.append(ChatColor.GRAY + "Ban ID: " + ChatColor.WHITE + "#" + banID);
        return banMessage.toString();
    }

    public static void addBan(Player player, CommandSender sender, String banID, String reason, String type)
    {
        plugin.banConfig.createSection(player.getUniqueId().toString());
        plugin.banConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        plugin.banConfig.set(player.getUniqueId().toString() + ".ip", player.getAddress().getHostString());
        plugin.banConfig.set(player.getUniqueId().toString() + ".type", type);
        plugin.banConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        plugin.banConfig.set(player.getUniqueId().toString() + ".reason", reason);
        plugin.banConfig.set(player.getUniqueId().toString() + ".id", banID);
        plugin.banConfig.save();
        plugin.banConfig.load();
    }

    public static void addBan(OfflinePlayer player, CommandSender sender, String banID, String reason, String type)
    {
        plugin.banConfig.createSection(player.getUniqueId().toString());
        plugin.banConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        if (player.hasPlayedBefore())
        {
            plugin.banConfig.set(player.getUniqueId().toString() + ".ip", player.getPlayer().getAddress().getHostString());
        }
        plugin.banConfig.set(player.getUniqueId().toString() + ".type", type);
        plugin.banConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        plugin.banConfig.set(player.getUniqueId().toString() + ".reason", reason);
        plugin.banConfig.set(player.getUniqueId().toString() + ".id", banID);
        plugin.banConfig.save();
        plugin.banConfig.load();
    }

    public static String getReason(Player player)
    {
        return plugin.banConfig.get(player.getUniqueId().toString() + ".reason").toString();
    }

    public static String getBanID(Player player)
    {
        return plugin.banConfig.get(player.getUniqueId().toString() + ".id").toString();
    }

    public static boolean isBanned(Player player)
    {
        return plugin.banConfig.isConfigurationSection(player.getUniqueId().toString());
    }

    public static boolean removeBan(OfflinePlayer player)
    {
        if (plugin.banConfig.isConfigurationSection(player.getUniqueId().toString()))
        {
            plugin.banConfig.set(player.getUniqueId().toString(), null);
            plugin.banConfig.save();
            plugin.banConfig.load();
            return true;
        }
        return false;
    }

    public static boolean removeBan(Player player)
    {
        if (plugin.banConfig.isConfigurationSection(player.getUniqueId().toString()))
        {
            plugin.banConfig.set(player.getUniqueId().toString(), null);
            plugin.banConfig.save();
            plugin.banConfig.load();
            return true;
        }
        return false;
    }
}
