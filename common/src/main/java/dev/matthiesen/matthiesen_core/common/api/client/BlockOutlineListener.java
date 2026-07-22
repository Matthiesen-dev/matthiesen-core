package dev.matthiesen.matthiesen_core.common.api.client;

/**
 * Listener invoked when a block outline highlight is about to be rendered.
 */
@FunctionalInterface
public interface BlockOutlineListener {
    /**
     * Called before the default block outline is rendered.
     *
     * @param context The current block outline render context.
     * @return {@code true} to continue with default rendering, {@code false} to cancel it.
     */
    boolean onBlockOutline(BlockOutlineContext context);
}

