package dev.matthiesen.matthiesen_core.common.api.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread-safe, priority-ordered observable for events that return a cancellation flag.
 *
 * <h2>Dispatch behavior</h2>
 * <ul>
 *   <li>Listeners are invoked in {@link EventPriority#HIGHEST} -> {@link EventPriority#LOWEST} order.</li>
 *   <li>Within the same priority tier, listeners fire in the order they were registered.</li>
 *   <li>If any listener returns {@code true}, dispatch stops immediately and the emit call returns {@code true}.</li>
 *   <li>If no listener returns {@code true}, emit returns {@code false}.</li>
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
public final class BooleanResultEventObservable<T> {

    private static final Logger LOGGER = LogManager.getLogger(BooleanResultEventObservable.class);

    private final CopyOnWriteArrayList<Entry<T>> entries = new CopyOnWriteArrayList<>();
    private final AtomicLong sequenceCounter = new AtomicLong();

    /**
     * Creates a new {@code BooleanResultEventObservable} instance.
     */
    public BooleanResultEventObservable() {}

    // -------------------------------------------------------------------------
    // Listener type
    // -------------------------------------------------------------------------

    /**
     * A listener that receives an event and returns {@code true} to cancel it.
     *
     * @param <T> the event type
     */
    @FunctionalInterface
    public interface ResultListener<T> {

        /**
         * Handles the event and returns {@code true} to cancel it, or {@code false} to allow it to continue.
         *
         * @param event the event to handle
         * @return {@code true} to cancel the event, {@code false} to allow it to continue
         */
        boolean handle(T event);
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
     * @param event the event to dispatch; must not be {@code null}
     * @return {@code true} if the event was cancelled, otherwise {@code false}
     */
    public boolean emit(T event) {
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
                if (entry.listener().handle(event)) {
                    return true;
                }
            } catch (Throwable t) {
                LOGGER.error("[PlatformEvents] Unhandled exception in result listener for event '{}': {}",
                        event.getClass().getSimpleName(), t.getMessage(), t);
            }
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private record Entry<E>(int priority, long sequence, ResultListener<E> listener, AtomicBoolean active) {}
}

