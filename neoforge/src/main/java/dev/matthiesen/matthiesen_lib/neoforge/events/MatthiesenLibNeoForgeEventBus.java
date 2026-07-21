package dev.matthiesen.matthiesen_lib.neoforge.events;

import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import dev.matthiesen.matthiesen_lib.neoforge.MatthiesenLibNeoForge;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@EventBusSubscriber(modid = MatthiesenLibCommon.MOD_ID)
public final class MatthiesenLibNeoForgeEventBus {
    public MatthiesenLibNeoForgeEventBus() {}

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        MatthiesenLibNeoForge.SERVER_INSTANCE = server;
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        MatthiesenLibNeoForge.SERVER_INSTANCE = null;
    }
}
