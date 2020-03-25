package net.novelmc.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util extends ConverseBase {


    //honestly why tf is this here????
    public List<String> getOnlinePlayers() {
        List<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach((p) ->
        {
            players.add(p.getName());
        });
        return players;
    }

    public static void action(String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[Converse: " + message + "]");
    }

    public static void action(Player player, String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + player.getName() + ": " + message + "]");
    }

    public static void action(CommandSender sender, String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + sender.getName() + ": " + message + "]");
    }

    public static void action(String sender, String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + sender + ": " + message + "]");
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }


    public void adminchat(CommandSender sender, String message) {
        Player p = Bukkit.getPlayer(sender.getName());
        String rank = plugin.lp.displayRank(Bukkit.getPlayer(sender.getName()));
        ChatColor color = plugin.lp.displayRankColor(Bukkit.getPlayer(sender.getName()));

        String format = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "STAFF"
                + ChatColor.DARK_GRAY + "] "
                + plugin.lp.nameColor(p) + sender.getName() 
                + ChatColor.DARK_GRAY + " [" + color + rank + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + ": " + ChatColor.GOLD + message;
        Bukkit.getLogger().info(ChatColor.stripColor(format));
        Bukkit.getOnlinePlayers()
                .stream()
                .filter((players) -> (players.hasPermission("converse.adminchat")))
                .forEachOrdered((players) -> {
                        players.sendMessage(format);
                    });
    }
    
    public void adminchat(Player p, String message) {
        CommandSender sender = (CommandSender) p;
        adminchat(sender, message);
    }
    
    private HashMap<UUID, Boolean> adminChat = new HashMap<>();
 
    //creation of the adminchat toggleable variable;
    public void putAdminChat(UUID uuid) {
        if (adminChat.containsKey(uuid)) {
            boolean value = adminChat.get(uuid);
            if (value == true) {
                adminChat.replace(uuid, false);
            } else {
                adminChat.replace(uuid, true);
            }
        } else {
            adminChat.put(uuid, true);
        }
    }

    public Boolean isInAdminChat(UUID uuid) {
        if (!adminChat.containsKey(uuid)) {
            return false;
        }
        
        return adminChat.get(uuid);
    }
    
    // thank you tfm :pensive:
    public static Date parseDateOffset(String time) {
        Pattern timePattern = Pattern.compile(
                "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if (m.group() == null || m.group().isEmpty()) {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++) {
                if (m.group(i) != null && !m.group(i).isEmpty())
                {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (m.group(1) != null && !m.group(1).isEmpty()) {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty()) {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty()) {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty()) {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty()) {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty()) {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty()) {
                    seconds = Integer.parseInt(m.group(7));
                }
                break;
            }
        }
        if (!found) {
            return null;
        }

        Calendar c = new GregorianCalendar();

        if (years > 0) {
            c.add(Calendar.YEAR, years);
        }
        if (months > 0) {
            c.add(Calendar.MONTH, months);
        }
        if (weeks > 0) {
            c.add(Calendar.WEEK_OF_YEAR, weeks);
        }
        if (days > 0) {
            c.add(Calendar.DAY_OF_MONTH, days);
        }
        if (hours > 0) {
            c.add(Calendar.HOUR_OF_DAY, hours);
        }
        if (minutes > 0) {
            c.add(Calendar.MINUTE, minutes);
        }
        if (seconds > 0) {
            c.add(Calendar.SECOND, seconds);
        }

        return c.getTime();
    }

    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static Date getUnixDate(long unix) {
        return new Date(unix * 1000);
    }

    public static long getUnixTime(Date date) {
        if (date == null)
        {
            return 0;
        }

        return date.getTime() / 1000L;
    }
}
