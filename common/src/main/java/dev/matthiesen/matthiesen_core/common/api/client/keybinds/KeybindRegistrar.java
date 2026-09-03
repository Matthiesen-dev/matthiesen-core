package dev.matthiesen.matthiesen_core.common.api.client.keybinds;

import net.minecraft.client.KeyMapping;

/**
 * Utility interface for registering named keybind mappings.
 */
@FunctionalInterface
public interface KeybindRegistrar {
    /**
     * Registers a named keybind mapping.
     * @param name The name of the keybind mapping.
     * @param keybind The keybind mapping to register.
     */
    void register(String name, KeybindMapping keybind);

    /**
     * Registers a named key mapping with no tick callback.
     * @param name The name of the key mapping.
     * @param keyMapping The key mapping to register.
     */
    default void register(String name, KeyMapping keyMapping) {
        register(name, keyMapping, () -> {
        });
    }

    /**
     * Registers a named key mapping with a tick callback.
     * @param name The name of the key mapping.
     * @param keyMapping The key mapping to register.
     * @param onClientTick The callback to run each client tick while the game is running
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

