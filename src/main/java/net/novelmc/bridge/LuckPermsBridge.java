package net.novelmc.bridge;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import net.novelmc.Converse;
import net.novelmc.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LuckPermsBridge
{
    private final Converse plugin;
    private final LuckPermsApi api = Converse.getLuckPermsAPI();

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
            return ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "CON";
        }

        if (isModerator(player.getUniqueId()))
        {
            return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "M";
        }
        else if (isSeniorModerator(player.getUniqueId()))
        {
            return ChatColor.GOLD + "" + ChatColor.BOLD + "SRM";
        }
        else if (isDeveloper(player.getUniqueId()))
        {
            return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "D";
        }
        else if (isExecutive(player.getUniqueId()))
        {
            return ChatColor.RED + "" + ChatColor.BOLD + "E";
        }
        else if (isArchitect(player.getUniqueId()))
        {
            return ChatColor.BLUE + "" + ChatColor.BOLD + "A";
        }

        return ChatColor.WHITE + "" + ChatColor.BOLD + "MB";
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
        else if (isSeniorModerator(player.getUniqueId()))
        {
            return ChatColor.GOLD;
        }
        else if (isDeveloper(player.getUniqueId()))
        {
            return ChatColor.DARK_PURPLE;
        }
        else if (isExecutive(player.getUniqueId()))
        {
            return ChatColor.RED;
        }
        else if (isArchitect(player.getUniqueId()))
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
    //

    public void setPrefix(UUID uuid, String permission)
    {
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);

        userFuture.thenAcceptAsync(user ->
        {
            Date expires = Util.parseDateOffset("1m");
            long longExpires = Util.getUnixTime(expires);
            Node prefix = api.buildNode(permission).setExpiry(longExpires).build();
            user.setPermission(prefix);
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
            Node command = api.buildNode("converse.staffworld").build();
            user.setPermission(permission);
            user.setPermission(command);
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
            Node command = api.buildNode("converse.staffworld").build();
            user.unsetPermission(permission);
            user.unsetPermission(command);
            userManager.saveUser(user);
        });
    }
}
