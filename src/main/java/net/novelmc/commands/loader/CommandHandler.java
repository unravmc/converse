package net.novelmc.commands.loader;

import net.novelmc.ConversePlugin;
import net.novelmc.commands.Adminchat;
import net.novelmc.commands.Converse;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {
    public static final String COMMAND_PATH = Converse.class.getPackage().getName();
    public static final String DEFINER = "Command";

    public static boolean handle(CommandSender sender, Command cmd, String lbl, String[] args) {
        final Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
            Bukkit.getLogger().info(String.format("[PLAYER COMMAND] %s (%s): /%s %s",
                    player.getName(),
                    ChatColor.stripColor(player.getDisplayName()),
                    lbl,
                    StringUtils.join(args, ' ')));
        } else {
            player = null;
            Bukkit.getLogger().info(String.format("[CONSOLE COMMAND] %s: /%s %s",
                    sender.getName(),
                    lbl,
                    StringUtils.join(args, ' ')));
        }

        final CommandBase base;
        try {
            final ClassLoader loader = ConversePlugin.class.getClassLoader();
            base = (CommandBase) loader.loadClass(COMMAND_PATH + "." + cmd.getName()).newInstance();
            base.setup(ConversePlugin.plugin, sender, base.getClass());
        } catch (Exception ex) {
            Bukkit.getLogger().severe("Could not load command: " + cmd.getName());
            Bukkit.getLogger().severe(ex.getMessage());

            sender.sendMessage(ChatColor.RED + "Command error! Could not load command: " + cmd.getName());
            return true;
        }

        try {
            return base.onCommand(sender, cmd, lbl, args);
        } catch (Exception ex) {
            Bukkit.getLogger().severe("Command Error: " + lbl);
            Bukkit.getLogger().severe(ex.getMessage());
            sender.sendMessage(ChatColor.RED + "COMMAND ERROR: " + ex.getMessage());
        }

        return true;
    }
}
