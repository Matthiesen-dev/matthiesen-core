package dev.matthiesen.matthiesen_core.common.api.events.config;

import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import net.neoforged.fml.config.IConfigSpec;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a mod configuration, including its type, specification, file name, mod ID, and an optional loaded configuration.
 * @param type The type of the mod configuration.
 * @param spec The specification of the mod configuration.
 * @param fileName The file name of the mod configuration.
 * @param modId The ID of the mod associated with the configuration.
 * @param loadedConfig The optional loaded configuration, if available.
 */
public record ModConfig(
        ModConfigType type,
        IConfigSpec spec,
        String fileName,
        String modId,
        @Nullable IConfigSpec.ILoadedConfig loadedConfig
        ) {
}
