package net.novelmc.permban;

import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permban extends ConverseBase {
    private Converse plugin;

    public Permban(Converse plugin) {
        this.plugin = plugin;
    }

    public String constructBanMessage(String reason, String banID) {
        final StringBuilder banMessage = new StringBuilder(ChatColor.GRAY + "Unraveled");
        banMessage.append(ChatColor.DARK_GRAY + "MC\n");
        banMessage.append(ChatColor.RED + "You are permanently banned from this server!\n");
        if (!(reason.length() == 0)) {
            banMessage.append(ChatColor.GRAY + "Reason: " + ChatColor.WHITE + reason + "\n");
        }
        banMessage.append(ChatColor.GRAY + "More Info: " + ChatColor.GOLD + ChatColor.UNDERLINE + plugin.config.getString("banurl") +
                "\n");
        banMessage.append(ChatColor.GRAY + "Permban ID: " + ChatColor.WHITE + "#" + banID);
        return banMessage.toString();
    }

    public void addPermban(Player player, CommandSender sender, String banID, String reason, String type) {
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

    public void addPermban(OfflinePlayer player, CommandSender sender, String banID, String reason, String type) {
        plugin.permbanConfig.createSection(player.getUniqueId().toString());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".type", type);
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".reason", reason);
        plugin.permbanConfig.set(player.getUniqueId().toString() + ".id", banID);
        plugin.permbanConfig.save();
        plugin.permbanConfig.load();
    }

    public String getReason(Player player) {
        return plugin.permbanConfig.get(player.getUniqueId().toString() + ".reason").toString();
    }

    public String getBanID(Player player) {
        return plugin.permbanConfig.get(player.getUniqueId().toString() + ".id").toString();
    }

    public boolean isBanned(Player player) {
        return plugin.permbanConfig.isConfigurationSection(player.getUniqueId().toString());
    }

    public boolean isBanned(OfflinePlayer player) {
        return plugin.permbanConfig.isConfigurationSection(player.getUniqueId().toString());
    }


    public boolean removePermban(OfflinePlayer player) {
        if (plugin.permbanConfig.isConfigurationSection(player.getUniqueId().toString())) {
            plugin.permbanConfig.set(player.getUniqueId().toString(), null);
            plugin.permbanConfig.save();
            plugin.permbanConfig.load();
            return true;
        }
        return false;
    }

    public boolean removePermban(Player player) {
        if (plugin.permbanConfig.isConfigurationSection(player.getUniqueId().toString())) {
            plugin.permbanConfig.set(player.getUniqueId().toString(), null);
            plugin.permbanConfig.save();
            plugin.permbanConfig.load();
            return true;
        }
        return false;
    }
}
