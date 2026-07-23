package dev.matthiesen.matthiesen_core.common.api.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * A thread-safe, priority-ordered observable for void (non-result) events.
 *
 * <h2>Dispatch behavior</h2>
 * <ul>
 *   <li>Listeners are invoked in {@link EventPriority#HIGHEST} → {@link EventPriority#LOWEST} order.</li>
 *   <li>Within the same priority tier, listeners fire in the order they were registered.</li>
 *   <li>If a listener throws any {@link Throwable}, the exception is logged and dispatch continues
 *       with the remaining listeners — one faulty mod cannot block others.</li>
 * </ul>
 *
 * <h2>Unsubscribe semantics</h2>
 * <p>Calling {@link EventSubscription#unsubscribe()} on the returned handle marks the listener as
 * inactive. The current in-flight emit is unaffected; the listener is removed before the next emit.</p>
 *
 * @param <T> the event type
 * @see ResultEventObservable
 * @see PlatformEvents
 */
@SuppressWarnings("unused")
public final class EventObservable<T> {

    private static final Logger LOGGER = LogManager.getLogger(EventObservable.class);

    private final CopyOnWriteArrayList<Entry<T>> entries = new CopyOnWriteArrayList<>();
    private final AtomicLong sequenceCounter = new AtomicLong();

    // -------------------------------------------------------------------------
    // Subscription
    // -------------------------------------------------------------------------

    /**
     * Subscribes a listener at {@link EventPriority#NORMAL} priority.
     *
     * @param listener the listener to register
     * @return an {@link EventSubscription} handle that can be used to remove the listener later
     */
    public EventSubscription subscribe(Consumer<T> listener) {
        return subscribe(EventPriority.NORMAL, listener);
    }

    /**
     * Subscribes a listener at the specified priority.
     *
     * @param priority the dispatch priority; lower enum ordinals fire first
     * @param listener the listener to register
     * @return an {@link EventSubscription} handle that can be used to remove the listener later
     */
    public EventSubscription subscribe(EventPriority priority, Consumer<T> listener) {
        AtomicBoolean active = new AtomicBoolean(true);
        entries.add(new Entry<>(priority.getValue(), sequenceCounter.getAndIncrement(), listener, active));
        return EventSubscription.of(active);
    }

    // -------------------------------------------------------------------------
    // Emit
    // -------------------------------------------------------------------------

    /**
     * Dispatches {@code event} to all currently active subscribers in priority and registration order.
     *
     * <p>Any listeners whose {@link EventSubscription} was unsubscribed before this call will be
     * removed from the internal list during this emit and will not be invoked.</p>
     *
     * @param event the event to dispatch; must not be {@code null}
     */
    public void emit(T event) {
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
                entry.listener().accept(event);
            } catch (Throwable t) {
                LOGGER.error("[PlatformEvents] Unhandled exception in listener for event '{}': {}",
                        event.getClass().getSimpleName(), t.getMessage(), t);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private record Entry<E>(int priority, long sequence, Consumer<E> listener, AtomicBoolean active) {}
}

