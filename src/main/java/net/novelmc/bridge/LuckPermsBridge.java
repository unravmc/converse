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
    private LuckPermsApi api = Converse.getLuckPermsAPI();

    private Converse plugin;

    public LuckPermsBridge(Converse plugin)
    {
        this.plugin = plugin;
    }

    public boolean isModerator(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(plugin.config.getString("permissions.mod"));
    }

    public boolean isSeniorModerator(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(plugin.config.getString("permissions.senior_mod"));
    }

    public boolean isDeveloper(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(plugin.config.getString("permissions.developer"));
    }

    public boolean isExecutive(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(plugin.config.getString("permissions.executive"));
    }

    public boolean isArchitect(UUID player)
    {
        User user = api.getUserManager().getUser(player);
        return user.getPrimaryGroup().equalsIgnoreCase(plugin.config.getString("permissions.architect"));
    }

    public boolean isStaff(UUID player)
    {
        return isModerator(player) || isSeniorModerator(player) || isDeveloper(player) || isExecutive(player);
    }

    public String displayRank(Player player)
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

    public ChatColor displayRankColor(Player player)
    {
        if (player == null)
        {
            return ChatColor.DARK_AQUA;
        }

        if (isModerator(player.getUniqueId()))
        {
            return ChatColor.DARK_GREEN;
        }
        if (isSeniorModerator(player.getUniqueId()))
        {
            return ChatColor.GOLD;
        }
        if (isDeveloper(player.getUniqueId()))
        {
            return ChatColor.DARK_PURPLE;
        }
        if (isExecutive(player.getUniqueId()))
        {
            return ChatColor.RED;
        }
        if (isArchitect(player.getUniqueId()))
        {
            return ChatColor.BLUE;
        }

        return ChatColor.RESET;
    }

    public void set(UUID uuid, String group)
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

    public void allowStaffWorld(UUID uuid)
    {
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);

        userFuture.thenAcceptAsync(user ->
        {
            Node permission = api.buildNode("multiverse.access.staffworld").build();
            user.setPermission(permission);
            userManager.saveUser(user);
        });
    }

    public void disallowStaffWorld(UUID uuid)
    {
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);

        userFuture.thenAcceptAsync(user ->
        {
            Node permission = api.buildNode("multiverse.access.staffworld").build();
            user.unsetPermission(permission);
            userManager.saveUser(user);
        });
    }
}
