package net.novelmc.permban;

import net.novelmc.Converse;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permban
{
    public static Converse plugin = Converse.plugin;

    public static String constructBanMessage(String reason, String banID)
    {
        final StringBuilder banMessage = new StringBuilder(ChatColor.BLUE + "" + ChatColor.BOLD
                + "NOVEL");
        banMessage.append(ChatColor.WHITE + "" + ChatColor.BOLD + "MC\n");
        banMessage.append(ChatColor.RED + "You are permanently banned from this server!\n");
        if (!(reason.length() == 0))
        {
            banMessage.append(ChatColor.DARK_GRAY + "Reason: " + ChatColor.GRAY + reason + "\n");
        }
        banMessage.append(ChatColor.DARK_GRAY + "More Info: " + ChatColor.GRAY + plugin.getConfig().getString("banurl") + "\n");
        banMessage.append(ChatColor.DARK_GRAY + "Permban ID: " + ChatColor.GRAY + "#" + banID);
        return banMessage.toString();
    }

    public static void addPermban(Player player, CommandSender sender, String banID, String reason, String type)
    {
        plugin.permbanConfig.createSection(player.getUniqueId().toString());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".ip", player.getAddress().getHostString());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".type", type);
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".reason", reason);
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".id", banID);
        plugin.permbanConfig.save();
        plugin.permbanConfig.load();
    }

    public static void addPermban(OfflinePlayer player, CommandSender sender, String banID, String reason, String type)
    {
        plugin.permbanConfig.createSection(player.getUniqueId().toString());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".type", type);
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".reason", reason);
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".id", banID);
        plugin.permbanConfig.save();
        plugin.permbanConfig.load();
    }

    public static String getReason(Player player)
    {
        return plugin.permbanConfig.get(player.getUniqueId().toString() + ".reason").toString();
    }

    public static String getBanID(Player player)
    {
        return plugin.permbanConfig.get(player.getUniqueId().toString() + ".id").toString();
    }

    public static boolean isBanned(Player player)
    {
        return plugin.permbanConfig.isConfigurationSection(player.getUniqueId().toString());
    }

    public static boolean removePermban(OfflinePlayer player)
    {
        if (plugin.permbanConfig.isConfigurationSection(player.getUniqueId().toString()))
        {
            plugin.permbanConfig.set(player.getUniqueId().toString(), null);
            plugin.permbanConfig.save();
            plugin.permbanConfig.load();
            return true;
        }
        return false;
    }

    public static boolean removePermban(Player player)
    {
        if (plugin.permbanConfig.isConfigurationSection(player.getUniqueId().toString()))
        {
            plugin.permbanConfig.set(player.getUniqueId().toString(), null);
            plugin.permbanConfig.save();
            plugin.permbanConfig.load();
            return true;
        }
        return false;
    }
}
