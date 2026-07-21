package dev.matthiesen.matthiesen_core.fabric;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommonClient;
import net.fabricmc.api.ClientModInitializer;

public final class MatthiesenCoreFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        var instance = MatthiesenCoreCommonClient.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader (Client)");
        instance.initialize();
        instance.onClientSetup();
    }
}
