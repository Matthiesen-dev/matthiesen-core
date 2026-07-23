package dev.matthiesen.matthiesen_core.common.api.events;

import net.minecraft.server.MinecraftServer;

/**
 * Typed event records for server lifecycle events used with {@link PlatformEvents}.
 *
 * <p>Each nested record represents a distinct phase of the server lifecycle. Subscribe to the
 * corresponding {@link PlatformEvents} field to receive notifications for that phase.</p>
 *
 * @see PlatformEvents#SERVER_STARTING
 * @see PlatformEvents#SERVER_STARTED
 * @see PlatformEvents#SERVER_STOPPING
 * @see PlatformEvents#SERVER_STOPPED
 * @see PlatformEvents#SERVER_START_TICK
 * @see PlatformEvents#SERVER_END_TICK
 * @see PlatformEvents#SERVER_RELOAD
 */
@SuppressWarnings("unused")
public final class ServerEvent {

    private ServerEvent() {}

    /**
     * Fired when the server begins its startup sequence. World data has not yet been loaded
     * and players cannot connect.
     *
     * @param server the starting {@link MinecraftServer} instance
     */
    public record Starting(MinecraftServer server) {}

    /**
     * Fired when the server has fully started and is ready to accept player connections.
     *
     * @param server the running {@link MinecraftServer} instance
     */
    public record Started(MinecraftServer server) {}

    /**
     * Fired when the server begins its shutdown sequence. Players are still connected at this point.
     *
     * @param server the stopping {@link MinecraftServer} instance
     */
    public record Stopping(MinecraftServer server) {}

    /**
     * Fired after the server has fully stopped and all worlds have been saved.
     *
     * @param server the stopped {@link MinecraftServer} instance
     */
    public record Stopped(MinecraftServer server) {}

    /**
     * Fired at the beginning of each server tick, before game logic is processed.
     *
     * @param server the ticking {@link MinecraftServer} instance
     */
    public record StartTick(MinecraftServer server) {}

    /**
     * Fired at the end of each server tick, after all game logic has been processed.
     *
     * @param server the ticking {@link MinecraftServer} instance
     */
    public record EndTick(MinecraftServer server) {}

    /**
     * Fired when the server completes a data pack reload.
     *
     * <p>No server instance is provided because the reload pipeline does not expose the server
     * uniformly across all platforms. If you need the server reference, cache it from
     * {@link PlatformEvents#SERVER_STARTED}.</p>
     */
    public record Reload() {}
}

