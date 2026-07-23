package dev.matthiesen.matthiesen_core.common.abstracts.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.registry.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.world.inventory.MenuType;

/**
 * Convenience base class for registries that register {@link MenuType} instances.
 *
 * <p>This type locks registration to the menu type registry category by wiring
 * {@link SupportedRegistries#MENU_TYPE} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractMenuTypeRegistry extends AbstractRegistry<MenuType<?>> {
    /**
     * Creates a menu type registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractMenuTypeRegistry(String modId) {
        super(modId, SupportedRegistries.MENU_TYPE);
    }

    /**
     * Creates a menu type registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform menu type registrations
     */
    protected AbstractMenuTypeRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.MENU_TYPE);
    }
}

