package me.tks.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a new warp is about to be created.
 */
public class PWarpCreateEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private final String warpName;
    private final Player player;
    private final Location location;

    public PWarpCreateEvent(String name, Player player, Location location) {
        this.warpName = name;
        this.player = player;
        this.location = location;
    }

    /**
     * Gets name of the new warp.
     * @return New warp name.
     */
    public String getWarpName() {
        return warpName;
    }

    /**
     * Gets player that is creating the warp.
     * @return Player that is creating the warp.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the new warp location.
     * @return Location at which the warp is created.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the handler list. This is required by the event system.
     * @return A list of HANDLERS.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
