package dev.matthiesen.matthiesen_core.fabric;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.fabric.events.PlatformClientEventsBusListener;
import net.fabricmc.api.ClientModInitializer;

/**
 * The MatthiesenCoreFabricClient class serves as the main entry point for the Matthiesen Core mod when running on the Fabric mod
 * loader in a client environment. It implements the ClientModInitializer interface, which allows it to perform client-specific
 * initialization tasks during the mod loading process.
 */
public final class MatthiesenCoreFabricClient implements ClientModInitializer {
    /**
     * A static reference to the MatthiesenCoreCommonClient instance, which provides common client-side functionalities for the mod.
     */
    public static final MatthiesenCoreCommonClient INSTANCE = MatthiesenCoreCommonClient.INSTANCE;

    /**
     * Constructs a new instance of MatthiesenCoreFabricClient.
     * This class is not intended to be instantiated, as it serves as the main entry point for the mod in a client environment.
     */
    public MatthiesenCoreFabricClient() {
    }

    /**
     * Invoked during the client-side initialization phase of the mod loading process. This method initializes the Matthiesen Core client-side components,
     * logs the loading process, and performs any necessary client setup tasks.
     */
    @Override
    public void onInitializeClient() {
        INSTANCE.createInfoLog("Loading for Fabric Mod Loader (Client)");
        INSTANCE.initialize();

        PlatformClientEventsBusListener.initialize();
    }
}
