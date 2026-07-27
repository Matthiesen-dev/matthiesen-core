package dev.matthiesen.matthiesen_core.fabric;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.fabric.events.PlatformEventsBusListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

/**
 * The MatthiesenCoreFabric class serves as the main entry point for the Matthiesen Core mod when running on the Fabric mod loader.
 * It implements the ModInitializer interface, which allows it to perform initialization tasks during the mod loading process.
 */
public final class MatthiesenCoreFabric implements ModInitializer {

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

        PlatformEventsBusListener.initialize();
    }
}
