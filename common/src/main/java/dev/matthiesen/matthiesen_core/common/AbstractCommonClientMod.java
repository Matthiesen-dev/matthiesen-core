package dev.matthiesen.matthiesen_core.common;

import dev.matthiesen.matthiesen_core.common.api.platform.CommonMod;
import dev.matthiesen.matthiesen_core.common.api.platform.LoggerMethods;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.common.core.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract class representing a common client mod. This class provides basic functionality for initializing the client-side of the mod,
 * tracking errors, and managing client-specific services. Mods should extend this class to leverage the common client functionality.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommonClientMod implements CommonMod, LoggerMethods {
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
    @Override
    public String getModId() {
        return MOD_ID;
    }

    /**
     * Get the mod's name
     * @return The mod's name
     */
    @Override
    public String getModName() {
        return MOD_NAME;
    }

    /**
     * Get the mod's logger
     * @return The mod's logger
     */
    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    /**
     * Track an error using the mod's error tracking system.
     * @param throwable The throwable to track
     */
    @Override
    public void trackError(Throwable throwable) {
        COMMON_MOD.trackError(throwable);
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
