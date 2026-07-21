package dev.matthiesen.matthiesen_lib.fabric;

import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import dev.matthiesen.matthiesen_lib.fabric.events.MatthiesenLibFabricEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;

public final class MatthiesenLibFabric implements ModInitializer {
    public static volatile MinecraftServer SERVER_INSTANCE;

    @Override
    public void onInitialize() {
        var instance = MatthiesenLibCommon.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();

        MatthiesenLibFabricEvents.init(instance);
    }
}
