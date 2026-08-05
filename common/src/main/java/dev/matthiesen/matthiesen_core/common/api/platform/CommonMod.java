package dev.matthiesen.matthiesen_core.common.api.platform;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModContainer;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
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
     * @deprecated This method is deprecated and will be removed in future versions. Use the {@link #registerModConfig(String, ModConfigType, IConfigSpec)} or {@link #registerModConfig(String, ModConfigType, IConfigSpec, String)} methods instead for registering mod configurations.
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

    /**
     * Creates a new ModContainer instance for the mod. The ModContainer provides information about the mod, such as its ID, name, version, and platform data. It also allows for registering configuration specifications for the mod.
     * @param modId The unique identifier (ID) of the mod. This ID is typically a lowercase string that uniquely identifies the mod within the modding ecosystem.
     * @param type The type of configuration (e.g., COMMON, CLIENT, SERVER) that specifies how the configuration should be loaded and applied.
     * @param configSpec The configuration specification that defines the structure and default values of the configuration settings for the mod.
     */
    default void registerModConfig(String modId, ModConfigType type, IConfigSpec configSpec) {
        ModContainer modContainer = MatthiesenCoreCommon.INSTANCE.getCommonUtils().getModContainer(modId);
        if (modContainer == null) {
            throw new IllegalArgumentException("Mod container not found for mod ID: " + modId);
        }
        modContainer.registerConfig(type, configSpec);
    }

    /**
     * Creates a new ModContainer instance for the mod. The ModContainer provides information about the mod, such as its ID, name, version, and platform data. It also allows for registering configuration specifications for the mod.
     * @param modId The unique identifier (ID) of the mod. This ID is typically a lowercase string that uniquely identifies the mod within the modding ecosystem.
     * @param type The type of configuration (e.g., COMMON, CLIENT, SERVER) that specifies how the configuration should be loaded and applied.
     * @param configSpec The configuration specification that defines the structure and default values of the configuration settings for the mod.
     * @param filename The name of the configuration file (with the file extension, should probably be {@code .toml}). The ModContainer will use this name to create and manage the configuration file.
     */
    default void registerModConfig(String modId, ModConfigType type, IConfigSpec configSpec, String filename) {
        ModContainer modContainer = MatthiesenCoreCommon.INSTANCE.getCommonUtils().getModContainer(modId);
        if (modContainer == null) {
            throw new IllegalArgumentException("Mod container not found for mod ID: " + modId);
        }
        modContainer.registerConfig(type, configSpec, filename);
    }

    /**
     * Registers a runtime config loading listener for the supplied mod id.
     *
     * <p>This is separate from base config registration and is intended for advanced runtime parsing workflows.</p>
     */
    default void registerModConfigLoadingListener(String modId) {
        PlatformEvents.CONFIG_LOADING(modId);
    }

    /**
     * Registers a runtime config unloading listener for the supplied mod id.
     *
     * <p>This is separate from base config registration and is intended for advanced runtime parsing workflows.</p>
     */
    default void registerModConfigUnloadingListener(String modId) {
        PlatformEvents.CONFIG_UNLOADING(modId);
    }

    /**
     * Registers a runtime config reloading listener for the supplied mod id.
     *
     * <p>This is separate from base config registration and is intended for advanced runtime parsing workflows.</p>
     */
    default void registerModConfigReloadingListener(String modId) {
        PlatformEvents.CONFIG_RELOADING(modId);
    }
}
