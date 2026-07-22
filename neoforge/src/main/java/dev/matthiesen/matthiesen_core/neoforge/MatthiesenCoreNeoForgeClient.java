package dev.matthiesen.matthiesen_core.neoforge;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommonClient;
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

@Mod(value = MatthiesenCoreCommon.MOD_ID, dist = Dist.CLIENT)
public final class MatthiesenCoreNeoForgeClient {
    public static MatthiesenCoreCommonClient INSTANCE;

    public MatthiesenCoreNeoForgeClient(IEventBus modBus) {
        INSTANCE = MatthiesenCoreCommonClient.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader (Client)");
        NeoForgeClientRegistryHelper.init(modBus);
        INSTANCE.initialize();
    }

    @EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID, value = Dist.CLIENT)
    public static class ClientEventsSubscriber {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void clientSetup(FMLClientSetupEvent event) {
            INSTANCE.onClientSetup();
        }

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
