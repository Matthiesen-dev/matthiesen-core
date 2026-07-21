package dev.matthiesen.matthiesen_core.fabric;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.fabric.events.FabricServerEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;

public final class MatthiesenCoreFabric implements ModInitializer {
    public static volatile MinecraftServer SERVER_INSTANCE;

    @Override
    public void onInitialize() {
        var instance = MatthiesenCoreCommon.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();

        FabricServerEvents.init(instance);
    }
}
