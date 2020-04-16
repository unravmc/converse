package net.novelmc.commands.loader;

import net.novelmc.ConversePlugin;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class CommandBase {
    protected ConversePlugin plugin;
    protected Server server;
    private CommandSender commandSender;
    private Class<?> commandClass;
    private static String permissionPrefix = "converse\\.";

    public CommandBase() {}

    abstract public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args);

    public void setup(final ConversePlugin plugin, final CommandSender cs, final Class<?> clazz) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.commandSender = cs;
        this.commandClass = clazz;
    }

    /*
    public static boolean hasPermission(CommandSender sender, String permission) {
        Collection<? extends String> col = CommandLoader.getInstance().getPermissions();
        if (col.contains(permission)) {
            if (!(sender instanceof Player)) {
                return true;
            }

            if (!sender.hasPermission(permissionPrefix + permission)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean hasPermission(Player player, String permission) {
        return hasPermission((CommandSender) player, permission);
    }
     */

    public Player getPlayer(final String partial) {
        List<Player> matcher = server.matchPlayer(partial);
        if (matcher.isEmpty()) {
            for (Player p : server.getOnlinePlayers()) {
                if (p.getDisplayName().toLowerCase().contains(partial.toLowerCase())) {
                    return p;
                }
            }
            return null;
        } else {
            return matcher.get(0);
        }
    }
}
