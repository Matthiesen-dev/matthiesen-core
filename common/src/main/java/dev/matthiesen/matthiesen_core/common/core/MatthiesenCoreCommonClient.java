package dev.matthiesen.matthiesen_core.common.core;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import dev.matthiesen.matthiesen_core.common.core.client.EntityRendererManager;
import dev.matthiesen.matthiesen_core.common.core.client.KeybindingsManager;
import dev.matthiesen.matthiesen_core.common.core.client.ScreenManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

/**
 * MatthiesenCoreCommonClient is a singleton class that provides client-side functionalities and services for the Matthiesen Lib mod.
 * It manages initialization, logging, and access to various client-side managers and services used throughout the application.
 * This class is designed to be thread-safe and ensures that only one instance exists throughout the lifecycle of the application.
 * It provides methods for logging and access to client-side utilities and managers. It also handles the initialization of various
 * managers responsible for screens, entity renderers, block outlines, HUD layers, and keybindings. The class is intended to be used
 * by mods that depend on the Matthiesen Lib framework, providing a centralized point of access to client-side functionalities and services.
 */
@SuppressWarnings("unused")
public final class MatthiesenCoreCommonClient {
    private static final Logger LOGGER = LogManager.getLogger(MatthiesenCoreCommon.MOD_NAME + " (Client)");

    private static final CommonLoaderClientEventsListeners CLIENT_EVENTS_LISTENERS =
            ServiceLoader.load(CommonLoaderClientEventsListeners.class).findFirst().orElseThrow();

    /**
     * Singleton instance of the MatthiesenCoreCommonClient class, providing access to client-side mod functionality.
     */
    public static final MatthiesenCoreCommonClient INSTANCE = new MatthiesenCoreCommonClient();

    private boolean initialized;

    /**
     * Initializes the MatthiesenCoreCommonClient instance.
     */
    public MatthiesenCoreCommonClient() {
    }

    /**
     * Initializes the client-side components of the mod, including managers for screens, entity renderers,
     * block outlines, HUD layers, and keybindings. This method should be called during the client setup phase of the mod lifecycle.
     */
    public void initialize() {
        if (initialized) return;

        initialized = true;

        ScreenManager.INSTANCE.initialize(CLIENT_EVENTS_LISTENERS);
        EntityRendererManager.INSTANCE.initialize(CLIENT_EVENTS_LISTENERS);
        KeybindingsManager.INSTANCE.initialize(CLIENT_EVENTS_LISTENERS);
        PlatformClientEvents.initialize(CLIENT_EVENTS_LISTENERS);

        LOGGER.info("Initialized Common Client");
    }

    /**
     * Called during the client setup phase to perform any necessary client-side initialization.
     */
    public void onClientSetup() {}

    /**
     * Creates an info log message using the common mod's logger.
     * @param message the message to log
     */
    public void createInfoLog(String message) {
        LOGGER.info(message);
    }

    /**
     * Creates an error log message using the common mod's logger.
     * @param message the message to log
     */
    public void createErrorLog(String message) {
        LOGGER.error(message);
    }

    /**
     * Creates an error log message using the common mod's logger, including an associated throwable for debugging purposes.
     * @param message the message to log
     * @param throwable the throwable associated with the error, providing additional context for debugging
     */
    public void createErrorLog(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }

    /**
     * Returns the ScreenManager instance for managing menu screen registrations and platform callbacks.
     * @return the ScreenManager instance
     */
    public ScreenManager getScreenManager() {
        return ScreenManager.INSTANCE;
    }

    /**
     * Returns the EntityRendererManager instance for managing entity and block entity renderer registrations.
     * @return the EntityRendererManager instance
     */
    public EntityRendererManager getEntityRendererManager() {
        return EntityRendererManager.INSTANCE;
    }

    /**
     * Returns the KeybindingsManager instance for managing keybinding registrations and tick callbacks.
     * @return the KeybindingsManager instance
     */
    public KeybindingsManager getKeybindingsManager() {
        return KeybindingsManager.INSTANCE;
    }
}
