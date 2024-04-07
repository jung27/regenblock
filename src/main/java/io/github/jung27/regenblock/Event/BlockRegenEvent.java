package io.github.jung27.regenblock.Event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockRegenEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
private final Block block;
    private final Material material;
    private boolean isCancelled;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public BlockRegenEvent(Block block, Material material) {
        this.block = block;
        this.material = material;
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Block getBlock() {
        return this.block;
    }

    public Material getMaterial() {
        return this.material;
    }
}
