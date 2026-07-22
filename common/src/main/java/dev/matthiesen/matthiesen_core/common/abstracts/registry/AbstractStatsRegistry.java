package dev.matthiesen.matthiesen_core.common.abstracts.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.resources.ResourceLocation;

/**
 * Convenience base class for registries that register custom statistics as {@link ResourceLocation} keys.
 *
 * <p>This type locks registration to the stats registry category by wiring
 * {@link SupportedRegistries#STAT} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractStatsRegistry extends AbstractRegistry<ResourceLocation> {
    /**
     * Creates a stats registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractStatsRegistry(String modId) {
        super(modId, SupportedRegistries.STAT);
    }

    /**
     * Creates a stats registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform stats registrations
     */
    protected AbstractStatsRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.STAT);
    }
}

