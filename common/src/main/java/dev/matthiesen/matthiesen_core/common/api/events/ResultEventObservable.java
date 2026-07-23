package dev.matthiesen.matthiesen_core.common.api.events;

import net.minecraft.world.InteractionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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
public final class ResultEventObservable<T> {

    private static final Logger LOGGER = LogManager.getLogger(ResultEventObservable.class);

    private final CopyOnWriteArrayList<Entry<T>> entries = new CopyOnWriteArrayList<>();
    private final AtomicLong sequenceCounter = new AtomicLong();

    // -------------------------------------------------------------------------
    // Listener type
    // -------------------------------------------------------------------------

    /**
     * A listener that receives an event and returns an {@link InteractionResult} indicating whether
     * the action should proceed.
     *
     * <p>Return {@link InteractionResult#FAIL} to cancel the event immediately. Return
     * {@link InteractionResult#PASS} (or any other non-{@code FAIL} result) to allow dispatch
     * to continue to lower-priority listeners.</p>
     *
     * @param <T> the event type
     */
    @FunctionalInterface
    public interface ResultListener<T> {
        InteractionResult handle(T event);
    }

    // -------------------------------------------------------------------------
    // Subscription
    // -------------------------------------------------------------------------

    /**
     * Subscribes a result listener at {@link EventPriority#NORMAL} priority.
     *
     * @param listener the listener to register
     * @return an {@link EventSubscription} handle that can be used to remove the listener later
     */
    public EventSubscription subscribe(ResultListener<T> listener) {
        return subscribe(EventPriority.NORMAL, listener);
    }

    /**
     * Subscribes a result listener at the specified priority.
     *
     * @param priority the dispatch priority; lower enum ordinals fire first
     * @param listener the listener to register
     * @return an {@link EventSubscription} handle that can be used to remove the listener later
     */
    public EventSubscription subscribe(EventPriority priority, ResultListener<T> listener) {
        AtomicBoolean active = new AtomicBoolean(true);
        entries.add(new Entry<>(priority.getValue(), sequenceCounter.getAndIncrement(), listener, active));
        return EventSubscription.of(active);
    }

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
    public InteractionResult emit(T event) {
        List<Entry<T>> snapshot = new ArrayList<>(entries.size());
        List<Entry<T>> dead = new ArrayList<>();

        for (Entry<T> e : entries) {
            if (e.active().get()) {
                snapshot.add(e);
            } else {
                dead.add(e);
            }
        }

        if (!dead.isEmpty()) {
            entries.removeAll(dead);
        }

        snapshot.sort(Comparator.comparingInt((Entry<T> a) -> a.priority()).thenComparingLong(Entry::sequence));

        for (Entry<T> entry : snapshot) {
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

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private record Entry<E>(int priority, long sequence, ResultListener<E> listener, AtomicBoolean active) {}
}

