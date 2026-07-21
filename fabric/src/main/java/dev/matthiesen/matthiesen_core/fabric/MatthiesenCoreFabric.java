package dev.matthiesen.matthiesen_core.fabric;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public final class MatthiesenCoreFabric implements ModInitializer {
    public static volatile MinecraftServer SERVER_INSTANCE;

    @Override
    public void onInitialize() {
        var instance = MatthiesenCoreCommon.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();

        ServerLifecycleEvents.SERVER_STARTING.register(server ->
                SERVER_INSTANCE = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server ->
                SERVER_INSTANCE = null);
    }
}
