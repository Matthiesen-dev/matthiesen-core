package dev.matthiesen.matthiesen_core.fabric;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.fabric.text_parsers.EmbersTextParserFabric;
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

        if (instance.getCommonUtils().isModLoaded(BuiltInTextParsers.EMBERS.getId())) {
            instance.getTextParserManager().registerTextParser(new EmbersTextParserFabric());
        }

        instance.onCommonServerSetup();

        ServerLifecycleEvents.SERVER_STARTING.register(server ->
                SERVER_INSTANCE = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server ->
                SERVER_INSTANCE = null);
    }
}
