package dev.matthiesen.matthiesen_core.common.api.events.client;

import dev.matthiesen.matthiesen_core.common.api.client.block_outline.BlockOutlineContext;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudRegistrar;

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
     * Fired when HUD layers should be registered.
     *
     * <p>Listeners should register layers via the provided registrar using explicit ordering and resource IDs.
     * On NeoForge, this maps to native layer registration. On Fabric, ordering is applied by common fallback rendering.</p>
     *
     * @param registrar HUD layer registrar
     */
    public record HudRegistration(HudRegistrar registrar) {}

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
