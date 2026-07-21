package dev.matthiesen.matthiesen_core.neoforge.events;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.neoforge.MatthiesenCoreNeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID)
public final class NeoForgeServerEvents {
    public NeoForgeServerEvents() {}

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MatthiesenCoreNeoForge.SERVER_INSTANCE = event.getServer();
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        MatthiesenCoreNeoForge.SERVER_INSTANCE = null;
    }
}
