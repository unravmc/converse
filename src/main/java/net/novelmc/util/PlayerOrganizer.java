package net.novelmc.util;

import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerOrganizer {
    //TabList Sorting Methods
    private final LuckPermsBridge LPB = Converse.plugin.lp;
    private Scoreboard sb;
    Team op;
    Team voter;
    Team arc;
    Team mod;
    Team srmod;
    Team dev;
    Team exec;

    public PlayerOrganizer() {
        sb = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
        sb.getTeams().forEach(Team::unregister);
        op = sb.registerNewTeam("G-OP");
        voter = sb.registerNewTeam("F-Voter");
        arc = sb.registerNewTeam("E-Arch");
        mod = sb.registerNewTeam("D-Mod");
        srmod = sb.registerNewTeam("C-SMod");
        dev = sb.registerNewTeam("B-Dev");
        exec = sb.registerNewTeam("A-Exec");

        exec.setPrefix(colorize("&8[&4E&8] &r"));
        dev.setPrefix(colorize("&8[&5D&8] &r"));
        srmod.setPrefix(colorize("&8[&6S&8] &r"));
        mod.setPrefix(colorize("&8[&2M&8] &r"));
        arc.setPrefix(colorize("&8[&9A&8] &r"));
        voter.setPrefix(colorize("&8[&3V&8] &r"));
        op.setPrefix(colorize("&r"));
    }

    public void tabAdd(@NotNull Player p) {

        String pName = p.getName();
        //
        //
        if (LPB.isModerator(p.getUniqueId())) {
            mod.addEntry(pName);
        } else if (LPB.isSeniorModerator(p.getUniqueId())) {
            srmod.addEntry(pName);
        } else if (LPB.isDeveloper(p.getUniqueId())) {
            dev.addEntry(pName);
        } else if (LPB.isExecutive(p.getUniqueId())) {
            exec.addEntry(pName);
        } else if (LPB.isArchitect(p.getUniqueId())) {
            arc.addEntry(pName);
        } else if (LPB.isVoter(p.getUniqueId())) {
            voter.addEntry(pName);
        } else {
            op.addEntry(pName);
        }
    }

    public void tabRemove(Player p) {
        String uuid = p.getUniqueId().toString().trim();

        if (sb.getEntries().contains(uuid)) {
            Team team = sb.getEntryTeam(uuid);
            assert team != null;
            team.removeEntry(uuid);
        }
    }

    private String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}