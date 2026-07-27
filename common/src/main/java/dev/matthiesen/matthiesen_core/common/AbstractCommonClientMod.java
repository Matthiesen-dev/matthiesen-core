package dev.matthiesen.matthiesen_core.common;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.common.core.client.*;
import dev.matthiesen.matthiesen_core.common.utility.config.ConfigFolderManager;
import dev.matthiesen.matthiesen_core.common.utility.config.ConfigManager;
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

    /**
     * Creates a new ConfigManager instance for managing configuration files. The ConfigManager is responsible for loading, saving, and managing configuration data for the mod.
     * @param configClass The class type of the configuration data. This class should represent the structure of the configuration file and contain fields corresponding to the configuration options.
     * @param configName The name of the configuration file (without the file extension). The ConfigManager will use this name to create and manage the configuration file.
     * @return A new instance of ConfigManager for managing the specified configuration class and file name. The ConfigManager will handle loading, saving, and managing the configuration data for the mod.
     * @param <T> The type of the configuration class. This type should represent the structure of the configuration file and contain fields corresponding to the configuration options.
     */
    public <T> ConfigManager<T> createConfigManager(Class<T> configClass, String configName) {
        return new ConfigManager<>(configClass, configName, MOD_ID);
    }

    /**
     * Creates a new ConfigFolderManager instance for managing configuration files within a specified folder. The ConfigFolderManager is responsible for loading, saving, and managing multiple configuration files within the specified folder.
     * @param configClass The class type of the configuration data. This class should represent the structure of the configuration files and contain fields corresponding to the configuration options.
     * @param folderName The name of the folder where the configuration files will be stored. The ConfigFolderManager will use this folder to create and manage multiple configuration files.
     * @return A new instance of ConfigFolderManager for managing the specified configuration class and folder name. The ConfigFolderManager will handle loading, saving, and managing multiple configuration files within the specified folder.
     * @param <T> The type of the configuration class. This type should represent the structure of the configuration files and contain fields corresponding to the configuration options.
     */
    public <T> ConfigFolderManager<T> createConfigFolderManager(Class<T> configClass, String folderName) {
        return new ConfigFolderManager<>(configClass, folderName, MOD_ID);
    }
}
