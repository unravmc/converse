package net.novelmc.util;

import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.Converse;
import org.bukkit.Server;

public class ConverseBase
{
    protected Converse plugin = Converse.plugin;
    protected Server server = Converse.server;
    protected LuckPermsApi api = Converse.getLuckPermsAPI();
}
