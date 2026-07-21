package dev.matthiesen.matthiesen_core.common.api.events;

import net.minecraft.server.MinecraftServer;

/**
 * Interface for listening to server events. Implementations of this interface can be registered to receive notifications about
 * server lifecycle events, such as starting, stopping, and ticking. This allows for custom behavior to be executed at specific
 * points in the server's lifecycle.
 */
@SuppressWarnings("unused")
public interface ServerEventListener {
    /**
     * Called when the server is starting. This method can be used to perform initialization or setup tasks before the server fully starts.
     * @param server The MinecraftServer instance that is starting. This provides access to server-level information and functionality during the startup process.
     */
    default void onServerStarting(MinecraftServer server) {}

    /**
     * Called when the server is stopping. This method can be used to perform cleanup or save operations before the server shuts down.
     * @param server The MinecraftServer instance that is stopping. This provides access to server-level information and functionality during the shutdown process.
     */
    default void onServerStopping(MinecraftServer server) {}

    /**
     * Called when the server has completed its reload process. This method can be used to perform actions that need to occur after the server has reloaded its data and configurations.
     */
    default void onServerReload() {}

    /**
     * Called at the start of each server tick. This method can be used to perform actions that need to occur before any other server tick processing.
     * @param server The MinecraftServer instance that is ticking. This provides access to server-level information and functionality during the tick event.
     */
    default void onServerStartTick(MinecraftServer server) {}

    /**
     * Called at the end of each server tick. This method can be used to perform actions that need to occur after all other server tick processing has completed.
     * @param server The MinecraftServer instance that is ticking. This provides access to server-level information and functionality during the tick event.
     */
    default void onServerEndTick(MinecraftServer server) {}
}
