package net.novelmc.banning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import net.novelmc.Converse;
import net.novelmc.util.Ips;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ban
{
    public static Converse plugin = Converse.plugin;

    public void initiateBan(Player player, CommandSender sender)
    {
        try
        {
            FileWriter stream = new FileWriter(Converse.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            Converse.bans.set(Ips.getIp(player).replace(".", "-"), Ips.getIp(player).replace(".", "-"));
            Converse.bans.set(Ips.getIp(player).replace(".", "-") + ".username", player.getName());
            Converse.bans.set(Ips.getIp(player).replace(".", "-") + ".banned-by", sender.getName());
            out.newLine();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void initiateBanIP(String ip, CommandSender sender, String user)
    {
        try
        {
            FileWriter stream = new FileWriter(Converse.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            Converse.bans.set(ip, ip);
            Converse.bans.set(ip + ".username", user);
            Converse.bans.set(ip + ".banned-by", sender.getName());
            out.newLine();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void initiateBanIP(String ip, CommandSender sender, String user, String reason)
    {
        try
        {
            FileWriter stream = new FileWriter(Converse.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            Converse.bans.set(ip, ip);
            Converse.bans.set(ip + ".username", user);
            Converse.bans.set(ip + ".banned-by", sender.getName());
            Converse.bans.set(ip + ".reason", reason);
            out.newLine();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void initiateOfflineUserBan(String ip, CommandSender sender, String args)
    {
        try
        {
            FileWriter stream = new FileWriter(Converse.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            Converse.bans.set(ip, ip);
            Converse.bans.set(ip + ".username", args);
            Converse.bans.set(ip + ".banned-by", sender.getName());
            out.newLine();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void initiateOfflineUserBan(String ip, CommandSender sender, String args, String reason)
    {
        try
        {
            FileWriter stream = new FileWriter(Converse.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            Converse.bans.set(ip, ip);
            Converse.bans.set(ip + ".username", args);
            Converse.bans.set(ip + ".banned-by", sender.getName());
            Converse.bans.set(ip + ".reason", reason);
            out.newLine();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            Converse.bans.save("admins.yml");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void initiateBan(Player player, CommandSender sender, String reason)
    {
        try
        {
            FileWriter stream = new FileWriter(Converse.bans.toString());
            BufferedWriter out = new BufferedWriter(stream);
            Converse.bans.set(Ips.getIp(player).replace(".", "-"), Ips.getIp(player).replace(".", "-"));
            Converse.bans.set(Ips.getIp(player).replace(".", "-") + ".username", player.getName());
            Converse.bans.set(Ips.getIp(player).replace(".", "-") + ".banned-by", sender.getName());
            Converse.bans.set(Ips.getIp(player).replace(".", "-") + ".reason", reason);
            out.newLine();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void initiateUnban(String args)
    {
        Converse.bans.set(args, null);
    }
}
