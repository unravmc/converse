package net.novelmc.world;

import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;

public class WorldGenInitEvent extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final WorldGenBase worldGen;

    public WorldGenInitEvent(WorldGenBase worldGen) {
        super(worldGen.getWorld());
        this.worldGen = worldGen;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public WorldGenBase getWorldGenerator() {
        return worldGen;
    }


}
