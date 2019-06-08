package net.novelmc.bridge;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsBridge
{
    static LuckPermsApi api = Converse.getLuckPermsAPI();

    public static boolean isPlayerInGroup(Player player, String group)
    {
        api.buildNode("group.mod");
        return player.hasPermission("group." + group);
    }

    public static String getPlayerGroup(Player player, Collection<String> possibleGroups)
    {
        for (String group : possibleGroups)
        {
            if (player.hasPermission("group." + group))
            {
                return group;
            }
        }
        return null;
    }

    public static void set(UUID uuid, String group) {
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);

        userFuture.thenAcceptAsync(user -> {
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
