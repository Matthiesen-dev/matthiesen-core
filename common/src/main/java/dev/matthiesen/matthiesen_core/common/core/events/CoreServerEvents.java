package dev.matthiesen.matthiesen_core.common.core.events;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.events.ServerEventListener;

/**
 * The CoreServerEvents class is responsible for registering core server events for the Matthiesen Core Common library. It provides a static method to register event listeners that handle various server-side events, allowing for custom behavior and functionality to be implemented in response to those events. This class is typically used during the initialization phase of the server to ensure that all necessary event listeners are registered and ready to handle incoming events.
 */
public final class CoreServerEvents {
    private CoreServerEvents() {}

    /**
     * Registers the core server events for the Matthiesen Core Common library. This method is responsible for setting up event listeners that handle various server-side events, allowing for custom behavior and functionality to be implemented in response to those events. It is typically called during the initialization phase of the server to ensure that all necessary event listeners are registered and ready to handle incoming events.
     */
    public static void register() {
//        MatthiesenCoreCommon.INSTANCE.getServerEventsManager().registerListener(MatthiesenCoreCommon.MOD_ID, new ServerEventListener() {
//        });
    }
}
