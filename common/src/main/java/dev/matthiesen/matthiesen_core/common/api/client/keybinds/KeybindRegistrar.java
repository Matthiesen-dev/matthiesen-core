package dev.matthiesen.matthiesen_core.common.api.client.keybinds;

import net.minecraft.client.KeyMapping;

/**
 * Utility interface for registering named keybind mappings.
 */
@FunctionalInterface
public interface KeybindRegistrar {
    /**
     * Registers a named keybind mapping.
     */
    void register(String name, KeybindMapping keybind);

    /**
     * Registers a named key mapping with no tick callback.
     */
    default void register(String name, KeyMapping keyMapping) {
        register(name, keyMapping, () -> {
        });
    }

    /**
     * Registers a named key mapping with a tick callback.
     */
    default void register(String name, KeyMapping keyMapping, Runnable onClientTick) {
        register(name, new KeybindMapping() {
            @Override
            public KeyMapping getKeybind() {
                return keyMapping;
            }

            @Override
            public void onClientTick() {
                onClientTick.run();
            }
        });
    }
}

