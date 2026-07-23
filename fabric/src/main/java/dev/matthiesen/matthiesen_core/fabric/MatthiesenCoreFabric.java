package dev.matthiesen.matthiesen_core.fabric;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

/**
 * The MatthiesenCoreFabric class serves as the main entry point for the Matthiesen Core mod when running on the Fabric mod loader.
 * It implements the ModInitializer interface, which allows it to perform initialization tasks during the mod loading process.
 */
public final class MatthiesenCoreFabric implements ModInitializer {
    /**
     * A volatile reference to the MinecraftServer instance, which is set when the server starts and cleared when the server stops.
     * This allows other parts of the mod to access the server instance safely across different threads.
     */
    public static volatile MinecraftServer SERVER_INSTANCE;

    /**
     * Called when the mod is initialized. This method performs the following tasks:
     * <p>
     * 1. Retrieves the singleton instance of MatthiesenCoreCommon.
     * </p>
     * <p>
     * 2. Logs an informational message indicating that the mod is loading for the Fabric Mod Loader.
     * </p>
     * <p>
     * 3. Calls the initialize method on the MatthiesenCoreCommon instance to perform common initialization tasks.
     * </p>
     * <p>
     * 4. Calls the onCommonServerSetup method on the MatthiesenCoreCommon instance to perform server-specific setup tasks.
     * </p>
     * <p>
     * 5. Registers event listeners for server starting and stopping events to manage the SERVER_INSTANCE reference.
     * </p>
     */
    @Override
    public void onInitialize() {
        var instance = MatthiesenCoreCommon.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();
        instance.onCommonServerSetup();

        ServerLifecycleEvents.SERVER_STARTING.register(server ->
                SERVER_INSTANCE = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server ->
                SERVER_INSTANCE = null);
    }
}
