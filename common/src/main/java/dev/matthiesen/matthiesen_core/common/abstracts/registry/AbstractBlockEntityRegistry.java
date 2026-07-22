package dev.matthiesen.matthiesen_core.common.abstracts.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Convenience base class for registries that register {@link BlockEntityType} instances.
 *
 * <p>This type locks registration to the block entity type registry category by wiring
 * {@link SupportedRegistries#BLOCK_ENTITY} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractBlockEntityRegistry extends AbstractRegistry<BlockEntityType<?>> {
    /**
     * Creates a block entity type registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractBlockEntityRegistry(String modId) {
        super(modId, SupportedRegistries.BLOCK_ENTITY);
    }

    /**
     * Creates a block entity type registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform block entity type registrations
     */
    protected AbstractBlockEntityRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.BLOCK_ENTITY);
    }
}

