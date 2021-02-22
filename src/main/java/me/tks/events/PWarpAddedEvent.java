package me.tks.events;

import me.tks.playerwarp.Warp;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a new warp has been added to the
 */
public class PWarpAddedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Warp warp;

    public PWarpAddedEvent(Warp warp) {
        this.warp = warp;
    }

    public Warp getWarp() {
        return warp;
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
