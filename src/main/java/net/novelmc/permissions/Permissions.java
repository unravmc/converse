package net.novelmc.permissions;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class Permissions implements Listener
{
    static LuckPermsApi api = Converse.getLuckPermsAPI();

    public static CompletableFuture<Boolean> isModerator(UUID who) {
        return api.getUserManager().loadUser(who)
                .thenApplyAsync(user -> user.getPrimaryGroup().equalsIgnoreCase("mod"));
    }

    public static void informIfMod(CommandSender sender, UUID who) {
        isModerator(who).thenAcceptAsync(result -> {
            if (result) {
                sender.sendMessage("Yes! That player is a moderator!");
            } else {
                sender.sendMessage("No, that player isn't a moderator.");
            }
        });
    }
}
