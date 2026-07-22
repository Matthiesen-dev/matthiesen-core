package dev.matthiesen.matthiesen_core.common.abstracts.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.world.level.block.Block;

/**
 * Convenience base class for registries that register {@link Block} instances.
 *
 * <p>This type locks registration to the block registry category by wiring
 * {@link SupportedRegistries#BLOCK} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractBlockRegistry extends AbstractRegistry<Block> {
    /**
     * Creates a block registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractBlockRegistry(String modId) {
        super(modId, SupportedRegistries.BLOCK);
    }

    /**
     * Creates a block registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform block registrations
     */
    protected AbstractBlockRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.BLOCK);
    }
}

