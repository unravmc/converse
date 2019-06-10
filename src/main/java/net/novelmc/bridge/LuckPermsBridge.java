package net.novelmc.bridge;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import net.novelmc.Converse;
import org.bukkit.ChatColor;

public class LuckPermsBridge
{
    private static LuckPermsApi api = Converse.getLuckPermsAPI();

    public static boolean isModerator(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.getConfig().getString("permissions.moderator"));
    }

    public static boolean isSeniorModerator(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.getConfig().getString("permissions.senior_moderator"));
    }

    public static boolean isDeveloper(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.getConfig().getString("permissions.developer"));
    }

    public static boolean isExecutive(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.getConfig().getString("permissions.executive"));
    }

    public static boolean isArchitect(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(Converse.plugin.getConfig().getString("permissions.architect"));
    }

    public static String displayRank(UUID player)
    {
        if (isModerator(player))
        {
            return ChatColor.BLUE + "" + ChatColor.BOLD + "MOD";
        }
        if (isSeniorModerator(player))
        {
            return ChatColor.GOLD + "" + ChatColor.BOLD + "SRMOD";
        }
        if (isDeveloper(player))
        {
            return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "DEV";
        }
        if (isExecutive(player))
        {
            return ChatColor.RED + "" + ChatColor.BOLD + "EXEC";
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
