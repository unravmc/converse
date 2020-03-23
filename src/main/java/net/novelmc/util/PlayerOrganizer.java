package net.novelmc.util;

import java.util.Iterator;
import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerOrganizer {
    //TabList Sorting Methods
    private final LuckPermsBridge LPB = Converse.plugin.lp;
    private Scoreboard sb;
    private Team mod;
    private Team srmod;
    private Team dev;
    private Team exec;
    private Team arc;
    private Team voter;
    private Team player;
    
    public PlayerOrganizer() {
        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        player = sb.registerNewTeam("07G");
        voter = sb.registerNewTeam("06V");
        arc = sb.registerNewTeam("05A");
        mod = sb.registerNewTeam("04M");
        srmod = sb.registerNewTeam("03S");
        dev = sb.registerNewTeam("02D");
        exec = sb.registerNewTeam("01E");
        
        sb.getTeam("01E").setPrefix("&8[&4E&8] &r");
        sb.getTeam("02D").setPrefix("&8[&5D&8] &r");
        sb.getTeam("03S").setPrefix("&8[&6S&8] &r");
        sb.getTeam("04M").setPrefix("&8[&2M&8] &r");
        sb.getTeam("05A").setPrefix("&8[&9A&8] &r");
        sb.getTeam("06V").setPrefix("&8[&3V&8] &r");
        sb.getTeam("07G").setPrefix("&r");
    }
    
    public void tabAdd(Player p) {

        String team;
        String uuid = p.getUniqueId().toString().trim();
        //
        if (p == null) {
            throw new NullPointerException();
        }
        //
        if (LPB.isModerator(p.getUniqueId())) {
            team = "04M";
        }
        else if (LPB.isSeniorModerator(p.getUniqueId())) {
            team = "03S";
        }
        else if (LPB.isDeveloper(p.getUniqueId())) {
            team = "02D";
        }
        else if (LPB.isExecutive(p.getUniqueId())) {
            team = "01E";
        }
        else if (LPB.isArchitect(p.getUniqueId())) {
            team = "05A";
        }
        else if (LPB.isVoter(p.getUniqueId())) {
            team = "06V";
        }
        else {
            team = "07G";
        }
        //
        try {
            sb.getTeam(team).addEntry(uuid);
        } catch (NullPointerException ex) {
            //possible null pointer; just do nothing for the logs.
        }
        Bukkit.getOnlinePlayers().forEach(pl -> {
            pl.setScoreboard(sb);
        });
    }
    
    public void tabRemove(Player p) {
        String uuid = p.getUniqueId().toString().trim();
        if (p == null) {
            throw new NullPointerException();
        }
        
        if (sb.getEntries().contains(uuid)) {
            Team team = sb.getEntryTeam(uuid);
            team.removeEntry(uuid);
        } else {
            //do nothing :)
        }
    }
}