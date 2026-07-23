package dev.matthiesen.matthiesen_core.common.api.events;

import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import net.minecraft.world.InteractionResult;

/**
 * Central registry for all client-side platform events.
 *
 * <p>This class manages a set of observables that dispatch client-side events (lifecycle, HUD render, block highlight)
 * to registered listeners. It is initialized during client setup and wired to platform-specific event systems via
 * the {@link CommonLoaderClientEventsListeners} bridge.</p>
 *
 * <p>Dispatch semantics:</p>
 * <ul>
 *   <li><strong>Lifecycle events (CLIENT_STOPPING, CLIENT_END_TICK):</strong> Dispatch in priority order (HIGHEST → LOWEST),
 *       then by registration order. Exceptions are logged and suppressed; dispatch continues.</li>
 *   <li><strong>HUD render (HUD_RENDER):</strong> Same priority and exception handling as lifecycle events.</li>
 *   <li><strong>Block highlight (BLOCK_HIGHLIGHT):</strong> Result-based event. First listener returning FAIL immediately
 *       stops dispatch and returns FAIL to the platform. All other listeners must return PASS for rendering to continue.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Subscribe to HUD render event with default priority
 * PlatformClientEvents.HUD_RENDER.subscribe(event ->
 *     drawContext.drawString(minecraft.font, "Hello", 10, 10, 0xFFFFFF));
 *
 * // Subscribe to block highlight with custom priority
 * PlatformClientEvents.BLOCK_HIGHLIGHT.subscribe(EventPriority.HIGH, event ->
 *     shouldSkipBlock(event.context().blockHitResult())
 *         ? InteractionResult.FAIL
 *         : InteractionResult.PASS);
 * }</pre>
 */
public final class PlatformClientEvents {
    private PlatformClientEvents() {}

    /**
     * Fired when the client is stopping.
     *
     * <p>Void event. Listeners are dispatched in priority order; exceptions are logged and suppressed.</p>
     */
    public static final EventObservable<ClientEvent.Stopping> CLIENT_STOPPING = new EventObservable<>();

    /**
     * Fired at the end of each client tick.
     *
     * <p>Void event. Listeners are dispatched in priority order; exceptions are logged and suppressed.</p>
     */
    public static final EventObservable<ClientEvent.EndTick> CLIENT_END_TICK = new EventObservable<>();

    /**
     * Fired when the HUD is being rendered.
     *
     * <p>Void event. Listeners are dispatched in priority order; exceptions are logged and suppressed.
     * Emitted on every frame during client rendering.</p>
     */
    public static final EventObservable<ClientEvent.HudRender> HUD_RENDER = new EventObservable<>();

    /**
     * Fired when a block outline highlight is about to be rendered.
     *
     * <p>Result-based event returning {@link InteractionResult}. First listener returning FAIL stops dispatch
     * and cancels rendering. All other listeners must return PASS. Exceptions are logged and suppressed.</p>
     */
    public static final ResultEventObservable<ClientEvent.BlockHighlight> BLOCK_HIGHLIGHT = new ResultEventObservable<>();

    /**
     * Initializes the client-side event system by wiring platform-specific event callbacks to the observables.
     *
     * <p>Called once during client setup by {@link dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient#initialize()}.</p>
     *
     * @param loader the platform-specific client event listener bridge
     */
    public static void initialize(CommonLoaderClientEventsListeners loader) {
        loader.onClientStopping(() -> CLIENT_STOPPING.emit(new ClientEvent.Stopping()));
        loader.endClientTick(() -> CLIENT_END_TICK.emit(new ClientEvent.EndTick()));
        loader.onHudRender(PlatformClientEvents::emitHudRender);
        loader.applyBlockHighlightOverrides(PlatformClientEvents::emitBlockHighlight);
    }

    private static void emitHudRender(ClientEvent.HudRender event) {
        HUD_RENDER.emit(event);
    }

    private static void emitBlockHighlight(ClientEvent.BlockHighlight event) {
        BLOCK_HIGHLIGHT.emit(event);
    }
}




