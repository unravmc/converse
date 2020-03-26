package net.novelmc.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Util extends ConverseBase {

    private static String time;
    private static long unix;
    private static Date date;
    private UUID uuid;


    //honestly why tf is this here????
    public List<String> getOnlinePlayers() {
        List<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach((p) ->
                players.add(p.getName()));
        return players;
    }

    public static void action(String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[Converse: " + message + "]");
    }

    public static void action(@NotNull Player player, String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + player.getName() + ": " + message + "]");
    }

    public static void action(@NotNull CommandSender sender, String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + sender.getName() + ": " + message + "]");
    }

    public static void action(String sender, String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + sender + ": " + message + "]");
    }

    @NotNull
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }


    public void adminchat(@NotNull CommandSender sender, String message) {
        Player p = Bukkit.getPlayer(sender.getName());
        String rank = plugin.lp.displayRank(Bukkit.getPlayer(sender.getName()));
        ChatColor color = plugin.lp.displayRankColor(Bukkit.getPlayer(sender.getName()));

        String format = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "STAFF"
                + ChatColor.DARK_GRAY + "] "
                + plugin.lp.nameColor(p) + sender.getName()
                + ChatColor.DARK_GRAY + " [" + color + rank + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + ": " +
                ChatColor.GOLD + message;
        Bukkit.getLogger().info(ChatColor.stripColor(format));
        Bukkit.getOnlinePlayers()
                .stream()
                .filter((players) -> (players.hasPermission("converse.adminchat")))
                .forEachOrdered((players) -> players.sendMessage(format));
    }

    public void adminchat(Player p, String message) {
        adminchat((CommandSender) p, message);
    }

    private HashMap<UUID, Boolean> adminChat = new HashMap<>();

    //creation of the adminchat toggleable variable;
    public void putAdminChat(UUID uuid) {
        this.uuid = uuid;
        if (adminChat.containsKey(uuid)) {
            boolean value = adminChat.get(uuid);
            if (value) {
                adminChat.replace(uuid, false);
            } else {
                adminChat.replace(uuid, true);
            }
        } else {
            adminChat.put(uuid, true);
        }
    }

    public Boolean isInAdminChat(UUID uuid) {
        this.uuid = uuid;
        if (!adminChat.containsKey(uuid)) {
            return false;
        }

        return adminChat.get(uuid);
    }

    // thank you tfm :pensive:
    @Nullable
    public static Date parseDateOffset(String time) {
        Util.time = time;
        @Nullable Date result = null;
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
                if (m.group(i) != null && !m.group(i).isEmpty()) {
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
        if (found) {
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
            result = c.getTime();
        }

        return result;
    }

    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static Date getUnixDate(long unix) {
        Util.unix = unix;
        return new Date(unix * 1000);
    }

    @Contract(pure = true)
    public static long getUnixTime(Date date) {
        long result = 0;
        Util.date = date;
        if (date != null) {
            result = date.getTime() / 1000L;
        }

        return result;
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        Util.time = time;
    }

    public static long getUnix() {
        return unix;
    }

    public static void setUnix(long unix) {
        Util.unix = unix;
    }

    public static Date getDate() {
        return date;
    }

    public static void setDate(Date date) {
        Util.date = date;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
