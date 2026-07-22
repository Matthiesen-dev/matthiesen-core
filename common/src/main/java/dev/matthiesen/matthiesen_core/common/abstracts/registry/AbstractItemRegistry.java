package dev.matthiesen.matthiesen_core.common.abstracts.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.world.item.Item;

/**
 * Convenience base class for registries that register {@link Item} instances.
 *
 * <p>This type locks registration to the item registry category by wiring
 * {@link SupportedRegistries#ITEM} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractItemRegistry extends AbstractRegistry<Item> {
    /**
     * Creates an item registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractItemRegistry(String modId) {
        super(modId, SupportedRegistries.ITEM);
    }

    /**
     * Creates an item registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform item registrations
     */
    protected AbstractItemRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.ITEM);
    }
}

