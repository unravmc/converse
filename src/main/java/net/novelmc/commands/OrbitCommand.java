package net.novelmc.commands;

import net.novelmc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OrbitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("converse.orbit")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }
        if (args.length == 0) return false;
        CommandSender p = sender;
        Boolean orbitStatus;
        Player targetPlayer=Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }
        UUID targetPlayerUUID=targetPlayer.getUniqueId();
        if(args.length==2){
            Boolean newSetting;
            switch(args[1].toLowerCase()){
                case "on":
                    newSetting=true;
                    break;
                case "off":
                    newSetting=false;
                    break;
                default:
                    return false;
            }
            orbitStatus=Util.setOrbit(targetPlayerUUID,newSetting);
        }else orbitStatus=Util.toggleOrbit(targetPlayerUUID);
        p.sendMessage("§bSet orbit to " + (orbitStatus ? "on" : "off") + " for player "+args[0]);
        return true;
    }
}
