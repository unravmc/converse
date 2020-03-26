package net.novelmc.util;

import net.novelmc.Converse;
import net.novelmc.bridge.LuckPermsBridge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerOrganizer {
    //TabList Sorting Methods
    private final LuckPermsBridge LPB = Converse.plugin.lp;
    private Scoreboard sb;

    public PlayerOrganizer() {
        sb = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Team player = sb.registerNewTeam("07G");
        Team voter = sb.registerNewTeam("06V");
        Team arc = sb.registerNewTeam("05A");
        Team mod = sb.registerNewTeam("04M");
        Team srmod = sb.registerNewTeam("03S");
        Team dev = sb.registerNewTeam("02D");
        Team exec = sb.registerNewTeam("01E");

        Objects.requireNonNull(sb.getTeam("01E")).setPrefix("&8[&4E&8] &r");
        Objects.requireNonNull(sb.getTeam("02D")).setPrefix("&8[&5D&8] &r");
        Objects.requireNonNull(sb.getTeam("03S")).setPrefix("&8[&6S&8] &r");
        Objects.requireNonNull(sb.getTeam("04M")).setPrefix("&8[&2M&8] &r");
        Objects.requireNonNull(sb.getTeam("05A")).setPrefix("&8[&9A&8] &r");
        Objects.requireNonNull(sb.getTeam("06V")).setPrefix("&8[&3V&8] &r");
        Objects.requireNonNull(sb.getTeam("07G")).setPrefix("&r");
    }

    public void tabAdd(@NotNull Player p) {

        String team;
        String uuid = p.getUniqueId().toString().trim();
        //
        //
        if (LPB.isModerator(p.getUniqueId())) {
            team = "04M";
        } else if (LPB.isSeniorModerator(p.getUniqueId())) {
            team = "03S";
        } else if (LPB.isDeveloper(p.getUniqueId())) {
            team = "02D";
        } else if (LPB.isExecutive(p.getUniqueId())) {
            team = "01E";
        } else if (LPB.isArchitect(p.getUniqueId())) {
            team = "05A";
        } else if (LPB.isVoter(p.getUniqueId())) {
            team = "06V";
        } else {
            team = "07G";
        }
        //
        try {
            Objects.requireNonNull(sb.getTeam(team)).addEntry(uuid);
        } catch (NullPointerException ex) {
            //possible null pointer; just do nothing for the logs.
        }
        Bukkit.getOnlinePlayers().forEach(pl -> pl.setScoreboard(sb));
    }

    public void tabRemove(Player p) {
        String uuid = p.getUniqueId().toString().trim();

        if (sb.getEntries().contains(uuid)) {
            Team team = sb.getEntryTeam(uuid);
            assert team != null;
            team.removeEntry(uuid);
        }

    }
}