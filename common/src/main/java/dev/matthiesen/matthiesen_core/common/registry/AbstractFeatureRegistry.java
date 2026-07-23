package dev.matthiesen.matthiesen_core.common.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.registry.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.world.level.levelgen.feature.Feature;

/**
 * Abstract registry for features, providing a base implementation for registering features with a mod ID or an existing {@link RegistryBuilder}.
 */
@SuppressWarnings("unused")
public abstract class AbstractFeatureRegistry extends AbstractRegistry<Feature<?>> {
    /**
     * Creates an entity effect registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractFeatureRegistry(String modId) {
        super(modId, SupportedRegistries.FEATURE);
    }

    /**
     * Creates an entity effect registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform entity effect registrations
     */
    protected AbstractFeatureRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.FEATURE);
    }
}
