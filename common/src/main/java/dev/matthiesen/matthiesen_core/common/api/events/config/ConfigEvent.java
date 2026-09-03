package dev.matthiesen.matthiesen_core.common.api.events.config;

/**
 * This class defines events related to mod configuration loading, unloading, and reloading. It provides three nested
 * record classes: {@link Loading}, {@link Unloading}, and {@link Reloading}, each representing a specific configuration
 * event. These events are fired when a mod config is loaded, unloaded, or reloaded, respectively, and contain the relevant
 * {@link ModConfig} instance associated with the event.
 */
public final class ConfigEvent {
    private ConfigEvent() {}

    /**
     * Fired when a mod config is loading for the supplied mod id.
     * @param config The mod config that is being loaded. This contains the default configuration values that are being applied to the mod.
     */
    public record Loading(ModConfig config) {}

    /**
     * Fired when a mod config is unloading for the supplied mod id.
     * @param config The mod config that is being unloaded. This contains the current configuration values that are being saved to disk before the mod is unloaded.
     */
    public record Unloading(ModConfig config) {}

    /**
     * Fired when a mod config is reloading for the supplied mod id.
     * @param config The mod config that is being reloaded. This contains the new configuration values that are being applied to the mod.
     */
    public record Reloading(ModConfig config) {}
}
