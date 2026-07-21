package dev.matthiesen.matthiesen_lib.common.api.platform;

/**
 * This interface represents a mod container, which provides information about a mod, such as its name, version, and platform
 * data. It also provides a method to generate a unique metric ID for the mod based on its name and platform.
 */
@SuppressWarnings("unused")
public interface ModContainer {
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
    Platform getPlatformData();

    /**
     * Generates a unique metric ID for the mod based on its name and platform. The metric ID is in the format "platform:mod_name",
     * where "platform" is the label of the platform (e.g., "fabric", "forge") and "mod_name" is the name of the mod in lowercase
     * with spaces replaced by underscores.
     * @return A unique metric ID for the mod.
     */
    default String getModMetricId() {
        Platform platform = getPlatformData();
        String platformName = platform.getLabel();
        String modName = getModName().toLowerCase().replaceAll("\\s+", "_");
        return platformName + ":" + modName;
    }
}
