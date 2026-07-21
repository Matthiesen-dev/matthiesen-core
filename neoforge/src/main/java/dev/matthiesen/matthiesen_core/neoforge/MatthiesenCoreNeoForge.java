package dev.matthiesen.matthiesen_core.neoforge;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.neoforge.platform.helpers.NeoForgeRegistryHelper;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@Mod(MatthiesenCoreCommon.MOD_ID)
public final class MatthiesenCoreNeoForge {
    public static MatthiesenCoreCommon INSTANCE;
    public static volatile MinecraftServer SERVER_INSTANCE;

    public MatthiesenCoreNeoForge(IEventBus modBus) {
        INSTANCE = MatthiesenCoreCommon.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        NeoForgeRegistryHelper.init(modBus);
        INSTANCE.initialize();
    }

    @EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID)
    public static class ServerEventsSubscriber {

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            SERVER_INSTANCE = event.getServer();
        }

        @SubscribeEvent
        public static void onServerStopped(ServerStoppedEvent event) {
            SERVER_INSTANCE = null;
        }

    }
}
