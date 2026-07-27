package dev.matthiesen.matthiesen_core.common.api.events;

import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import dev.matthiesen.matthiesen_core.common.api.events.server.ServerEvent;

/**
 * Central registry of all server-side platform events provided by Matthiesen Core.
 *
 * <p>Each public static field is either an {@link EventObservable} (void dispatch) or a
 * {@link ResultEventObservable} ({@link net.minecraft.world.InteractionResult}-returning dispatch).
 * Access them directly to subscribe listeners — no manager or registration step is required.</p>
 *
 * <h2>Dispatch rules</h2>
 * <ul>
 *   <li>Listeners fire from {@link EventPriority#HIGHEST} to {@link EventPriority#LOWEST}.</li>
 *   <li>Listeners at the same priority fire in the order they were registered.</li>
 *   <li>For {@link ResultEventObservable} fields: returning {@link net.minecraft.world.InteractionResult#FAIL}
 *       from any listener immediately cancels the action and stops further listeners.</li>
 *   <li>Exceptions thrown by any listener are logged and dispatch continues.</li>
 *   <li>Calling {@link EventSubscription#unsubscribe()} takes effect on the <em>next</em> emit cycle.</li>
 * </ul>
 *
 * <h2>Basic usage</h2>
 * <pre>{@code
 * // Subscribe at default (NORMAL) priority — no handle needed
 * PlatformEvents.SERVER_STARTING.subscribe(event -> {
 *     MinecraftServer server = event.server();
 * });
 *
 * // Subscribe at a specific priority and keep a handle for later removal
 * EventSubscription sub = PlatformEvents.PLAYER_JOIN.subscribe(EventPriority.HIGH, event -> {
 *     ServerPlayer player = event.player();
 * });
 * // ...later:
 * sub.unsubscribe();
 *
 * // Cancel an item-use interaction
 * PlatformEvents.PLAYER_USE_ITEM.subscribe(event -> {
 *     if (isForbidden(event.player(), event.hand())) {
 *         return InteractionResult.FAIL;
 *     }
 *     return InteractionResult.PASS;
 * });
 * }</pre>
 *
 * @see ServerEvent
 * @see PlayerEvent
 * @see EventPriority
 * @see EventSubscription
 */
@SuppressWarnings("unused")
public final class PlatformEvents {

    private PlatformEvents() {}

    // =========================================================================
    // Server lifecycle
    // =========================================================================

    /**
     * Fired when the server begins its startup sequence. World data has not yet been loaded
     * and players cannot connect.
     */
    public static final EventObservable<ServerEvent.Starting> SERVER_STARTING = new EventObservable<>();

    /**
     * Fired when the server has fully started and is ready to accept player connections.
     */
    public static final EventObservable<ServerEvent.Started> SERVER_STARTED = new EventObservable<>();

    /**
     * Fired when the server begins its shutdown sequence.
     */
    public static final EventObservable<ServerEvent.Stopping> SERVER_STOPPING = new EventObservable<>();

    /**
     * Fired after the server has fully stopped and all worlds have been saved.
     */
    public static final EventObservable<ServerEvent.Stopped> SERVER_STOPPED = new EventObservable<>();

    // =========================================================================
    // Server ticking
    // =========================================================================

    /**
     * Fired at the beginning of each server tick, before game logic is processed.
     *
     * <p><strong>Warning:</strong> this event fires every tick (~20 times per second).
     * Keep handlers lightweight.</p>
     */
    public static final EventObservable<ServerEvent.StartTick> SERVER_START_TICK = new EventObservable<>();

    /**
     * Fired at the end of each server tick, after all game logic has been processed.
     *
     * <p><strong>Warning:</strong> this event fires every tick (~20 times per second).
     * Keep handlers lightweight.</p>
     */
    public static final EventObservable<ServerEvent.EndTick> SERVER_END_TICK = new EventObservable<>();

    // =========================================================================
    // Server reload
    // =========================================================================

    /**
     * Fired when the server completes a data pack reload.
     *
     * <p>No server instance is available in the event because the reload pipeline does not expose the
     * server uniformly across all loaders. If you need the server reference, cache it from
     * {@link #SERVER_STARTED}.</p>
     */
    public static final EventObservable<ServerEvent.Reload> SERVER_RELOAD = new EventObservable<>();

    // =========================================================================
    // Player connection
    // =========================================================================

    /**
     * Fired when a player successfully joins the server.
     */
    public static final EventObservable<PlayerEvent.Join> PLAYER_JOIN = new EventObservable<>();

    /**
     * Fired when a player disconnects from the server for any reason.
     */
    public static final EventObservable<PlayerEvent.Leave> PLAYER_LEAVE = new EventObservable<>();

    // =========================================================================
    // Player ticking
    // =========================================================================

    /**
     * Fired at the start of each server tick for each player, before game logic is processed.
     */
    public static final EventObservable<PlayerEvent.PreTick> PLAYER_PRE_TICK = new EventObservable<>();

    /**
     * Fired at the end of each server tick for each player, after all game logic has been processed.
     */
    public static final EventObservable<PlayerEvent.EndTick> PLAYER_END_TICK = new EventObservable<>();

    // =========================================================================
    // Player interaction — result-based, cancellable
    // =========================================================================

    /**
     * Fired when a server-side player uses an item.
     *
     * <p>Return {@link net.minecraft.world.InteractionResult#FAIL} to cancel the action immediately.
     * No subsequent listeners at lower priorities will be invoked after a cancellation.</p>
     */
    public static final ResultEventObservable<PlayerEvent.UseItem> PLAYER_USE_ITEM = new ResultEventObservable<>();

    /**
     * Fired when a server-side player right-clicks a block.
     *
     * <p>Return {@link net.minecraft.world.InteractionResult#FAIL} to cancel the action immediately.
     * No subsequent listeners at lower priorities will be invoked after a cancellation.</p>
     */
    public static final ResultEventObservable<PlayerEvent.UseBlock> PLAYER_USE_BLOCK = new ResultEventObservable<>();

    // =========================================================================
    // Internal bootstrap — do not call from mod code
    // =========================================================================

    /**
     * Wires platform-specific loader callbacks into the common event observables.
     */
    public static void initialize() {
        // Handled Externally by EventBusSubscriber (Fabric/NeoForge)
        // - SERVER_STARTING, SERVER_STARTED, SERVER_STOPPING, SERVER_STOPPED
        // - SERVER_START_TICK, SERVER_END_TICK, SERVER_RELOAD
        // - PLAYER_JOIN, PLAYER_LEAVE
        // - PLAYER_USE_ITEM, PLAYER_USE_BLOCK
        // Handled Externally by Mixin (Fabric) and EventBusSubscriber (NeoForge)
        // - PLAYER_PRE_TICK, PLAYER_END_TICK
    }
}

