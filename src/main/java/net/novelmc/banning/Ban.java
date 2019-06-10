package net.novelmc.banning;

import java.io.IOException;
import net.novelmc.Converse;
import net.novelmc.config.BanConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
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
        banMessage.append(ChatColor.DARK_GRAY + "More info: " + ChatColor.GRAY + "https://novelmc.net/appeal\n");
        banMessage.append(ChatColor.DARK_GRAY + "Ban ID: " + ChatColor.GRAY + "#" + banID);
        return banMessage.toString();
    }

    public static void addBan(Player player, CommandSender sender, String banID, String reason, String type) throws IOException, InvalidConfigurationException
    {
        BanConfig.getConfig().createSection(player.getUniqueId().toString());
        BanConfig.getConfig().set(player.getUniqueId().toString() + ".player", player.getName());
        BanConfig.getConfig().set(player.getUniqueId().toString() + ".ip", player.getAddress().getHostName());
        BanConfig.getConfig().set(player.getUniqueId().toString() + ".type", type);
        BanConfig.getConfig().set(player.getUniqueId().toString() + ".by", sender.getName());
        BanConfig.getConfig().set(player.getUniqueId().toString() + ".reason", reason);
        BanConfig.getConfig().set(player.getUniqueId().toString() + ".id", banID);
        BanConfig.save();
        YamlConfiguration.loadConfiguration(BanConfig.config);

    }

    public static String getReason(Player player)
    {
        return BanConfig.getConfig().get(player.getUniqueId().toString() + ".reason").toString();
    }

    public static String getBanID(Player player)
    {
        return BanConfig.getConfig().get(player.getUniqueId().toString() + ".id").toString();
    }

    public static boolean isBanned(Player player)
    {
        return BanConfig.getConfig().isConfigurationSection(player.getUniqueId().toString());
    }
}