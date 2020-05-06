package net.novelmc.util.nbt;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.ByteBuf;
import net.novelmc.ConversePlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class PlibListener extends PacketAdapter {
    private static PacketType a = PacketType.Play.Client.SET_CREATIVE_SLOT;
    private static PacketType b = PacketType.Play.Client.CUSTOM_PAYLOAD;
    public static Cache<Player, Object> c;

    public PlibListener(Plugin plugin) {
        super(plugin,
                ListenerPriority.HIGHEST,
                PacketType.Play.Client.SET_CREATIVE_SLOT,
                PacketType.Play.Client.CUSTOM_PAYLOAD);
        c = CacheBuilder.newBuilder()
                .concurrencyLevel(2)
                .initialCapacity(20)
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build();
    }

    /**
     * The only thing I won't bother obfuscating :)
     * @param event The event to parse.
     */
    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.isCancelled()) return;
        Player player = MiniFactory.getPacketPlayer(event);
        if (player == null) return;
        if (this.c(player)) {
            event.setCancelled(true);
            return;
        }
        if (event.getPacketType() == a && player.getGameMode() == GameMode.CREATIVE) {
            this.e(event, player);
        } else if (event.getPacketType() == b && !d() && !player.hasPermission("converse.bypass.packet")) {
            this.f(event, player);
        }
    }

    public boolean c(Player p) {
        return c.getIfPresent(p) != null;
    }

    public static boolean d() {
        String d = dd();
        return !d.startsWith("1_15_R") || !d.startsWith("1_14_R");
    }

    public static String dd() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    private void e(PacketEvent e, Player p) {
        ItemStack s = e.getPacket().getItemModifier().readSafely(0);
        if (ConversePlugin.fixer.isHackedItem(s, p)) {
            c.put(p, new Object());
            p.updateInventory();
        }
    }

    private void f(PacketEvent e, Player p) {
        String ch = e.getPacket().getStrings().readSafely(0);
        if (("MC|BEdit".equals(ch) || "MC|BSign".equals(ch))) {
            c.put(p, new Object());
        } else if ("REGISTER".equals(ch)) {
            g(e, p);
        }
    }

    private void g(PacketEvent e, Player p) {
        int cs = p.getListeningPluginChannels().size();
        final PacketContainer pc = e.getPacket();
        final ByteBuf bb = (pc.getSpecificModifier(ByteBuf.class).read(0)).copy();
        final String[] ch = bb.toString(Charsets.UTF_8).split("\0");
        for (int i = 0; i < ch.length; i++) {
            if (++cs > 120) {
                e.setCancelled(true);
                Bukkit.getScheduler().runTask(ConversePlugin.plugin, ()->p.kickPlayer("Too many channels registered. (max: 120)"));
                break;
            }
        }
        bb.release();
    }

}
