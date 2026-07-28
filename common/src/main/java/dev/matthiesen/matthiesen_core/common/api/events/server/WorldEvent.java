package dev.matthiesen.matthiesen_core.common.api.events.server;

import net.minecraft.world.level.Level;

/**
 * This class contains events that are fired on the server side of the game.
 * These events are related to the world and its ticking process.
 */
public final class WorldEvent {

    private WorldEvent() {}

    /**
     * This event is fired at the start of a world tick.
     * @param level The world that is ticking.
     */
    public record StartTick(Level level) {}

    /**
     * This event is fired at the end of a world tick.
     * @param level The world that is ticking.
     */
    public record EndTick(Level level) {}
}
