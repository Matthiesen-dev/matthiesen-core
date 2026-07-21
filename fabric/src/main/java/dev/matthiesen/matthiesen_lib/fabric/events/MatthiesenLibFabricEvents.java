package dev.matthiesen.matthiesen_lib.fabric.events;

import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import dev.matthiesen.matthiesen_lib.fabric.MatthiesenLibFabric;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class MatthiesenLibFabricEvents {
    public static void init(MatthiesenLibCommon instance) {

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            instance.createInfoLog("Server starting... registering event handlers");
            MatthiesenLibFabric.SERVER_INSTANCE = server;
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            instance.createInfoLog("Server stopped... clearing server instance");
            MatthiesenLibFabric.SERVER_INSTANCE = null;
        });

    }
}
