package net.novelmc.commands;

import net.novelmc.util.ConverseBase;
import net.novelmc.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnloadChunksCommand extends ConverseBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        int numChunks = 0;

        for (World world : server.getWorlds())
        {
            numChunks += unloadUnusedChunks(world);
        }

        sender.sendMessage(ChatColor.GRAY + "" + numChunks + " chunks unloaded.");
        Util.action(sender.getName(), "Unloading all unused chunks");
        return true;
    }

    private int unloadUnusedChunks(World world)
    {
        int numChunks = 0;

        for (Chunk chunk : world.getLoadedChunks())
        {
            chunk.unload();
            numChunks++;
        }

        return numChunks;
    }
}
