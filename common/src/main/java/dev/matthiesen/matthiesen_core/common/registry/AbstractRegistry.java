package dev.matthiesen.matthiesen_core.common.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.registry.ConfigurableRegistry;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;

import java.util.function.Supplier;

/**
 * An abstract implementation of a configurable registry that provides basic functionality for registering entries.
 * @param <T> the type of entries in this registry
 */
@SuppressWarnings("unused")
public abstract class AbstractRegistry<T> implements ConfigurableRegistry<T> {
    private final RegistryBuilder registryBuilder;
    private final SupportedRegistries<T> supportedRegistry;

    /**
     * Creates a new AbstractRegistry with the given mod ID and supported registry category.
     * @param modId the mod ID to use for this registry
     * @param supportedRegistry the supported registry category for this registry
     */
    protected AbstractRegistry(String modId, SupportedRegistries<T> supportedRegistry) {
        this(new RegistryBuilder(modId), supportedRegistry);
    }

    /**
     * Creates a new AbstractRegistry with the given registry builder and supported registry category.
     * @param registryBuilder the registry builder to use for this registry
     * @param supportedRegistry the supported registry category for this registry
     */
    protected AbstractRegistry(RegistryBuilder registryBuilder, SupportedRegistries<T> supportedRegistry) {
        this.registryBuilder = registryBuilder;
        this.supportedRegistry = supportedRegistry;
    }

    @Override
    public final <T1 extends T> Supplier<T1> register(String name, Supplier<T1> entry) {
        return supportedRegistry.register(registryBuilder, name, entry);
    }

    /**
     * Registers an entry with the given name and value.
     * @return the registry builder backing this registry
     */
    protected final RegistryBuilder getRegistryBuilder() {
        return registryBuilder;
    }

    /**
     * Gets the supported registry category used by this registry.
     * @return the supported registry category used by this registry
     */
    protected final SupportedRegistries<T> getSupportedRegistry() {
        return supportedRegistry;
    }
}
