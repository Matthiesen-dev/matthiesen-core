package dev.matthiesen.matthiesen_core.fabric.events;

import dev.matthiesen.matthiesen_core.fabric.MatthiesenCoreFabric;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class FabricServerEvents {
    public static void init() {
        ServerLifecycleEvents.SERVER_STARTING.register(server ->
                MatthiesenCoreFabric.SERVER_INSTANCE = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server ->
                MatthiesenCoreFabric.SERVER_INSTANCE = null);
    }
}
