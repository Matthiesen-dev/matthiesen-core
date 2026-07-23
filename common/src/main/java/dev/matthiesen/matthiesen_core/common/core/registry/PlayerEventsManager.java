package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.events.PlayerEventListener;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderEventsListeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The PlayerEventsManager class is responsible for managing player event listeners and their registration with the PlayerEventRegistry.
 * It provides methods to register listeners, bind the registry, and ensure that listeners are registered when the registry is available.
 * This class follows the singleton pattern, ensuring that there is only one instance of the manager throughout the application.
 */
@SuppressWarnings("unused")
public final class PlayerEventsManager {
    private static final Map<String, PlayerEventListener> REGISTERED_LISTENERS = new ConcurrentHashMap<>();

    /**
     * Singleton instance of the PlayerEventsManager. This instance is used to manage player event listeners and their registration with
     * the PlayerEventRegistry. It ensures that there is only one instance of the manager throughout the application, providing a
     * centralized point for managing player events.
     */
    public static final PlayerEventsManager INSTANCE = new PlayerEventsManager();

    private boolean initialized;
    private CommonLoaderEventsListeners serverEventRegistry;

    private PlayerEventsManager() {}

    /**
     * Initializes the PlayerEventsManager. This method should be called once during the mod's initialization phase. It sets the
     * initialized flag to true and logs the initialization message. If the manager is already initialized, this method will return
     * without performing any action.
     */
    public synchronized void initialize(CommonLoaderEventsListeners registry) {
        if (initialized) return;

        initialized = true;
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Initialized Player Events Manager");
        bindRegistry(registry);
    }

    /**
     * Registers a player event listener for the specified mod ID. If the PlayerEventRegistry is already bound, the listener will be
     * registered immediately. Otherwise, it will be stored and registered when the registry is bound.
     * @param modId The mod ID associated with the listener. This is used to identify the source of the listener.
     * @param listener The player event listener to register. This listener will be notified of relevant player events.
     */
    public synchronized void registerListener(String modId, PlayerEventListener listener) {
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

    private void registerHandler(CommonLoaderEventsListeners registry, PlayerEventListener listener) {
        registry.onPlayerJoin(listener::onPlayerJoin);
        registry.onPlayerLeave(listener::onPlayerLeave);
        registry.onPlayerUseItemResult(listener::onPlayerUseItemResult);
        registry.onPlayerUseBlockResult(listener::onPlayerUseBlockResult);
    }
}
