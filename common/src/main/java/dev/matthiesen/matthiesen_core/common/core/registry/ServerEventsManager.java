package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.events.ServerEventListener;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderEventsListeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ServerEventsManager class is responsible for managing server event listeners and their registration with the server
 * event registry. It provides methods to register listeners, bind a server event registry, and initialize the manager. This
 * class ensures that server event listeners are properly registered and notified of relevant server events during the mod's lifecycle.
 */
@SuppressWarnings("unused")
public final class ServerEventsManager {
    private static final Map<String, ServerEventListener> REGISTERED_LISTENERS = new ConcurrentHashMap<>();

    /**
     * Singleton instance of the ServerEventsManager. This instance is used to manage server event listeners and their registration
     * with the server event registry. It ensures that there is only one instance of the manager throughout the application,
     * providing a centralized point for managing server events.
     */
    public static final ServerEventsManager INSTANCE = new ServerEventsManager();

    private boolean initialized;
    private CommonLoaderEventsListeners serverEventRegistry;

    private ServerEventsManager() {}

    /**
     * Initializes the ServerEventsManager. This method should be called once during the mod's initialization phase. It sets
     * the initialized flag to true and logs an informational message indicating that the Server Events Manager has been initialized.
     * If the manager is already initialized, this method will return without performing any actions.
     */
    public synchronized void initialize(CommonLoaderEventsListeners registry) {
        if (initialized) return;

        initialized = true;
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Initialized Server Events Manager");
        bindRegistry(registry);
    }

    /**
     * Registers a server event listener for the specified mod ID. If the server event registry is already bound, the listener
     * will be registered immediately. Otherwise, it will be stored and registered when the registry is bound.
     * @param modId The mod ID associated with the listener. This is used to identify the listener and ensure that it is registered only once per mod.
     * @param listener The server event listener to register. This listener will be notified of relevant server events when they occur.
     */
    public synchronized void registerListener(String modId, ServerEventListener listener) {
        REGISTERED_LISTENERS.put(modId, listener);
        if (serverEventRegistry != null) {
            registerHandler(serverEventRegistry, listener);
        }
    }

    private synchronized void bindRegistry(CommonLoaderEventsListeners registry) {
        serverEventRegistry = registry;
        for (var entry : REGISTERED_LISTENERS.entrySet()) {
            registerHandler(registry, entry.getValue());
        }
    }

    private void registerHandler(CommonLoaderEventsListeners registry, ServerEventListener listener) {
        registry.onServerStarting(listener::onServerStarting);
        registry.onServerStopping(listener::onServerStopping);
        registry.onServerReload(listener::onServerReload);
        registry.onServerStartTick(listener::onServerStartTick);
        registry.onServerEndTick(listener::onServerEndTick);
    }
}
