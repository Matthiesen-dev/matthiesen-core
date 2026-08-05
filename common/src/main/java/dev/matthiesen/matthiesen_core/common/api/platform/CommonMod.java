package dev.matthiesen.matthiesen_core.common.api.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.matthiesen_core.common.utility.config.ConfigFolderManager;
import dev.matthiesen.matthiesen_core.common.utility.config.ConfigManager;
import net.neoforged.fml.config.IConfigSpec;

/**
 * Interface representing a common mod. This interface provides methods for retrieving the mod's ID and name, as well as
 * creating configuration managers for managing configuration files.
 */
@SuppressWarnings("unused")
public interface CommonMod extends LoggerMethods {

    /**
     * Get the mod's ID
     * @return The mod's ID
     */
    String getModId();

    /**
     * Get the mod's name
     * @return The mod's name
     */
    String getModName();

    /**
     * Initializes the mod. This method should be called during the mod's initialization phase to set up necessary components and configurations.
     */
    void initialize();

    /**
     * Creates a new ConfigManager instance for managing configuration files. The ConfigManager is responsible for loading, saving, and managing configuration data for the mod.
     * @param configClass The class type of the configuration data. This class should represent the structure of the configuration file and contain fields corresponding to the configuration options.
     * @param configName The name of the configuration file (without the file extension). The ConfigManager will use this name to create and manage the configuration file.
     * @return A new instance of ConfigManager for managing the specified configuration class and file name. The ConfigManager will handle loading, saving, and managing the configuration data for the mod.
     * @param <T> The type of the configuration class. This type should represent the structure of the configuration file and contain fields corresponding to the configuration options.
     * @deprecated This method is deprecated and will be removed in future versions. Use the {@link CommonServerMod#registerModConfig(String, ModConfigType, IConfigSpec)} or {@link CommonServerMod#registerModConfig(String, ModConfigType, IConfigSpec, String)} methods instead for registering mod configurations.
     */
    @Deprecated(forRemoval = true)
    default <T> ConfigManager<T> createConfigManager(Class<T> configClass, String configName) {
        return new ConfigManager<>(configClass, configName, getModId());
    }

    /**
     * Creates a new ConfigFolderManager instance for managing configuration files within a specified folder. The ConfigFolderManager is responsible for loading, saving, and managing multiple configuration files within the specified folder.
     * @param configClass The class type of the configuration data. This class should represent the structure of the configuration files and contain fields corresponding to the configuration options.
     * @param folderName The name of the folder where the configuration files will be stored. The ConfigFolderManager will use this folder to create and manage multiple configuration files.
     * @return A new instance of ConfigFolderManager for managing the specified configuration class and folder name. The ConfigFolderManager will handle loading, saving, and managing multiple configuration files within the specified folder.
     * @param <T> The type of the configuration class. This type should represent the structure of the configuration files and contain fields corresponding to the configuration options.
     */
    default <T> ConfigFolderManager<T> createConfigFolderManager(Class<T> configClass, String folderName) {
        return new ConfigFolderManager<>(configClass, folderName, getModId());
    }
}
