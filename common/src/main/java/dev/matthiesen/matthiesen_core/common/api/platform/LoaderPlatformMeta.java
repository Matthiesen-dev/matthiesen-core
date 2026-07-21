package dev.matthiesen.matthiesen_core.common.api.platform;

/**
 * This enum represents the different platforms that the mod can run on. Each platform has a label and a mod ID associated
 * with it, which are used for identifying the platform and its associated mod. The platforms currently supported are Fabric and NeoForge.
 */
@SuppressWarnings("unused")
public enum LoaderPlatformMeta {
    FABRIC("fabric", "fabricloader"),
    NEOFORGE("neoforge");

    private final String label;
    private final String modId;

    /**
     * Constructor for the Platform enum. This constructor is used to associate a label with each platform, which is used
     * for identifying the platform.
     * @param label The label associated with this platform.
     */
    LoaderPlatformMeta(String label) {
        this.label = label;
        this.modId = label;
    }

    /**
     * Constructor for the Platform enum. This constructor is used to associate a label and mod ID with each platform, which
     * are used for identifying the platform and its associated mod.
     * @param label The label associated with this platform.
     * @param modId The mod ID associated with this platform.
     */
    LoaderPlatformMeta(String label, String modId) {
        this.label = label;
        this.modId = modId;
    }

    /**
     * Gets the label associated with this platform. The label is a human-readable string that represents the platform's name or identifier.
     * @return The label associated with this platform.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the mod ID associated with this platform. The mod ID is a unique identifier for the mod on the given platform, and
     * is used for various purposes such as registering items, blocks, and other game elements.
     * @return The mod ID associated with this platform.
     */
    public String getModId() {
        return modId;
    }
}
