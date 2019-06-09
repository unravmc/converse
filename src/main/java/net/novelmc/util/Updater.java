package net.novelmc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import net.novelmc.Converse;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater
{
    private Plugin plugin;
    private Converse.BuildProperties build = Converse.build;
    private String oldHead = build.head;
    private String path = this.getFilePath();

    public Updater(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public void update()
    {
        try
        {
            String versionLink = "https://www.telesphoreo.me/converse/version.txt";
            URL url = new URL(versionLink);
            URLConnection con = url.openConnection();
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            if (!reader.ready())
            {
                return;
            }

            String newHead = reader.readLine();
            reader.close();

            if (oldHead.equals("${git.commit.id.abbrev}") || oldHead.equals("unknown"))
            {
                plugin.getLogger().info("No Git head detected, not updating Converse.");
                return;
            }

            if (plugin.getDescription().getVersion().endsWith("Beta"))
            {
                plugin.getLogger().info("You are running a beta release, not checking for updates.");
                return;
            }

            if (newHead.equals(oldHead))
            {
                plugin.getLogger().info("There are no updates available.");
                return;
            }

            String dlLink = "https://telesphoreo.me/converse/Converse.jar";
            url = new URL(dlLink);
            con = url.openConnection();
            InputStream in = con.getInputStream();
            FileOutputStream out = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, size);
            }

            out.close();
            in.close();
            plugin.getLogger().info("An update was successfully applied.");
        }
        catch (IOException ex)
        {
            plugin.getLogger().info("There was an issue fetching the server for an update.");
        }
    }

    private String getFilePath()
    {
        if (plugin instanceof JavaPlugin)
        {
            try
            {
                Method method = JavaPlugin.class.getDeclaredMethod("getFile");
                boolean wasAccessible = method.isAccessible();
                method.setAccessible(true);
                File file = (File)method.invoke(plugin);
                method.setAccessible(wasAccessible);

                return file.getPath();
            }
            catch (Exception e)
            {
                return "plugins" + File.separator + plugin.getName();
            }
        }
        else
        {
            return "plugins" + File.separator + "Converse.jar";
        }
    }
}