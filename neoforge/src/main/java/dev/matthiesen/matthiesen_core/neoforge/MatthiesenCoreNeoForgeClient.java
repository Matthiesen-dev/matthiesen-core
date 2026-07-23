package dev.matthiesen.matthiesen_core.neoforge;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.neoforge.platform.NeoForgeLoaderClientEventsListeners;
import dev.matthiesen.matthiesen_core.neoforge.platform.helpers.NeoForgeClientRegistryHelper;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

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
    public static MatthiesenCoreCommonClient INSTANCE;

    /**
     * Constructs a new instance of the MatthiesenCoreNeoForgeClient class. This constructor is responsible for initializing the client-side
     * components of the Matthiesen Core mod when running on the NeoForge mod loader. It sets up the event bus and initializes the client-side registry helper.
     * @param modBus The event bus used for registering client-side events and components.
     */
    public MatthiesenCoreNeoForgeClient(IEventBus modBus) {
        INSTANCE = MatthiesenCoreCommonClient.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader (Client)");
        NeoForgeClientRegistryHelper.init(modBus);
        INSTANCE.initialize();
    }

    /**
     * This nested static class serves as an event subscriber for client-side events in the Matthiesen Core mod when running on the NeoForge mod loader.
     */
    @EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID, value = Dist.CLIENT)
    public static class ClientEventsSubscriber {

        /**
         * This method is called during the client setup phase of the mod loading process. It is responsible for performing
         * any necessary client-side initialization tasks, such as setting up renderers, key bindings, and other client-specific features.
         * @param event The FMLClientSetupEvent event that triggers this method.
         */
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void clientSetup(FMLClientSetupEvent event) {
            INSTANCE.onClientSetup();
        }

        /**
         * This method is called at the end of each client tick. It checks if the player instance is not null and then executes
         * all the Runnables that have been registered to run at the end of the client tick. This allows for scheduling tasks
         * that need to be performed after the main client tick processing is complete.
         * @param event The ClientTickEvent.Post event that triggers this method.
         */
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onClientEndTick(ClientTickEvent.Post event) {
            if (Minecraft.getInstance().player != null) {
                for (Runnable runnable : NeoForgeLoaderClientEventsListeners.END_CLIENT_TICK_RUNNABLES) {
                    runnable.run();
                }
            }
        }
    }
}
