package dev.matthiesen.matthiesen_core.common.api.events.client;

import dev.matthiesen.matthiesen_core.common.api.client.block_outline.BlockOutlineContext;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Typed records for all client-side event types.
 *
 * <p>These records are emitted through the {@link dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents} registry
 * and dispatched to registered listeners in priority order.</p>
 */
public final class ClientEvent {
    private ClientEvent() {}

    /**
     * Fired when the client is stopping.
     *
     * <p>Emitted once during client shutdown, allowing mods to perform cleanup tasks.</p>
     */
    public record Stopping() {}

    /**
     * Fired at the end of each client tick.
     *
     * <p>Emitted after all client-side processing for the current tick has completed.</p>
     */
    public record EndTick() {}

    /**
     * Fired when the HUD is being rendered.
     *
     * <p>Emitted during each client frame, allowing mods to render additional HUD elements.</p>
     *
     * @param drawContext the GUI graphics context for rendering
     * @param tickCounter the frame delta tracker for animation
     */
    public record HudRender(GuiGraphics drawContext, DeltaTracker tickCounter) {}

    /**
     * Fired when a block outline highlight is about to be rendered.
     *
     * <p>Emitted before the default block outline is rendered. Result-based event: first listener returning
     * {@link net.minecraft.world.InteractionResult#FAIL} short-circuits dispatch and cancels rendering.</p>
     *
     * @param context the current block outline render context
     */
    public record BlockHighlight(BlockOutlineContext context) {}
}

