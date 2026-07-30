package dev.matthiesen.matthiesen_core.common.core;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents;
import dev.matthiesen.matthiesen_core.common.api.platform.CommonClientMod;
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
public final class MatthiesenCoreCommonClient implements CommonClientMod {
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
    private MatthiesenCoreCommonClient() {}

    /**
     * Initializes the client-side components of the mod, including managers for screens, entity renderers,
     * block outlines, HUD layers, and keybindings. This method should be called during the client setup phase of the mod lifecycle.
     */
    @Override
    public void initialize() {
        if (initialized) return;

        initialized = true;

        ScreenManager.INSTANCE.initialize(CLIENT_EVENTS_LISTENERS);
        EntityRendererManager.INSTANCE.initialize(CLIENT_EVENTS_LISTENERS);
        KeybindingsManager.INSTANCE.initialize(CLIENT_EVENTS_LISTENERS);
        PlatformClientEvents.initialize();

        LOGGER.info("Initialized Common Client");
    }

    /**
     * Called during the client setup phase to perform any necessary client-side initialization.
     */
    public void onClientSetup() {}

    @Override
    public String getModId() {
        return MatthiesenCoreCommon.MOD_ID;
    }

    @Override
    public String getModName() {
        return MatthiesenCoreCommon.MOD_NAME;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void trackError(Throwable throwable) {
        MatthiesenCoreCommon.INSTANCE.trackError(throwable);
    }

    @Override
    public ScreenManager getScreenManager() {
        return ScreenManager.INSTANCE;
    }

    @Override
    public EntityRendererManager getEntityRendererManager() {
        return EntityRendererManager.INSTANCE;
    }

    @Override
    public KeybindingsManager getKeybindingsManager() {
        return KeybindingsManager.INSTANCE;
    }
}
