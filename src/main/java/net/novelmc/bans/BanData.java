package net.novelmc.bans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import net.novelmc.ConversePlugin;
import net.novelmc.util.Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanData {
    private UUID playerUUID;
    private BanType banType;
    private UUID staffUUID;
    private String reason;
    private Date banExpiration;
    private Date dateIssued;
    private String banID;

    public BanData() {
        this.banID = generateBanID();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public BanType getBanType() {
        return banType;
    }

    public void setBanType(BanType banType) {
        this.banType = banType;
    }

    public UUID getStaffUUID() {
        return staffUUID;
    }

    public void setStaffUUID(UUID staffUUID) {
        this.staffUUID = staffUUID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getBanExpiration() {
        return banExpiration;
    }

    public void setBanExpiration(Date banExpiration) {
        this.banExpiration = banExpiration;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getBanID() {
        return banID;
    }


    public String generateBanID() {
        return RandomStringUtils.randomAlphabetic(5);
    }
}
