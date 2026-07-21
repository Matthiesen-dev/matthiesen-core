package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public final class FabricLoaderClientEventsListeners implements CommonLoaderClientEventsListeners {
    @Override
    public void onClientStopping(Runnable runnable) {
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> runnable.run());
    }
}
