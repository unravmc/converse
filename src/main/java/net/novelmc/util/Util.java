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
    private static UUID uuid;


    /**
     * @return An accessible array of all players online;
     * Why do this when you can just use the Collection class?
     */
    public List<String> getOnlinePlayers() {
        List<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach((p) ->
                players.add(p.getName()));
        return players;
    }

    /**
     * @return Inventory size, for PlayersMenu;
     */
    public static int size() {
        int key = Bukkit.getOnlinePlayers().size();
        int value;
        if (key < 9) {
            value = 9;
            return value;
        }
        if (key > 54) {
            value = 54;
            return value;
        }
        //Check to make sure key is a multiple of nine.
        List<Integer> list = Arrays.asList(9, 18, 27, 36, 45, 54);
        if (list.contains(key)) {
            for (int fuzzy : list) {
                if (fuzzy < key) {
                    continue;
                }
                if (fuzzy > key) {
                    value = fuzzy;
                    return value;
                }
                break;
            }
        }

        value = key;
        return value;
    }

    /**
     * @param message Message to broadcast to the server.
     */
    public static void action(String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[Converse: " + message + "]");
    }

    /**
     * @param player  The player who sent the action.
     * @param message The message for the action.
     */
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

    public static void adminchat(@NotNull CommandSender sender, String message) {
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

    public static void adminchat(Player p, String message) {
        adminchat((CommandSender) p, message);
    }

    private static HashMap<UUID, Boolean> adminChat = new HashMap<>();

    //creation of the adminchat toggleable variable;
    public static void putAdminChat(UUID uuid) {
        Util.uuid = uuid;
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

    public static void removeAdminChat(UUID uuid) {
        Util.uuid = uuid;
        adminChat.remove(uuid);
    }

    public static Boolean isInAdminChat(UUID uuid) {
        Util.uuid = uuid;
        if (!adminChat.containsKey(uuid)) {
            return false;
        }

        return adminChat.get(uuid);
    }

    //orbit
    private static ArrayList<UUID> orbit = new ArrayList<>();

    public static Boolean toggleOrbit(UUID uuid){
        if(isInOrbit(uuid)){
            orbit.remove(uuid);
            return false;
        }
        orbit.add(uuid);
        return true;
    }

    public static Boolean setOrbit(UUID uuid, Boolean orbitStatus){
        Boolean isOrbiting=isInOrbit(uuid);
        if(orbitStatus==isOrbiting)return orbitStatus;
        return toggleOrbit(uuid);
    }

    public static Boolean isInOrbit(UUID uuid){
        return orbit.contains(uuid);
    }

    // Rewrote this a bit to be a little more functional.
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
        Util.uuid = uuid;
    }
}
