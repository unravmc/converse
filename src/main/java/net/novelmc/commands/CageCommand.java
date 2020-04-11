package net.novelmc.commands;

import net.novelmc.util.ConverseBase;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CageCommand extends ConverseBase implements CommandExecutor {
    public class Cage {
        private UUID uuid;
        private Material cageMaterial;
        public final Map<Location, Material> previousBlocks = new HashMap<>();

        public Cage(Player player, Material cageMaterial) {
            this.uuid = player.getUniqueId();
            this.cageMaterial = cageMaterial;
        }

        public void createCage() {
            final Block center = Objects.requireNonNull(Bukkit.getPlayer(uuid)).getLocation().getBlock();
            for (int xOffset = -2; xOffset <= 2; xOffset++) {
                for (int yOffset = -2; yOffset <= 2; yOffset++) {
                    for (int zOffset = -2; zOffset <= 2; zOffset++) {
                        if (Math.abs(xOffset) != 2 && Math.abs(yOffset) != 2 && Math.abs(zOffset) != 2) {
                            final Block block = center.getRelative(xOffset, yOffset, zOffset);
                            previousBlocks.put(block.getLocation(), block.getType());
                            block.setType(Material.AIR);
                        } else {
                            final Block block = center.getRelative(xOffset, yOffset, zOffset);
                            previousBlocks.put(block.getLocation(), block.getType());
                            block.setType(cageMaterial);
                        }
                    }
                }
            }

        }

        public void undo() {
            for (Location loc : previousBlocks.keySet()) {
                loc.getBlock().setType(previousBlocks.get(loc));
            }
        }
    }

    // Usage: /cage <player> [block]
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("converse.cage")) {
            if (args.length == 0) return false;
            else {
                if (!args[0].equalsIgnoreCase("purge")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (!plugin.cgl.cages.containsKey(target.getUniqueId())) {
                            if (!target.hasPermission("converse.cage")) {
                                Material cageMaterial = Material.GLASS;
                                if (args.length > 1) {
                                    Material matchedMaterial = Material.matchMaterial(args[1]);
                                    ;
                                    if (matchedMaterial != null) cageMaterial = matchedMaterial;
                                }

                                Cage cage = new Cage(target, cageMaterial);
                                cage.createCage();

                                plugin.cgl.cages.put(target.getUniqueId(), cage);
                                Util.action(sender, "Caging " + target.getName());
                            } else {
                                sender.sendMessage(ChatColor.RED + "You can't cage this person!");
                            }
                        } else {
                            plugin.cgl.cages.get(target.getUniqueId()).undo();
                            plugin.cgl.cages.remove(target.getUniqueId());
                            Util.action(sender, "Uncaged " + target.getName());
                        }
                    } else {
                        sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    }
                } else {
                    Util.action(sender, "Uncaging all players");
                    for (UUID u : plugin.cgl.cages.keySet()) {
                        Cage cage = plugin.cgl.cages.get(u);
                        cage.undo();
                    }
                    plugin.cgl.cages.clear();
                }
            }
        } else {
            sender.sendMessage(Messages.NO_PERMISSION);
        }
        return true;
    }
}
