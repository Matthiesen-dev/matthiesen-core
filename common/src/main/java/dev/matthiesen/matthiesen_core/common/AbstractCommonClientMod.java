package dev.matthiesen.matthiesen_core.common;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.common.core.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract class representing a common client mod. This class provides basic functionality for initializing the client-side of the mod,
 * tracking errors, and managing client-specific services. Mods should extend this class to leverage the common client functionality.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommonClientMod {
    private final String MOD_ID;
    private final String MOD_NAME;
    private final Logger LOGGER;
    private final AbstractCommonMod COMMON_MOD;

    /**
     * Constructor for the AbstractCommonClientMod class.
     *
     * @param commonMod The Server-side mod instance
     */
    public AbstractCommonClientMod(AbstractCommonMod commonMod) {
        this.COMMON_MOD = commonMod;
        this.MOD_ID = commonMod.getModId();
        this.MOD_NAME = commonMod.getModName() + " (client)";
        this.LOGGER = LogManager.getLogger(MOD_NAME);
    }

    /**
     * Initializer for the client mod. This method is called during the mod initialization phase.
     */
    public abstract void initialize();

    /**
     * Get the mod's ID
     * @return The mod's ID
     */
    public String getModId() {
        return MOD_ID;
    }

    /**
     * Get the mod's name
     * @return The mod's name
     */
    public String getModName() {
        return MOD_NAME;
    }

    /**
     * Get the mod's logger
     * @return The mod's logger
     */
    public Logger getLogger() {
        return LOGGER;
    }

    /**
     * Send an info log message using the mod's logger.
     * @param message The message to log
     */
    public void createInfoLog(String message) {
        getLogger().info(message);
    }

    /**
     * Send a warning log message using the mod's logger.
     * @param message The message to log
     */
    public void createWarnLog(String message) {
        getLogger().warn(message);
    }

    /**
     * Send an error log message using the mod's logger.
     * @param message The message to log
     */
    public void createErrorLog(String message) {
        getLogger().error(message);
    }

    /**
     * Send an error log message with a throwable using the mod's logger.
     * @param message The message to log
     * @param throwable The throwable to log
     */
    public void createErrorLog(String message, Throwable throwable) {
        getLogger().error(message, throwable);
        COMMON_MOD.trackError(throwable);
    }

    /**
     * Returns the CommonLoaderClientEventsListeners instance for handling platform-specific client events.
     * @return the CommonLoaderClientEventsListeners instance
     */
    public CommonLoaderClientEventsListeners getClientEventsListeners() {
        return MatthiesenCoreCommonClient.INSTANCE.getClientEventsListeners();
    }

    /**
     * Returns the ScreenManager instance for managing menu screen registrations and platform callbacks.
     * @return the ScreenManager instance
     */
    public ScreenManager getScreenManager() {
        return MatthiesenCoreCommonClient.INSTANCE.getScreenManager();
    }

    /**
     * Returns the EntityRendererManager instance for managing entity and block entity renderer registrations.
     * @return the EntityRendererManager instance
     */
    public EntityRendererManager getEntityRendererManager() {
        return MatthiesenCoreCommonClient.INSTANCE.getEntityRendererManager();
    }

    /**
     * Returns the KeybindingsManager instance for managing keybinding registrations and tick callbacks.
     * @return the KeybindingsManager instance
     */
    public KeybindingsManager getKeybindingsManager() {
        return MatthiesenCoreCommonClient.INSTANCE.getKeybindingsManager();
    }
}
