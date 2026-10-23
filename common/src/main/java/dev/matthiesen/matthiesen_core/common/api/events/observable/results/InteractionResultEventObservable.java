package dev.matthiesen.matthiesen_core.common.api.events.observable.results;

import dev.matthiesen.matthiesen_core.common.api.events.observable.EventObservable;
import dev.matthiesen.matthiesen_core.common.api.events.observable.EventPriority;
import dev.matthiesen.matthiesen_core.common.api.events.observable.EventSubscription;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A thread-safe, priority-ordered observable for events that return an {@link InteractionResult}.
 *
 * <h2>Dispatch behavior</h2>
 * <ul>
 *   <li>Listeners are invoked in {@link EventPriority#HIGHEST} → {@link EventPriority#LOWEST} order.</li>
 *   <li>Within the same priority tier, listeners fire in the order they were registered.</li>
 *   <li>If any listener returns {@link InteractionResult#FAIL}, dispatch stops immediately and
 *       {@link InteractionResult#FAIL} is returned to the platform bridge — no subsequent listeners
 *       are called for that emit.</li>
 *   <li>If no listener returns {@link InteractionResult#FAIL}, {@link InteractionResult#PASS} is returned.</li>
 *   <li>Exceptions thrown by listeners are logged and suppressed; dispatch continues to the next listener.</li>
 * </ul>
 *
 * <h2>Unsubscribe semantics</h2>
 * <p>Calling {@link EventSubscription#unsubscribe()} marks the listener for removal on the next emit cycle;
 * the current in-flight emit is not affected.</p>
 *
 * @param <T> the event type
 * @see EventObservable
 * @see PlatformEvents
 */
@SuppressWarnings("unused")
public final class InteractionResultEventObservable<T> extends ResultEventObservable<T, InteractionResult> {
    private static final Logger LOGGER = LogManager.getLogger(InteractionResultEventObservable.class);

    /**
     * Creates a new {@code ResultEventObservable} instance.
     */
    public InteractionResultEventObservable() {}

    // -------------------------------------------------------------------------
    // Emit
    // -------------------------------------------------------------------------

    /**
     * Dispatches {@code event} to active subscribers in priority and registration order.
     *
     * <p>Returns {@link InteractionResult#FAIL} and stops immediately if any listener returns
     * {@link InteractionResult#FAIL}. Otherwise returns {@link InteractionResult#PASS}.</p>
     *
     * @param event the event to dispatch; must not be {@code null}
     * @return {@link InteractionResult#FAIL} if the event was cancelled, otherwise {@link InteractionResult#PASS}
     */
    @Override
    public InteractionResult emit(T event) {
        var entries = getEntries();
        List<Entry<T, InteractionResult>> snapshot = new ArrayList<>(entries.size());
        List<Entry<T, InteractionResult>> dead = new ArrayList<>();

        for (Entry<T, InteractionResult> e : entries) {
            if (e.active().get()) {
                snapshot.add(e);
            } else {
                dead.add(e);
            }
        }

        if (!dead.isEmpty()) {
            removeEntries(dead);
        }

        snapshot.sort(Comparator.comparingInt((Entry<T, InteractionResult> a) -> a.priority()).thenComparingLong(Entry::sequence));

        for (Entry<T, InteractionResult> entry : snapshot) {
            try {
                InteractionResult result = entry.listener().handle(event);
                if (result == InteractionResult.FAIL) {
                    return InteractionResult.FAIL;
                }
            } catch (Throwable t) {
                LOGGER.error("[PlatformEvents] Unhandled exception in result listener for event '{}': {}",
                        event.getClass().getSimpleName(), t.getMessage(), t);
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * Dispatches a {@link PlayerEvent.UseItem} event to active subscribers in priority and registration order.
     *
     * <p>Returns an {@link InteractionResultHolder} containing the result of the dispatch and the item stack
     * in the player's hand. If any listener returns {@link InteractionResult#FAIL}, dispatch stops immediately
     * and {@link InteractionResult#FAIL} is returned in the holder.</p>
     *
     * @param event the player use item event to dispatch; must not be {@code null}
     * @return an {@link InteractionResultHolder} containing the result of the dispatch and the item stack in the player's hand
     */
    @SuppressWarnings("unchecked")
    public InteractionResultHolder<ItemStack> emitPlayerUseItem(PlayerEvent.UseItem event) {
        InteractionResult result = emit((T) event);
        return new InteractionResultHolder<>(result, event.player().getItemInHand(event.hand()));
    }
}

