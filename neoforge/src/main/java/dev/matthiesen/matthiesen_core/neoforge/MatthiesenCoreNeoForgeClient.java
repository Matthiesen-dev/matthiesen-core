package dev.matthiesen.matthiesen_core.neoforge;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommonClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = MatthiesenCoreCommon.MOD_ID, dist = Dist.CLIENT)
public final class MatthiesenCoreNeoForgeClient {
    public static MatthiesenCoreCommonClient INSTANCE;

    public MatthiesenCoreNeoForgeClient() {
        INSTANCE = MatthiesenCoreCommonClient.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader (Client)");
        INSTANCE.initialize();
    }

    @EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID, value = Dist.CLIENT)
    public static class ClientEventsSubscriber {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void clientSetup(FMLClientSetupEvent event) {
            INSTANCE.onClientSetup();
        }

    }
}
