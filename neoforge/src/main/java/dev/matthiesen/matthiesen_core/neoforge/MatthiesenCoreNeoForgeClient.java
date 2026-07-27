package dev.matthiesen.matthiesen_core.neoforge;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.neoforge.platform.helpers.NeoForgeClientRegistryHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * The MatthiesenCoreNeoForgeClient class serves as the main entry point for the Matthiesen Core mod when running on the NeoForge
 * mod loader in a client environment. It is responsible for initializing client-side components and handling client-specific events.
 */
@Mod(value = MatthiesenCoreCommon.MOD_ID, dist = Dist.CLIENT)
public final class MatthiesenCoreNeoForgeClient {
    /**
     * A static reference to the MatthiesenCoreCommonClient instance, which is initialized in the constructor. This allows other parts
     * of the mod to access the client-side instance of the Matthiesen Core mod when running on the NeoForge mod loader.
     */
    public static MatthiesenCoreCommonClient INSTANCE = MatthiesenCoreCommonClient.INSTANCE;

    /**
     * Constructs a new instance of the MatthiesenCoreNeoForgeClient class. This constructor is responsible for initializing the client-side
     * components of the Matthiesen Core mod when running on the NeoForge mod loader. It sets up the event bus and initializes the client-side registry helper.
     * @param modBus The event bus used for registering client-side events and components.
     */
    public MatthiesenCoreNeoForgeClient(IEventBus modBus) {
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader (Client)");
        NeoForgeClientRegistryHelper.init(modBus);
        INSTANCE.initialize();
    }
}
