package dev.matthiesen.matthiesen_core.common.api.platform;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModContainer;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
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
     * @param modId The unique identifier (ID) of the mod for which the config loading listener is being registered. This ID is typically a lowercase string that uniquely identifies the mod within the modding ecosystem.
     */
    default void registerModConfigLoadingListener(String modId) {
        PlatformEvents.CONFIG_LOADING(modId);
    }

    /**
     * Registers a runtime config unloading listener for the supplied mod id.
     *
     * <p>This is separate from base config registration and is intended for advanced runtime parsing workflows.</p>
     * @param modId The unique identifier (ID) of the mod for which the config loading listener is being registered. This ID is typically a lowercase string that uniquely identifies the mod within the modding ecosystem.
     */
    default void registerModConfigUnloadingListener(String modId) {
        PlatformEvents.CONFIG_UNLOADING(modId);
    }

    /**
     * Registers a runtime config reloading listener for the supplied mod id.
     *
     * <p>This is separate from base config registration and is intended for advanced runtime parsing workflows.</p>
     * @param modId The unique identifier (ID) of the mod for which the config loading listener is being registered. This ID is typically a lowercase string that uniquely identifies the mod within the modding ecosystem.
     */
    default void registerModConfigReloadingListener(String modId) {
        PlatformEvents.CONFIG_RELOADING(modId);
    }
}
