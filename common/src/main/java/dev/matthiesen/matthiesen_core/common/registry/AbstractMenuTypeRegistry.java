package dev.matthiesen.matthiesen_core.common.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.registry.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

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

    /**
     * Registers a menu type with the given name and supplier.
     * @param name the name of the menu type
     * @param supplier the supplier for the menu type
     * @return a supplier for the registered menu type
     * @param <T> the type of the menu
     */
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, MenuType.MenuSupplier<T> supplier) {
        return register(name, () -> new MenuType<>(supplier, FeatureFlagSet.of()));
    }

    /**
     * Registers a menu type with the given name, supplier, and feature flag set.
     * @param name the name of the menu type
     * @param supplier the supplier for the menu type
     * @param featureFlagSet the feature flag set for the menu type
     * @return a supplier for the registered menu type
     * @param <T> the type of the menu
     */
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, MenuType.MenuSupplier<T> supplier, FeatureFlagSet featureFlagSet) {
        return register(name, () -> new MenuType<>(supplier, featureFlagSet));
    }
}

