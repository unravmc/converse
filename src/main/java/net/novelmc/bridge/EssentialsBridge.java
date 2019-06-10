package net.novelmc.bridge;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class EssentialsBridge
{
    private static Essentials essentialsAPI = null;

    public static Essentials getPlugin()
    {
        if (essentialsAPI == null)
        {
            try
            {
                final Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");
                if (essentials != null && essentials instanceof Essentials)
                {
                    essentialsAPI = (Essentials)essentials;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return essentialsAPI;
    }

    public static User getUser(String username)
    {
        try
        {
            Essentials essentials = getPlugin();
            if (essentials != null)
            {
                return essentials.getUserMap().getUser(username);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
