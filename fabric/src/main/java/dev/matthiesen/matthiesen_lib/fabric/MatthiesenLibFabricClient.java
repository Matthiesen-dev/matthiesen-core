package dev.matthiesen.matthiesen_lib.fabric;

import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommonClient;
import dev.matthiesen.matthiesen_lib.fabric.events.MatthiesenLibFabricClientEvents;
import net.fabricmc.api.ClientModInitializer;

public final class MatthiesenLibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        var instance = MatthiesenLibCommonClient.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader (Client)");
        instance.initialize();
        instance.onClientSetup();

        MatthiesenLibFabricClientEvents.init(instance);
    }
}
