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
                + "Novel");
        banMessage.append(ChatColor.WHITE + "" + ChatColor.GOLD + "MC\n");
        banMessage.append(ChatColor.DARK_RED + "You are banned from this server!\n");
        if (!(reason.length() == 0))
        {
            banMessage.append(ChatColor.DARK_GRAY + "Reason: " + ChatColor.GRAY + reason + "\n");
        }
        banMessage.append(ChatColor.DARK_GRAY + "More info: " + ChatColor.GRAY + Converse.plugin.config.getString("banurl") + "\n");
        banMessage.append(ChatColor.DARK_GRAY + "Ban ID: " + ChatColor.GRAY + "#" + banID);
        return banMessage.toString();
    }

    public static void addBan(Player player, CommandSender sender, String banID, String reason, String type)
    {
        Converse.plugin.banConfig.createSection(player.getUniqueId().toString());
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".ip", player.getAddress().getHostString());
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".type", type);
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".reason", reason);
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".id", banID);
        Converse.plugin.banConfig.save();
        Converse.plugin.banConfig.load();
    }

    public static void addBan(OfflinePlayer player, CommandSender sender, String banID, String reason, String type)
    {
        Converse.plugin.banConfig.createSection(player.getUniqueId().toString());
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        if (player.hasPlayedBefore())
        {
            Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".ip", player.getPlayer().getAddress().getHostString());
        }
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".type", type);
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".reason", reason);
        Converse.plugin.banConfig.set(player.getUniqueId().toString() + ".id", banID);
        Converse.plugin.banConfig.save();
        Converse.plugin.banConfig.load();
    }

    public static String getReason(Player player)
    {
        return Converse.plugin.banConfig.get(player.getUniqueId().toString() + ".reason").toString();
    }

    public static String getBanID(Player player)
    {
        return Converse.plugin.banConfig.get(player.getUniqueId().toString() + ".id").toString();
    }

    public static boolean isBanned(Player player)
    {
        return Converse.plugin.banConfig.isConfigurationSection(player.getUniqueId().toString());
    }

    public static boolean removeBan(OfflinePlayer player)
    {
        if (Converse.plugin.banConfig.isConfigurationSection(player.getUniqueId().toString()))
        {
            Converse.plugin.banConfig.set(player.getUniqueId().toString(), null);
            Converse.plugin.banConfig.save();
            Converse.plugin.banConfig.load();
            return true;
        }
        return false;
    }
}