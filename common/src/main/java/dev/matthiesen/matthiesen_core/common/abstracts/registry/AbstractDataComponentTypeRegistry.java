package dev.matthiesen.matthiesen_core.common.abstracts.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.registry.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.core.component.DataComponentType;

/**
 * Convenience base class for registries that register {@link DataComponentType} instances.
 *
 * <p>This type locks registration to the data component type registry category by wiring
 * {@link SupportedRegistries#DATA_COMPONENT_TYPE} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractDataComponentTypeRegistry extends AbstractRegistry<DataComponentType<?>> {
    /**
     * Creates a data component type registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractDataComponentTypeRegistry(String modId) {
        super(modId, SupportedRegistries.DATA_COMPONENT_TYPE);
    }

    /**
     * Creates a data component type registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform data component type registrations
     */
    protected AbstractDataComponentTypeRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.DATA_COMPONENT_TYPE);
    }
}

