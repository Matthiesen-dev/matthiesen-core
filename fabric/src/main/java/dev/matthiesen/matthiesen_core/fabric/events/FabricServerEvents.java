package dev.matthiesen.matthiesen_core.fabric.events;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.fabric.MatthiesenCoreFabric;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class FabricServerEvents {
    public static void init(MatthiesenCoreCommon instance) {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            instance.createInfoLog("Server starting... registering event handlers");
            MatthiesenCoreFabric.SERVER_INSTANCE = server;
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            instance.createInfoLog("Server stopped... clearing server instance");
            MatthiesenCoreFabric.SERVER_INSTANCE = null;
        });
    }
}
