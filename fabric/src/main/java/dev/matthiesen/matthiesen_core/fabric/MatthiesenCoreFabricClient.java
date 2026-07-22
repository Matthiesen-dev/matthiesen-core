package dev.matthiesen.matthiesen_core.fabric;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommonClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * The MatthiesenCoreFabricClient class serves as the main entry point for the Matthiesen Core mod when running on the Fabric mod
 * loader in a client environment. It implements the ClientModInitializer interface, which allows it to perform client-specific
 * initialization tasks during the mod loading process.
 */
public final class MatthiesenCoreFabricClient implements ClientModInitializer {

    /**
     * Invoked during the client-side initialization phase of the mod loading process. This method initializes the Matthiesen Core client-side components,
     * logs the loading process, and performs any necessary client setup tasks.
     */
    @Override
    public void onInitializeClient() {
        var instance = MatthiesenCoreCommonClient.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader (Client)");
        instance.initialize();
        instance.onClientSetup();
    }
}
