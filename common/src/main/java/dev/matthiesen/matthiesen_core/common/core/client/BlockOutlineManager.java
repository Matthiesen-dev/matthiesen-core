package dev.matthiesen.matthiesen_core.common.core.client;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.common.api.client.BlockOutlineContext;
import dev.matthiesen.matthiesen_core.common.api.client.BlockOutlineListener;
import dev.matthiesen.matthiesen_core.common.api.client.BlockOutlineRegistrar;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Manages block outline listeners in common code and dispatches outline events to all registered listeners.
 *
 * <p>The manager installs a single platform callback during initialization and keeps listeners registered
 * for the lifetime of the client.</p>
 */
@SuppressWarnings("unused")
public final class BlockOutlineManager {
    private static final List<BlockOutlineListener> BLOCK_OUTLINE_LISTENERS = new CopyOnWriteArrayList<>();

    /**
     * Shared singleton instance used by common client bootstrap.
     */
    public static final BlockOutlineManager INSTANCE = new BlockOutlineManager();

    private BlockOutlineManager() {}

    private boolean initialized;

    /**
     * Installs the platform block outline callback once and wires it to this manager's listener list.
     *
     * @param clientEventsListeners platform event listener bridge
     */
    public synchronized void initialize(CommonLoaderClientEventsListeners clientEventsListeners) {
        if (initialized) {
            return;
        }
        initialized = true;
        clientEventsListeners.applyBlockHighlightOverrides(this::notifyListeners);
        MatthiesenCoreCommonClient.INSTANCE.createInfoLog("Initialized BlockOutlineManager");
    }

    /**
     * Registers a block outline listener that remains active for future outline events.
     *
     * @param listener listener to register
     */
    public void registerListener(BlockOutlineListener listener) {
        BLOCK_OUTLINE_LISTENERS.add(listener);
    }

    /**
     * Registers multiple block outline listeners using a registrar callback.
     *
     * @param registrarConsumer callback that receives a registrar helper
     */
    public void registerListener(Consumer<BlockOutlineRegistrar> registrarConsumer) {
        registrarConsumer.accept(this::registerListener);
    }

    private boolean notifyListeners(BlockOutlineContext context) {
        for (BlockOutlineListener listener : BLOCK_OUTLINE_LISTENERS) {
            if (!listener.onBlockOutline(context)) {
                return false;
            }
        }
        return true;
    }
}
