package dev.matthiesen.matthiesen_core.common.api.client.keybinds;

import net.minecraft.client.KeyMapping;

/**
 * Encapsulates a key mapping and optional per-tick logic.
 */
public interface KeybindMapping {
    /**
     * Returns the key mapping to register.
     */
    KeyMapping getKeybind();

    /**
     * Called each client tick while the game is running.
     */
    default void onClientTick() {
    }
}

