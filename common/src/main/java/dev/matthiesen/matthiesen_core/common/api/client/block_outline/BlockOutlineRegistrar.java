package dev.matthiesen.matthiesen_core.common.api.client.block_outline;

/**
 * Utility interface for registering block outline listeners.
 */
@FunctionalInterface
public interface BlockOutlineRegistrar {
    /**
     * Registers a block outline listener.
     *
     * @param listener The listener to register.
     */
    void register(BlockOutlineListener listener);
}

