package net.novelmc.bridge;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import net.novelmc.Converse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LuckPermsBridge
{
    private static LuckPermsApi api = Converse.getLuckPermsAPI();

    public static boolean isModerator(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.config.getString("permissions.moderator"));
    }

    public static boolean isSeniorModerator(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.config.getString("permissions.senior_moderator"));
    }

    public static boolean isDeveloper(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.config.getString("permissions.developer"));
    }

    public static boolean isExecutive(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.config.getString("permissions.executive"));
    }

    public static boolean isArchitect(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.config.getString("permissions.architect"));
    }

    public static boolean isStaff(UUID player)
    {
        return isModerator(player) || isSeniorModerator(player) || isDeveloper(player) || isExecutive(player);
    }

    public static String displayRank(Player player)
    {
        if (player == null)
        {
            return ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "CONSOLE";
        }

        if (isModerator(player.getUniqueId()))
        {
            return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "MOD";
        }
        if (isSeniorModerator(player.getUniqueId()))
        {
            return ChatColor.GOLD + "" + ChatColor.BOLD + "SRM";
        }
        if (isDeveloper(player.getUniqueId()))
        {
            return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "DEV";
        }
        if (isExecutive(player.getUniqueId()))
        {
            return ChatColor.RED + "" + ChatColor.BOLD + "EXEC";
        }
        if (isArchitect(player.getUniqueId()))
        {
            return ChatColor.BLUE + "" + ChatColor.BOLD + "ART";
        }

        return ChatColor.GREEN + "" + ChatColor.BOLD + "OP";
    }

    public static void set(UUID uuid, String group)
    {
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);

        userFuture.thenAcceptAsync(user ->
        {
            Node oldGroup = api.buildNode("group." + user.getPrimaryGroup()).build();
            Node newGroup = api.buildNode("group." + group).build();
            if (oldGroup == newGroup)
            {
                return;
            }
            user.unsetPermission(oldGroup);
            user.setPermission(newGroup);
            userManager.saveUser(user);
        });
    }
}
