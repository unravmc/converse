package net.novelmc.commands;

import net.novelmc.Converse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ConverseCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (args.length == 0)
        {
            Converse.BuildProperties build = Converse.build;
            sender.sendMessage(ChatColor.GOLD + "Converse is a plugin by which the NovelMC server manages operator " +
                    "permissions, in-game functions, and staff member tools.");
            sender.sendMessage(ChatColor.GOLD + String.format("Version "
                            + ChatColor.BLUE + "%s.%s.%s",
                    build.version,
                    build.number,
                    build.head));
            sender.sendMessage(String.format(ChatColor.GOLD + "Compiled on "
                            + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                            + ChatColor.BLUE + "%s",
                    build.date,
                    build.author));
            sender.sendMessage(ChatColor.GREEN + "Visit " + ChatColor.AQUA + "https://github.com/novelmc/converse" + ChatColor.GREEN + " for more information");
            return true;
        }
        switch (args[0].toLowerCase())
        {


            case "debug":
            {
                if (!sender.hasPermission("converse.debug"))
                {
                    sender.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                sender.sendMessage(Converse.plugin.config.getString("banurl"));
                Converse.plugin.config.set("banurl", "https://novelmc.net");
                Converse.plugin.config.save();
                Converse.plugin.config.load();
                sender.sendMessage(Converse.plugin.config.getString("banurl"));
                Converse.plugin.banConfig.createSection("test");
                Converse.plugin.banConfig.save();
                Converse.plugin.banConfig.load();
                if (Converse.plugin.banConfig.isConfigurationSection("test"))
                {
                    sender.sendMessage("it worked");
                    return true;
                }
                return true;
            }
            case "reload":
            {
                if (!sender.hasPermission("converse.reload"))
                {
                    sender.sendMessage(Messages.NO_PERMISSION);
                    return true;
                }
                Converse.plugin.config.save();
                Converse.plugin.banConfig.save();
                Converse.plugin.config.load();
                Converse.plugin.banConfig.load();
                sender.sendMessage(ChatColor.GRAY + "The Converse configuration file has been reloaded.");
                return true;
            }
            default:
                return false;
        }
    }
}
