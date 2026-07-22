package dev.matthiesen.matthiesen_core.common.api.client;

import net.minecraft.client.KeyMapping;

import java.util.function.Supplier;

/**
 * Utility interface for registering key mappings in a platform-neutral way.
 */
@FunctionalInterface
public interface KeyMappingRegistrar {
    /**
     * Registers a key mapping during client setup.
     * @param keyMapping The key mapping to register.
     */
    void register(KeyMapping keyMapping);

    /**
     * Registers a key mapping exposed as a supplier from common registration code.
     * @param keyMappingSupplier A supplier that provides the key mapping.
     */
    default void register(Supplier<? extends KeyMapping> keyMappingSupplier) {
        register(keyMappingSupplier.get());
    }
}

