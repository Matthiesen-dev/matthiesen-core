package dev.matthiesen.matthiesen_lib.common.api.platform;

import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public interface CommonLoaderUtils {
    /**
     * Get the Minecraft server instance. This method provides access to the Minecraft server, which can be used to perform various server-side operations,
     * such as managing players, handling commands, or interacting with the world. The implementation of this method may vary depending on the mod loader,
     * but it generally returns the current instance of the Minecraft server that is running the game. This allows mod developers to access server functionality
     * in a way that is compatible with multiple mod loaders without having to worry about the specific implementation details of each loader.
     * @return The current instance of the Minecraft server.
     */
    MinecraftServer getServer();

    /**
     * Check if the current environment is a development environment. This method provides a way to determine if the mod is running in a development environment,
     * which can be useful for enabling or disabling certain features, logging, or debugging information.
     * The implementation of this method may vary depending on the mod loader, but it generally returns a boolean value indicating whether the mod is running
     * in a development environment. This allows mod developers to conditionally execute code based on the environment, enabling features or logging that are
     * only relevant during development without affecting the behavior of the mod in production environments.
     * @return true if the current environment is a development environment, false otherwise. This allows mod developers to conditionally execute code based
     * on the environment, enabling features or logging that are only relevant during development without affecting the behavior of the mod in production environments.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Check if a specific mod is loaded. This method provides a way to check if a particular mod is present in the current environment.
     * @param modId The mod ID of the mod to check for. This is typically a unique identifier for the mod, such as "fabricloader" or "neoforge".
     * @return true if the specified mod is loaded, false otherwise. This allows mod developers to conditionally execute code based on the presence
     * of other mods, enabling compatibility and integration with different mod loaders and mod ecosystems.
     */
    boolean isModLoaded(String modId);

    /**
     * Get the game directory. This method provides access to the game directory, which is the root folder where the game is installed and where various game-related files are stored.
     * The implementation of this method may vary depending on the mod loader, but it generally returns a Path object that represents the location of the game directory. This allows mod developers to access game files and resources in a way that is compatible with multiple mod loaders without having to worry about the specific implementation details of each loader.
     * @return The Path object representing the location of the game directory.
     */
    Path getGameDirectory();

    /**
     * Get the configuration file for a specific mod. This method provides access to the configuration file for a mod, which can be used to read
     * or write configuration settings.
     * The implementation of this method may vary depending on the mod loader, but it generally returns a Path object that represents the location
     * of the configuration file for the specified mod. This allows mod developers to access mod configuration files in a way that is compatible
     * with multiple mod loaders without having to worry about the specific implementation details of each loader.
     * @param dir The directory where the configuration file is located.
     * @param file The name of the configuration file.
     * @return The Path object representing the location of the configuration file.
     */
    Path getModConfig(String dir, String file);

    /**
     * Get the current environment in which the mod is running. This method provides information about the environment, such as whether the mod
     * is running in a development or production environment.
     * The implementation of this method may vary depending on the mod loader, but it generally returns an instance of the Environment interface
     * that represents the current environment. This allows mod developers to access environment-specific functionality in a way that is compatible
     * with multiple mod loaders without having to worry about the specific implementation details of each loader.
     * @return The current environment in which the mod is running.
     */
    Environment getEnvironment();

    /**
     * Get the mod container for a specific mod ID. This method provides access to the mod container, which contains information about the mod,
     * such as its name, version, and platform data. The implementation of this method may vary depending on the mod loader, but it generally
     * returns an instance of the ModContainer interface that represents the mod container for the specified mod ID. This allows mod developers
     * to access mod-specific functionality in a way that is compatible with multiple mod loaders without having to worry about the specific
     * implementation details of each loader.
     * @param modId The mod ID for which to retrieve the mod container. This is typically a unique identifier for the mod, such as "fabricloader" or "neoforge".
     * @return The mod container for the specified mod ID, or null if the mod is not loaded or the mod ID is invalid. The returned ModContainer
     * instance provides access to information about the mod, such as its name, version, and platform data.
     */
    ModContainer getModContainer(String modId);
}
