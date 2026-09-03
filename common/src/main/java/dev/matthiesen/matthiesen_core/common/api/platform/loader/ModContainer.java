package dev.matthiesen.matthiesen_core.common.api.platform.loader;

import net.neoforged.fml.config.IConfigSpec;

/**
 * This interface represents a mod container, which provides information about a mod, such as its name, version, and platform
 * data. It also provides a method to generate a unique metric ID for the mod based on its name and platform.
 */
@SuppressWarnings("unused")
public interface ModContainer {
    /**
     * Gets the unique identifier (ID) of the mod. The mod ID is typically a lowercase string that uniquely identifies the mod within the modding ecosystem.
     * @return The unique identifier (ID) of the mod as a string.
     */
    String getModId();

    /**
     * Gets the name of the mod. The name is typically a human-readable string that represents the mod's title or identifier.
     * @return The name of the mod as a string.
     */
    String getModName();

    /**
     * Gets the version of the mod. The version is typically a string that represents the current version of the mod, such as "1.0.0" or "2.3.1-beta".
     * @return The version of the mod as a string.
     */
    String getModVersion();

    /**
     * Gets the platform data associated with this mod. The platform data provides information about the mod's platform, such as
     * its label (e.g., "fabric", "forge") and other relevant details.
     * @return The platform data associated with this mod.
     */
    LoaderPlatformMeta getPlatformData();

    /**
     * Registers a configuration specification for the mod. This method allows the mod to register its configuration settings with the underlying mod loader.
     * @param type The type of configuration (e.g., COMMON, CLIENT, SERVER) that specifies how the configuration should be loaded and applied.
     * @param configSpec The configuration specification that defines the structure and default values of the configuration settings for the mod.
     */
    void registerConfig(ModConfigType type, IConfigSpec configSpec);

    /**
     * Registers a configuration specification for the mod. This method allows the mod to register its configuration settings with the underlying mod loader.
     * @param type The type of configuration (e.g., COMMON, CLIENT, SERVER) that specifies how the configuration should be loaded and applied.
     * @param configSpec The configuration specification that defines the structure and default values of the configuration settings for the mod.
     * @param filename The filename for the configuration file. This allows the mod to specify a custom name for its configuration file, which will be used when saving and loading the configuration settings.
     */
    void registerConfig(ModConfigType type, IConfigSpec configSpec, String filename);

    /**
     * Registers a listener for configuration loading events. This method allows the mod to respond to the loading of its configuration settings, which may occur during mod initialization or when the mod is being enabled.
     */
    void registerConfigLoadingListener();

    /**
     * Registers a listener for configuration unloading events. This method allows the mod to respond to the unloading of its configuration settings, which may occur when the mod is being disabled or removed.
     */
    void registerConfigUnloadingListener();

    /**
     * Registers a listener for configuration reloading events. This method allows the mod to respond to changes in its configuration settings at runtime.
     */
    void registerConfigReloadingListener();

    /**
     * Generates a unique metric ID for the mod based on its name and platform. The metric ID is in the format "platform:mod_name",
     * where "platform" is the label of the platform (e.g., "fabric", "forge") and "mod_name" is the name of the mod in lowercase
     * with spaces replaced by underscores.
     * @return A unique metric ID for the mod.
     */
    default String getModMetricId() {
        return getModName().toLowerCase().replaceAll("\\s+", "_");
    }
}
