package net.novelmc.bans;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.novelmc.playerdata.InvalidPlayerDataException;
import net.novelmc.playerdata.PlayerData;
import net.novelmc.util.ConverseBase;
import net.novelmc.util.Util;
import org.bukkit.ChatColor;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanManager extends ConverseBase {
    File masterFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    public BanManager() {
        masterFile = new File(plugin.getDataFolder(), "bans.json");
        createMasterIfNecessary();
    }

    public void createMasterIfNecessary() {
        try {
            if (!masterFile.exists()) masterFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BanData getBanFromID(String banID) {
        Map<String, UUID> bans = getBanIDMap();
        if (bans.containsKey(banID.toLowerCase())) {
            UUID pUUID = bans.get(banID.toLowerCase());
            if (plugin.playerDataManager.doesPlayerDataExist(pUUID)) {
                PlayerData pData = plugin.playerDataManager.getPlayerData(pUUID);
                return pData.getBans().stream().filter(b -> b.getBanID().equalsIgnoreCase(banID)).findFirst().orElse(null);
            } else return null;
        } else return null;
    }

    private Map<String, UUID> getBanIDMap() {
        try {
            FileReader reader = new FileReader(masterFile);
            Type type = new TypeToken<Map<String, UUID>>() {}.getType();
            Map<String, UUID> map = gson.fromJson(reader, type);
            reader.close();

            if (map != null) return map;
            return new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    public void addBan(BanData ban) {
        try {
            String id = ban.getBanID();
            Map<String, UUID> map = getBanIDMap();
            map.put(id.toLowerCase(), ban.getPlayerUUID());
            FileWriter writer = new FileWriter(masterFile);
            gson.toJson(map, writer);
            writer.close();

            PlayerData pData = plugin.playerDataManager.getPlayerData(ban.getPlayerUUID());
            pData.getBans().add(0, ban);
            plugin.playerDataManager.save(pData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeBanFromHistory(BanData ban) {
        if (plugin.playerDataManager.doesPlayerDataExist(ban.getPlayerUUID())) {
            PlayerData pData = plugin.playerDataManager.getPlayerData(ban.getPlayerUUID());
            pData.getBans().removeIf(banData -> banData.getBanID().equalsIgnoreCase(ban.getBanID()));
            plugin.playerDataManager.save(pData);
            Map<String, UUID> map = getBanIDMap();
            map.remove(ban.getBanID().toLowerCase());
            try {
                FileWriter writer = new FileWriter(masterFile);
                gson.toJson(map, writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new InvalidPlayerDataException();
        }
    }

    public String getBanMessage(BanData ban) {
        final StringBuilder banMessage = new StringBuilder(ChatColor.GRAY + "Unraveled");
        banMessage.append(ChatColor.DARK_GRAY).append("MC\n");
        banMessage.append(ChatColor.RED).append("You are ").append(ban.getBanType() == BanType.PERMANENT ? "permanently" : "temporarily").append(" banned from this server!\n");
        if (ban.getReason() != null) {
            banMessage.append(ChatColor.GRAY).append("Reason: ").append(ChatColor.WHITE).append(ban.getReason())
                    .append("\n");
        }
        if (ban.getBanType() == BanType.TEMPORARY) {
            banMessage.append(ChatColor.GRAY).append("Until: ").append(ChatColor.WHITE).append(formatDate(ban.getBanExpiration()))
                    .append("\n");
        }
        banMessage.append(ChatColor.GRAY).append("More Info: ").append(ChatColor.GOLD).append(ChatColor.UNDERLINE)
                .append(plugin.config.getString("banurl")).append("\n");
        banMessage.append(ChatColor.GRAY).append("Ban ID: ").append(ChatColor.WHITE).append("#").append(ban.getBanID());
        return banMessage.toString();
    }

    public boolean isPlayerBanned(UUID u) {
        try {
            if (plugin.playerDataManager.doesPlayerDataExist(u)) {
                PlayerData pData = plugin.playerDataManager.getPlayerData(u);
                BanData latest = getLatestBan(u);
                if (latest == null) return false;
                if (latest.getBanType() == BanType.PERMANENT) return true;
                return latest.getBanExpiration().after(new Date());
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    public BanData getLatestBan(UUID u) {
        if (plugin.playerDataManager.doesPlayerDataExist(u)) {
            PlayerData pData = plugin.playerDataManager.getPlayerData(u);
            if (pData.getBans().isEmpty()) return null;
            return pData.getBans().get(0);
        }

        return null;
    }

    public String formatDate(Date date) {
        return simpleDateFormat.format(Util.getUnixDate(Util.getUnixTime(date)));
    }

}